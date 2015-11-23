package kr.ac.kaist.ds.groupd.search;

import java.util.List;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import peersim.core.Node;

public class SearchQuery {

    // start node
	private int source;

	// destination node
	private int destination;

	// backstep
	private boolean backward;

	// visited nodes
	private List<Node> visitedNodes;

	// visited groups
	private List<GroupName<?>> visitedGroups;

    public List<Node> getVisitedNodes() {
        return visitedNodes;
    }

    public void setVisitedNodes(List<Node> visitedNodes) {
        this.visitedNodes = visitedNodes;
    }

    public List<GroupName<?>> getVisitedGroups() {
        return visitedGroups;
    }

    public void setVisitedGroups(List<GroupName<?>> visitedGroups) {
        this.visitedGroups = visitedGroups;
    }
	
	

}
