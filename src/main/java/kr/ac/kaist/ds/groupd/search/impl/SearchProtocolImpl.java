
package kr.ac.kaist.ds.groupd.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;
import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.search.SearchProtocol;
import kr.ac.kaist.ds.groupd.search.SearchQuery;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;

public class SearchProtocolImpl implements SearchProtocol {

    private static final String PAR_NAME_PROTOCOL = "naming";

    private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

    private static final String PAR_GOSSIP_PROBABILITY = "prob";
    
    private static final String PAR_QUERY_TTL = "query.ttl";

    private Collection<SearchQuery> searchQueries = new ArrayList<>();

    private int namingProtocolPid;

    private int interestGroupProtocolPid;

    private double probabilityPk;

    private int quertyTtl;
    
    public SearchProtocolImpl(String prefix) {
        this.namingProtocolPid = Configuration.getPid(prefix + "." + PAR_NAME_PROTOCOL);
        this.interestGroupProtocolPid = Configuration
                .getPid(prefix + "." + PAR_INTEREST_GROUP_PROTOCOL);
        this.probabilityPk = Configuration.getDouble(prefix + "." + PAR_GOSSIP_PROBABILITY);
        this.quertyTtl = Configuration.getInt(prefix + "." + PAR_QUERY_TTL);
    }

    /**
     * Performs the forward search based on the ADGSA algorithm.
     * 
     * @param searchQuery
     */
    private void performSearch(Node node, int pid, SearchQuery searchQuery) {
        // target reached return the start source.
        if (isDestinationReached(node, searchQuery)) {
            searchQuery.setBackward(true);
            //put it back in, because we removed it
            this.searchQueries.add(searchQuery);
            Logger.getLogger(getClass().getName()).info("Destination found");
            return;
        }
        if (isSearchBackAtSource(node, searchQuery)) {
            Logger.getLogger(getClass().getName()).info("Back at source, search unsuccessful.");
            return;
        }
        InterestProtocol interestProtocol = getInterestProtocolFrom(node);
        Collection<Node> neighbours = interestProtocol.getNeighbours();

        // while Pm is not the last peer in the search path do
        if (!neighbours.isEmpty() || !hasNodeAlreadyBeenVisited(node, searchQuery)) {
            // Send Mq to the neighbrs in the set S = { s1..}
            List<Node> orangeNodes = sendQueryToNeighboursWithProbability(node, pid, searchQuery);

            // line 5 - 9
            Node neighbourWithHighestDegree = findNeighbourWithHighestDegree(node, pid);
            SearchProtocol searchProtocol = getSearchProtocolFrom(pid, neighbourWithHighestDegree);
            SearchQuery query = copySearchQuery(node, pid, orangeNodes, searchQuery);
            searchProtocol.addSearchQuery(query);
        } else {
            Node neighbourWithSecondHighestDegree = findNeighbourWithSecondHighestDegree(node, pid);
            if (hasNodeAlreadyBeenVisited(neighbourWithSecondHighestDegree, searchQuery)) {
                sendQueryToPredecessor(node, pid, searchQuery);
            } else {
                sendQueryToSecondDegreeNeighbour(node, pid, neighbourWithSecondHighestDegree, searchQuery);
            }
        }
    }

    private boolean isSearchBackAtSource(Node node, SearchQuery searchQuery) {
        return node.getID() == searchQuery.getSource() && !searchQuery.getVisitedNodes().isEmpty();
    }

    private boolean isDestinationReached(Node node, SearchQuery searchQuery) {
        return node.getID() == searchQuery.getDestination();
    }

    private boolean hasNodeAlreadyBeenVisited(Node neighbourWithSecondHighestDegree,
            SearchQuery searchQuery) {
        return searchQuery.getVisitedNodes().contains(neighbourWithSecondHighestDegree);
    }

    private void sendQueryToPredecessor(Node node, int pid, SearchQuery searchQuery) {
        int indexOf = searchQuery.getVisitedNodes().indexOf(node);
        Node predecessor = searchQuery.getVisitedNodes().get(indexOf - 1);
        SearchProtocol searchProtocolPredecessor = (SearchProtocol)predecessor.getProtocol(pid);
        searchProtocolPredecessor.addSearchQuery(searchQuery);
    }

    private void sendQueryToSecondDegreeNeighbour(Node node, int pid,
            Node neighbourWithSecondHighestDegree, SearchQuery searchQuery) {
        SearchProtocol searchProtocol = (SearchProtocol)neighbourWithSecondHighestDegree
                .getProtocol(pid);
        SearchQuery query = copySearchQuery(node, pid, Collections.emptyList(), searchQuery);
        searchProtocol.addSearchQuery(query);
    }

