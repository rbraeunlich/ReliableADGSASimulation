
package kr.ac.kaist.ds.groupd.imformation;

//camel Case

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node nRepresentationNode;

    private ArrayList<Node> alMemberNodes;

    private int nIndex;

    public GroupInformation(int index) {
        nRepresentationNode = null;
        alMemberNodes = new ArrayList<Node>();
        nIndex = index;
    }

    public void setRepresentationInGroupRandom() {
        if (alMemberNodes.size() < 1)
            return;

        nRepresentationNode = alMemberNodes.remove(new Random().nextInt(alMemberNodes.size()));

    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setnRepresentationNode(Node nRepresentationNode) {
        this.nRepresentationNode = nRepresentationNode;
    }

    public void setnNeighborNodes(ArrayList<Node> nNeighborNodes) {
        this.alMemberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node nNode) {
        this.alMemberNodes.add(nNode);
    }

    // getter, minner
    public Node getnRepresentationNode() {
        return nRepresentationNode;
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

        if (nRepresentationNode.getID() == id) {
            Node temp = nRepresentationNode;
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
