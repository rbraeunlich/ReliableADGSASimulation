
package kr.ac.kaist.ds.groupd.search.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import peersim.core.Node;

public class SearchProtocolImpl implements SearchProtocol {

    private static final String PAR_NAME_PROTOCOL = "naming";

    private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

    private static final String PAR_LINKABLE_PROTOCOL = "link";

    private SearchQuery searchQuery;

    private int namingProtocolPid;

    private int interestGroupProtocolPid;

    private int linkableProtocolPid;

    private float probabilityPk;

    public SearchProtocolImpl(String prefix) {
        this.namingProtocolPid = Configuration.getPid(prefix + "." + PAR_NAME_PROTOCOL);
        this.interestGroupProtocolPid = Configuration.getPid(prefix + "."
                + PAR_INTEREST_GROUP_PROTOCOL);
        this.linkableProtocolPid = Configuration.getPid(prefix + "." + PAR_LINKABLE_PROTOCOL);
        this.probabilityPk = 0.6f;
    }

    /**
     * Performs the forward search based on the ADGSA algorithm.
     */
    private void performSearch(Node node, int pid) {
        // target reached return the start source.
        if (node.getID() == searchQuery.getDestination()) {
            searchQuery.setBackward(true);
            Logger.getLogger(getClass().getName()).info("Destination found");
            return;
        }
        if (node.getID() == searchQuery.getSource() && !searchQuery.getVisitedNodes().isEmpty()) {
            Logger.getLogger(getClass().getName()).info("Back at source, search unsuccessful.");
            searchQuery = null;
            return;
        }
        InterestProtocol interestProtocol = (InterestProtocol)node
                .getProtocol(interestGroupProtocolPid);
        Set<Node> neighbours = interestProtocol.getNeighbours();

        // while Pm is not the last peer in the search path do
        if (!neighbours.isEmpty() || !hasSecondDegreeNeighbourAlreadyBeenVisited(node)) {
            // Send Mq to the neighbrs in the set S = { s1..}
            List<Node> orangeNodes = sendQueryToNeighboursWithProbability(node, pid);

            // line 5 - 9
            Node neighbourWithHighestDegree = findNeighbourWithHighestDegree(node, pid);
            SearchProtocol searchProtocol = (SearchProtocol)neighbourWithHighestDegree
                    .getProtocol(pid);
            SearchQuery query = copySearchQuery(node, pid, orangeNodes);
            searchProtocol.setSearchQuery(query);
        } else {
            Node neighbourWithSecondHighestDegree = findNeighbourWithSecondHighestDegree(node, pid);
            if (hasSecondDegreeNeighbourAlreadyBeenVisited(neighbourWithSecondHighestDegree)) {
                sendQueryToPredecessor(node, pid);
            } else {
                sendQueryToSecondDegreeNeighbour(node, pid, neighbourWithSecondHighestDegree);
            }
        }
        this.searchQuery = null;
    }

    private boolean hasSecondDegreeNeighbourAlreadyBeenVisited(Node neighbourWithSecondHighestDegree) {
        return searchQuery.getVisitedNodes().contains(neighbourWithSecondHighestDegree);
    }

    private void sendQueryToPredecessor(Node node, int pid) {
        int indexOf = searchQuery.getVisitedNodes().indexOf(node);
        Node predecessor = searchQuery.getVisitedNodes().get(indexOf - 1);
        SearchProtocol searchProtocolPredecessor = (SearchProtocol)predecessor.getProtocol(pid);
        searchProtocolPredecessor.setSearchQuery(searchQuery);
    }

    private void sendQueryToSecondDegreeNeighbour(Node node, int pid,
            Node neighbourWithSecondHighestDegree) {
        SearchProtocol searchProtocol = (SearchProtocol)neighbourWithSecondHighestDegree
                .getProtocol(pid);
        SearchQuery query = copySearchQuery(node, pid, Collections.emptyList());
        searchProtocol.setSearchQuery(query);
    }