    private Node findNeighbourWithHighestDegree(Node node, int pid) {
        InterestProtocol interestProtocol = (InterestProtocol)node
                .getProtocol(interestGroupProtocolPid);
        Map<Node, Integer> neighboursWithDegree = interestProtocol.getNeighbours().stream().collect(
                Collectors.toMap(n -> n, n -> getInterestProtocolFrom(n).getNeighbours().size()));
        Entry<Node, Integer> neighbourWithHighestDegree = null;
        for (Entry<Node, Integer> entry : neighboursWithDegree.entrySet()) {
            if (neighbourWithHighestDegree == null
                    || entry.getValue() > neighbourWithHighestDegree.getValue()) {
                neighbourWithHighestDegree = entry;
            }
        }
        return neighbourWithHighestDegree.getKey();
    }

    @SuppressWarnings("unused")
    private Node findNeighbourWithSecondHighestDegree(Node node, int pid) {
        InterestProtocol interestProtocol = (InterestProtocol)node.getProtocol(pid);
        Map<Node, Integer> neighboursWithDegree = interestProtocol.getNeighbours().stream()
                .collect(Collectors.toMap(n -> n,
                        n -> ((InterestProtocol)n.getProtocol(pid)).getNeighbours().size()));
        Entry<Node, Integer> neighbourWithHighestDegree = null;
        Entry<Node, Integer> neighbourWithSecondHighestDegree = null;
        for (Entry<Node, Integer> entry : neighboursWithDegree.entrySet()) {
            if (neighbourWithHighestDegree == null
                    || entry.getValue() > neighbourWithHighestDegree.getValue()) {
                neighbourWithSecondHighestDegree = neighbourWithHighestDegree;
                neighbourWithHighestDegree = entry;
            }
        }
        if (neighbourWithSecondHighestDegree == null) {
            return null;
        }
        return neighbourWithSecondHighestDegree.getKey();
    }

    /**
     * sendQueryToNeighboursWithProbability using probability
     * {@link #probabilityPk} and send the searchProtocal to the node's
     * neighbours. and return the nodes that did not receive the searchquery
     * (orange nodes);
     * 
     * @param node
     * @param pid
     * @param searchQuery 
     * @return
     */
    private List<Node> sendQueryToNeighboursWithProbability(Node node, int pid, SearchQuery searchQuery) {
        InterestProtocol interstProtocol = (InterestProtocol)node
                .getProtocol(interestGroupProtocolPid);
        Random random = new Random();
        List<Node> nodesToReceiveSearchQuery = interstProtocol.getNeighbours().stream()
                .filter(n -> probabilityPk < random.nextFloat()).collect(Collectors.toList());

        ArrayList<Node> nodesToLabelOrange = new ArrayList<>(interstProtocol.getNeighbours());
        nodesToLabelOrange.removeAll(nodesToReceiveSearchQuery);
        SearchQuery prototypeQuery = copySearchQuery(node, pid, nodesToLabelOrange, searchQuery);

        nodesToReceiveSearchQuery.forEach(n -> {
            SearchProtocol searchProtocol = (SearchProtocol)n.getProtocol(pid);
            searchProtocol.addSearchQuery(new SearchQuery(prototypeQuery));
        });
        return nodesToLabelOrange;
    }

    /**
     * Copies the old search query and add this node and this nodes group into
     * it.
     * 
     * @param node
     * @param pid
     * @param nodesToLabelOrange
     * @param searchQuery 
     * @return
     */
    private SearchQuery copySearchQuery(Node node, int pid, List<Node> nodesToLabelOrange, SearchQuery searchQuery) {
        SearchQuery q = new SearchQuery(searchQuery);
        q.addVisitedNode(node);
        q.addVisitedGroup(getGroupNameProtocolFrom(node).getGroupName());
        q.addOrangeNodes(node.getID(), nodesToLabelOrange);
        return q;
    }

    /**
     * Performs the backtracking, based on our group naming scheme. backTracking
     * follow previous steps
     * 
     * @param protocolID
     * @param node
     * @param searchQuery
     */
    private void performBacktracking(Node node, int protocolID, SearchQuery searchQuery) {
        if (node.getID() == searchQuery.getSource()) {
            Logger.getLogger(this.getClass().getName()).info("back home!");
        }
        Node lastNode = searchQuery.getVisitedNodes()
                .get(searchQuery.getVisitedNodes().size() - 1);
        InterestProtocol interestProtcol = getInterestProtocolFrom(node);
        if (interestProtcol.getNeighbours().contains(lastNode)) {
            sendQueryToLastNode(node, protocolID, lastNode, searchQuery);
            return;
        }
        Node representative = getInterestProtocolFrom(node).getRepresentative();
        if (node.equals(representative)) {
            boolean sent = sendQueryToNeighborIfItIsInVisitedList(protocolID, representative, searchQuery);
            if (sent) {
                return;
            }
            Collection<Node> neighbours = getInterestProtocolFrom(representative).getNeighbours();
            // no neighbour in the visited list found, gotta check the groups
            GroupName lastNodeGroupName = getGroupNameProtocolFrom(lastNode).getGroupName();
            boolean sentToGroup = sendQueryToNeighbourInGroupInVisitedList(protocolID, neighbours,
                    lastNodeGroupName, searchQuery);
            if (sentToGroup) {
                return;
            }
            // no group visible from here, gotta check the groups of my
            // neighbours neighbours
            boolean sentToNeighboursNeighbours = tryToSendQueryToNeighboursNeighbours(protocolID,
                    neighbours, lastNodeGroupName, searchQuery);
            if (sentToNeighboursNeighbours) {
                return;
            }
            // worst case go back to gossiping until we are back on track
            // TODO
        } else {
            // send query to representative because we do not know where to send
            // it
            getSearchProtocolFrom(protocolID, representative).addSearchQuery(searchQuery);
        }
    }

