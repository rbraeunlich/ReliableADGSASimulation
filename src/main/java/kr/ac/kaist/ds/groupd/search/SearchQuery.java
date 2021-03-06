
package kr.ac.kaist.ds.groupd.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.statistics.StatisticsCollector;
import peersim.core.CommonState;
import peersim.core.Node;

public class SearchQuery {

    // start node
    private final int source;

    // destination node
    private int destination;

    /**
     * The round in which the search message was created
     */
    private final int creationRound;

    // backstep
    private boolean backward;

    private List<Node> visitedNodes;

    private Set<GroupName> visitedGroups;

    /**
     * Nodes that did not get a message due to the probability will be marked as
     * "orange". If the searched for node lies not on the direct, highest-degree
     * path and we have to start performing backtracking, we gotta visit those
     * orange nodes again, so we can be sure that we visit all nodes.
     */
    private Map<Long, Collection<Node>> orangeNodes;

    private int movedInRound = -1;

    private int backTrackHops;
    
    /**
     * If on its way this query is off-track, we have to gossip it.
     */
    private boolean gossiping = false;

    private int id;

    /**
     * Copy constructor
     * 
     * @param q
     */
    public SearchQuery(SearchQuery q) {
        this.source = q.source;
        this.destination = q.destination;
        this.backward = q.backward;
        this.visitedNodes = new ArrayList<Node>(q.visitedNodes);
        this.visitedGroups = new LinkedHashSet<>(q.visitedGroups);
        this.orangeNodes = new HashMap<Long, Collection<Node>>(q.orangeNodes);
        this.creationRound = q.creationRound;
        // because of the cloning we will just assume, that it will be moved immediately
        this.movedInRound = CommonState.getIntTime();
        this.gossiping = q.gossiping;
        this.id = q.id;
        this.backTrackHops = q.backTrackHops;
        StatisticsCollector.queryCreated(q.id);
    }

    public SearchQuery(int source, int destination, int creationRound, int messageId) {
        this.source = source;
        this.destination = destination;
        this.backward = false;
        this.visitedNodes = new ArrayList<Node>();
        this.visitedGroups = new LinkedHashSet<>();
        this.orangeNodes = new HashMap<Long, Collection<Node>>();
        this.creationRound = creationRound;
        this.id = messageId;
        StatisticsCollector.queryCreated(messageId);
    }

    public List<Node> getVisitedNodes() {
        return visitedNodes;
    }

    public void setVisitedNodes(List<Node> visitedNodes) {
        this.visitedNodes = visitedNodes;
    }

    public Set<GroupName> getVisitedGroups() {
        return visitedGroups;
    }

    public void setVisitedGroups(Set<GroupName> visitedGroups) {
        this.visitedGroups = visitedGroups;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public boolean isBackward() {
        return backward;
    }

    public void setBackward(boolean backward) {
        this.backward = backward;
    }

    public void addVisitedNode(Node node) {
        this.visitedNodes.add(node);
    }

    public void addVisitedGroup(GroupName groupName) {
        this.visitedGroups.add(groupName);
    }

    public void addOrangeNodes(long id, List<Node> nodesToLabelOrange) {
        orangeNodes.put(id, nodesToLabelOrange);
    }

    public Map<Long, Collection<Node>> getOrangeNodes() {
        return orangeNodes;
    }

    public int getCreationRound() {
        return creationRound;
    }

    /**
     * Because of the cycle driven system we shall only move a message once each
     * round, or we will never be able to make use of the TTL and the whole
     * network will be congested after one or two rounds.
     */
    public void movedThisRound() {
        this.movedInRound = CommonState.getIntTime();
    }
    
    public boolean hasBeenMovedThisRound(){
        return movedInRound == CommonState.getIntTime();
    }

    /**
     * For the statistics.
     */
    public void addBacktrackHop() {
        this.backTrackHops++;
    }
    
    public int getBacktrackHops(){
        return backTrackHops;
    }
    
    public boolean isGossiping(){
    	return gossiping;
    }
    
    public void setGossiping(boolean b){
    	this.gossiping = b;
    }

    public int getId() {
        return id;
    }

    /**
     * Sets the destination. Is needed for the ADGSA backtracking.
     */
	public void setDestination(int newDestination) {
		this.destination = newDestination;
	}

}
