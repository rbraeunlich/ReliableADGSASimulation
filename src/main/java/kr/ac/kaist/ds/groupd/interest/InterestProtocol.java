package kr.ac.kaist.ds.groupd.interest;

import peersim.cdsim.CDProtocol;
import java.util.Collection;
import peersim.core.Node;

public interface InterestProtocol extends CDProtocol {

	/**
	 * Sets a flag that in the next round an election will be held by this node and its neighbours.
	 */
	void startElection();

	/**
	 * Return the list of neighbours this node is connected to, based on similar interests.
	 */
	Collection<Node> getNeighbors();

	/**
	 * Returns the representative of this node. It could be the node itself if it is the representative.
	 */
	Node getRepresentative();

}