    /**
     * Retrieves the search protocol from the given node. Since we are the
     * {@link SearchProtocol} ourselves here, we do not have the pid in a field,
     * but pass it to every method from the {@link #nextCycle(Node, int)}
     * method.
     * 
     * @param protocolID
     * @param representative
     * @return
     */
    private SearchProtocol getSearchProtocolFrom(int protocolID, Node representative) {
        return (SearchProtocol)representative.getProtocol(protocolID);
    }

    private GroupNameProtocol getGroupNameProtocolFrom(Node lastNode) {
        return (GroupNameProtocol)lastNode.getProtocol(namingProtocolPid);
    }

    private InterestProtocol getInterestProtocolFrom(Node node) {
        return (InterestProtocol)node.getProtocol(interestGroupProtocolPid);
    }

    private boolean tryToSendQueryToNeighboursNeighbours(int protocolID, Collection<Node> neighbours,
            GroupName lastNodeGroupName, SearchQuery searchQuery) {
        Optional<Node> neighboursNeighbourInCorrectGroup = neighbours.stream()
                .flatMap(n -> getInterestProtocolFrom(n).getNeighbours().stream())
                .filter(nn -> getGroupNameProtocolFrom(nn).compareWithGroupName(lastNodeGroupName))
                .findAny();
        if (neighboursNeighbourInCorrectGroup.isPresent()) {
            ((SearchProtocol)neighboursNeighbourInCorrectGroup.get().getProtocol(protocolID))
                    .addSearchQuery(searchQuery);
            return true;
        }
        return false;
    }

    private boolean sendQueryToNeighbourInGroupInVisitedList(int protocolID, Collection<Node> neighbours,
            GroupName groupName, SearchQuery searchQuery) {
        Optional<Node> nodeInBacktrackGroup = neighbours.stream()
                .filter(n -> getGroupNameProtocolFrom(n).compareWithGroupName(groupName)).findAny();
        if (nodeInBacktrackGroup.isPresent()) {
            searchQuery.getVisitedGroups().remove(groupName);
            getSearchProtocolFrom(protocolID, nodeInBacktrackGroup.get())
                    .addSearchQuery(searchQuery);
        }
        return true;
    }

    private boolean sendQueryToNeighborIfItIsInVisitedList(int protocolID, Node representative, SearchQuery searchQuery) {
        Collection<Node> neighbours = getInterestProtocolFrom(representative).getNeighbours();
        for (Node n : neighbours) {
            if (searchQuery.getVisitedNodes().contains(n)) {
                int indexOf = searchQuery.getVisitedNodes().indexOf(n);
                Node nextHop = searchQuery.getVisitedNodes().get(indexOf);
                searchQuery.getVisitedNodes().subList(0, indexOf).clear();
                getSearchProtocolFrom(protocolID, nextHop).addSearchQuery(searchQuery);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the entry of this node and this nodes group from the search query
     * and moves it on.
     * 
     * @param node
     * @param protocolID
     * @param lastNode
     * @param searchQuery 
     */
    private void sendQueryToLastNode(Node node, int protocolID, Node lastNode, SearchQuery searchQuery) {
        searchQuery.getVisitedNodes().remove(lastNode);
        Set<GroupName> visitedGroups = searchQuery.getVisitedGroups();
        visitedGroups.remove(getGroupNameProtocolFrom(node).getGroupName());
        getSearchProtocolFrom(protocolID, lastNode).addSearchQuery(searchQuery);
    }

    /**
     * @see kr.ac.kaist.ds.groupd.search.SearchProtocol#setSearchQuery(kr.ac.kaist.ds.groupd.search.SearchQuery)
     */
    public void addSearchQuery(SearchQuery q) {
        searchQueries.add(q);
    }

    @Override
    public Object clone() {
        try {
            SearchProtocolImpl clone = (SearchProtocolImpl)super.clone();
            clone.searchQueries = new ArrayList<>();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
        for (SearchQuery searchQuery : new ArrayList<>(searchQueries)) {
            if(searchQuery.hasBeenMovedThisRound()){
                continue;
            }
            searchQueries.remove(searchQuery);
            searchQuery.movedThisRound();
            if(isTimeOver(searchQuery)){
                continue;
            }
            if (searchQuery.isBackward()) {
                performBacktracking(node, protocolID, searchQuery);
            } else {
                performSearch(node, protocolID, searchQuery);
            }
        }
    }

    private boolean isTimeOver(SearchQuery searchQuery) {
        return searchQuery.getCreationRound() + quertyTtl <= CommonState.getIntTime();
    }

}
