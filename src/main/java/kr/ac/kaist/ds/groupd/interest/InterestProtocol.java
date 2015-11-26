
package kr.ac.kaist.ds.groupd.interest;

import java.util.Set;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.search.SearchQuery;
import peersim.cdsim.CDProtocol;
import peersim.core.Linkable;
import peersim.core.Node;

public interface InterestProtocol extends CDProtocol, Linkable {

    /**
     * Sets a flag that in the next round an election will be held by this node
     * and its neighbours.
     */
    void startElection();

    /**
     * Return the list of neighbours this node is connected to, based on similar
     * interests.
     */
    Set<Node> getNeighbours();

    /**
     * Returns the representative of this node. It could be the node itself if
     * it is the representative.
     */
    Node getRepresentative();

    /**
     * Sets the new interest community of this node.
     */
    void setNeighbours(Set<Node> neighbours);

    /**
     * Removes a neighbour to the interest community of this node.
     * @return true if the node was removed successfully
     */
    boolean removeNeighbour(Node toRemove);

    void resetVotes();

}