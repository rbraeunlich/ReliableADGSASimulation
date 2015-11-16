
package kr.ac.kaist.ds.groupd.information;

//camel Case

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node representationNode;

    private ArrayList<Node> alMemberNodes;

    private int nIndex;

    public GroupInformation(int index) {
        representationNode = null;
        alMemberNodes = new ArrayList<Node>();
        nIndex = index;
    }

    public void setRepresentationInGroupRandom() {
        if (alMemberNodes.size() < 1)
            return;

        representationNode = alMemberNodes.remove(new Random().nextInt(alMemberNodes.size()));

    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setnRepresentationNode(Node nRepresentationNode) {
        this.representationNode = nRepresentationNode;
    }

    public void setnNeighborNodes(ArrayList<Node> nNeighborNodes) {
        this.alMemberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node nNode) {
        this.alMemberNodes.add(nNode);
    }

    // getter, minner
    public Node getnRepresentationNode() {
        return representationNode;
    }

    public ArrayList<Node> getnNeighborNodes() {
        return alMemberNodes;
    }

    public Node getNode(int id) {
        for (int i = 0; i < alMemberNodes.size(); i++) {
            if (alMemberNodes.get(i).getID() == id) {
                return alMemberNodes.remove(i);
            }
        }

        if (representationNode.getID() == id) {
            Node temp = representationNode;
            setRepresentationInGroupRandom();
            return temp;
        }

        return null;
    }

    public String toString() {
        return "Node_number :" + alMemberNodes;
    }

    public int getIndex() {
        return nIndex;
    }
}
