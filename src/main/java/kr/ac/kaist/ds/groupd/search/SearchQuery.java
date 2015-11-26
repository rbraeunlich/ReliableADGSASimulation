package kr.ac.kaist.ds.groupd.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import peersim.core.Node;

public class SearchQuery {

    // start node
	private final int source;

	// destination node
	private final int destination;

	// backstep
	private boolean backward;

	private List<Node> visitedNodes;

	private Set<GroupName<?>> visitedGroups;
	
	/**
	 * Nodes that did not get a message due to the probability will be marked as "orange".
	 * If the searched for node lies not on the direct, highest-degree path and we have to 
	 * start performing backtracking, we gotta visit those orange nodes again, so we can be 
	 * sure that we visit all nodes.
	 */
	private Map<Long, Collection<Node>> orangeNodes;
		
	/**
	 * Copy constructor
	 * @param q
	 */
	public SearchQuery(SearchQuery q) {
	    this.source = q.source;
	    this.destination = q.destination;
	    this.backward = q.backward;
	    this.visitedNodes = new ArrayList<Node>(q.visitedNodes);
	    this.visitedGroups = new LinkedHashSet<>(q.visitedGroups);
	    this.orangeNodes = new HashMap<Long, Collection<Node>>(q.orangeNodes);
	}

    public SearchQuery(int source, int destination) {
        this.source = source;
        this.destination = destination;
        this.backward = false;
        this.visitedNodes = new ArrayList<Node>();
        this.visitedGroups = new LinkedHashSet<>();
        this.orangeNodes = new HashMap<Long, Collection<Node>>();
    }

    public List<Node> getVisitedNodes() {
        return visitedNodes;
    }

    public void setVisitedNodes(List<Node> visitedNodes) {
        this.visitedNodes = visitedNodes;
    }

    public Set<GroupName<?>> getVisitedGroups() {
        return visitedGroups;
    }

    public void setVisitedGroups(Set<GroupName<?>> visitedGroups) {
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
    
	
}
