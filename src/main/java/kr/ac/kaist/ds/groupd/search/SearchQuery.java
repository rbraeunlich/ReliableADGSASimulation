package kr.ac.kaist.ds.groupd.search;

import java.util.List;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import peersim.core.Node;

public class SearchQuery {

	private int source;

	private int destination;

	private boolean backward;

	private List<Node> visitedNodes;

	private List<GroupName<?>> visitedGroups;

}
