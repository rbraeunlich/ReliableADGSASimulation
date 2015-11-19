
package kr.ac.kaist.ds.groupd.information;

/**
 * GroupInformation class manage the interested node's set. so the GroupInformation
 */
import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

public class GroupInformation {

    private Node representationNode;

    private ArrayList<Node> memberNodes;

    private int Index;
    
    public GroupInformation(int index) {
        representationNode = null;
        memberNodes = new ArrayList<Node>();
        Index = index;
    }

    /**
     * Set the Representation but it is setting using random. we will change this code.
     */
    public void setRepresentationInGroupRandom() {
        if (memberNodes.size() < 1)
            return;

        representationNode = memberNodes.remove(new Random().nextInt(memberNodes.size()));

    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setRepresentationNode(Node nrepresentationNode) {
        this.representationNode = nrepresentationNode;
    }

    public void setNeighborNodes(ArrayList<Node> nNeighborNodes) {
        this.memberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node nNode) {
        this.memberNodes.add(nNode);
    }

    public void setIndex(int nIndex) {
        this.Index = nIndex;
    }
    
    // getter, minner
    public int getIndex() {
        return Index;
    }
    
    public Node getRepresentationNode() {
        return representationNode;
    }

    public ArrayList<Node> getNeighborNodes() {
        return memberNodes;
    }

    /**
     * extractNode erect one node based on id in the group.
     * 
     * @param id
     * @return
     */
    public Node extractNode(int id) {
        for (int i = 0; i < memberNodes.size(); i++) {
            if (memberNodes.get(i).getID() == id) {
                return memberNodes.remove(i);
            }
        }

        if (representationNode.getID() == id) {
            Node temp = representationNode;
            setRepresentationInGroupRandom();
            return temp;
        }

        return null;
    }
    
    
}