    private Node findNeighbourWithHighestDegree(Node node, int pid) {
        InterestProtocol interestProtocol = (InterestProtocol)node
                .getProtocol(interestGroupProtocolPid);
        Map<Node, Integer> neighboursWithDegree = interestProtocol
                .getNeighbours()
                .stream()
                .collect(
                        Collectors.toMap(n -> n, n -> ((InterestProtocol)n
                                .getProtocol(interestGroupProtocolPid)).getNeighbours().size()));
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
        Map<Node, Integer> neighboursWithDegree = interestProtocol
                .getNeighbours()
                .stream()
                .collect(
                        Collectors.toMap(n -> n, n -> ((InterestProtocol)n.getProtocol(pid))
                                .getNeighbours().size()));
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
     * @return
     */
    private List<Node> sendQueryToNeighboursWithProbability(Node node, int pid) {
        InterestProtocol interstProtocol = (InterestProtocol)node
                .getProtocol(interestGroupProtocolPid);
        Random random = new Random();
        List<Node> nodesToReceiveSearchQuery = interstProtocol.getNeighbours().stream()
                .filter(n -> probabilityPk < random.nextFloat()).collect(Collectors.toList());

        ArrayList<Node> nodesToLabelOrange = new ArrayList<>(interstProtocol.getNeighbours());
        nodesToLabelOrange.removeAll(nodesToReceiveSearchQuery);
        SearchQuery prototypeQuery = copySearchQuery(node, pid, nodesToLabelOrange);

        nodesToReceiveSearchQuery.forEach(n -> {
            SearchProtocol searchProtocol = (SearchProtocol)n.getProtocol(pid);
            searchProtocol.setSearchQuery(new SearchQuery(prototypeQuery));
        });
        return nodesToLabelOrange;
    }

    private SearchQuery copySearchQuery(Node node, int pid, List<Node> nodesToLabelOrange) {
        SearchQuery q = new SearchQuery(searchQuery);
        q.addVisitedNode(node);
        q.addVisitedGroup(((GroupNameProtocol)node.getProtocol(namingProtocolPid)).getGroupName());
        q.addOrangeNodes(node.getID(), nodesToLabelOrange);
        return q;
    }

    /*
     * private void findNeighbourstoOriginalPath(Node node, int pid) { Linkable
     * linkable = (Linkable) node.getProtocol(linkableProtocolPid); for (int i =
     * 0; i < linkable.degree(); i++) { for (int j = 0; j <
     * searchQuery.getVisitedNodes().size(); j++) { if
     * (searchQuery.getVisitedNodes().get(j).getID() == linkable
     * .getNeighbor(i).getID()) { SearchProtocol searchProtocol =
     * (SearchProtocol) linkable .getNeighbor(i).getProtocol(pid);
     * searchProtocol.getSearchQuery().setBackward(true); } else { } } } }
     */

    /**
     * Performs the backtracking, based on our group naming scheme. backTracking
     * follow previous steps
     * 
     * @param protocolID
     * @param node
     */
    private void performBacktracking(Node node, int protocolID) {
        if(node.getID() == searchQuery.getSource()){
            this.searchQuery = null;
            Logger.getLogger(this.getClass().getName()).info("back home!");
        }
        Node lastNode = this.searchQuery.getVisitedNodes().get(
                searchQuery.getVisitedNodes().size() - 1);
        InterestProtocol interestProtcol = (InterestProtocol)node.getProtocol(linkableProtocolPid);
        if (interestProtcol.getNeighbours().contains(lastNode)) {
            sendQueryToLastNode(node, protocolID, lastNode);
            return;
        }
        Node representative = ((InterestProtocol)node.getProtocol(interestGroupProtocolPid))
                .getRepresentative();
        if (node.equals(representative)) {
            boolean sent = sendQueryToNeighborIfItIsInVisitedList(protocolID,
                    representative);
            if(sent){
                return;
            }
            Set<Node> neighbours = ((InterestProtocol)representative
                    .getProtocol(interestGroupProtocolPid)).getNeighbours();
            // no neighbour in the visited list found, gotta check the groups
            GroupName lastNodeGroupName = ((GroupNameProtocol)lastNode.getProtocol(namingProtocolPid))
                    .getGroupName();
            boolean sentToGroup = sendQueryToNeighbourInGroup(protocolID, neighbours, lastNodeGroupName);
            if(sentToGroup){
                return;
            }
            // no group visible from here, gotta check the groups of my neighbours neighbours
            for (Node n : neighbours) {
                Set<Node> neighboursNeighbours = ((InterestProtocol) n.getProtocol(interestGroupProtocolPid)).getNeighbours();
                for (Node nn : neighboursNeighbours) {
                    boolean sameGroup = ((GroupNameProtocol) nn.getProtocol(namingProtocolPid)).compareWithGroupName(lastNodeGroupName);
                    if(sameGroup){
                        ((SearchProtocol)nn.getProtocol(protocolID)).setSearchQuery(searchQuery);
                        this.searchQuery = null;
                        return;
                    }
                }
            }
            // worst case go back to gossiping until we are back on track
        } else {
            //send query to representative because we do not know where to send it
            ((SearchProtocol)representative.getProtocol(protocolID)).setSearchQuery(searchQuery);
        }

        // findNeighbourstoOriginalPath(node, protocolID);

    }

    private boolean sendQueryToNeighbourInGroup(int protocolID, Set<Node> neighbours,
            GroupName groupName) {
        for (Node n : neighbours) {
            boolean sameName = ((GroupNameProtocol)n.getProtocol(namingProtocolPid))
                    .compareWithGroupName(groupName);
            if (sameName) {
                searchQuery.getVisitedGroups().remove(groupName);
                ((SearchProtocol)n.getProtocol(protocolID)).setSearchQuery(searchQuery);
                this.searchQuery = null;
                return true;
            }
        }
        return false;
    }

    private boolean sendQueryToNeighborIfItIsInVisitedList(int protocolID, Node representative) {
        Set<Node> neighbours = ((InterestProtocol)representative
                .getProtocol(interestGroupProtocolPid)).getNeighbours();
        for (Node n : neighbours) {
            if (searchQuery.getVisitedNodes().contains(n)) {
                int indexOf = searchQuery.getVisitedNodes().indexOf(n);
                Node nextHop = searchQuery.getVisitedNodes().get(indexOf);
                searchQuery.getVisitedNodes().subList(0, indexOf).clear();
                ((SearchProtocol)nextHop.getProtocol(protocolID)).setSearchQuery(searchQuery);
                return true;
            }
        }
        return false;
    }

    private void sendQueryToLastNode(Node node, int protocolID, Node lastNode) {
        this.searchQuery.getVisitedNodes().remove(lastNode);
        Set<GroupName> visitedGroups = this.searchQuery.getVisitedGroups();
        visitedGroups.remove(((GroupNameProtocol)node.getProtocol(namingProtocolPid))
                .getGroupName());
        ((SearchProtocol)lastNode.getProtocol(protocolID)).setSearchQuery(searchQuery);
        this.searchQuery = null;
    }

    /**
     * @see kr.ac.kaist.ds.groupd.search.SearchProtocol#setSearchQuery(kr.ac.kaist.ds.groupd.search.SearchQuery)
     */
    public void setSearchQuery(SearchQuery q) {
        searchQuery = q;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
        if (searchQuery != null) {
            if (searchQuery.isBackward()) {
                performBacktracking(node, protocolID);
            } else {
                performSearch(node, protocolID);
            }
        }
    }

}
