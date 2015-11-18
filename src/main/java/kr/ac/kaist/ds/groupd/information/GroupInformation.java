
package kr.ac.kaist.ds.groupd.information;

//camel Case

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node nRepresentationNode;

    private ArrayList<Node> MemberNodes;

    private int nIndex;
    
    public GroupInformation(int index) {
        nRepresentationNode = null;
        MemberNodes = new ArrayList<Node>();
        nIndex = index;
    }

    public void setRepresentationInGroupRandom() {
        if (MemberNodes.size() < 1)
            return;

        nRepresentationNode = MemberNodes.remove(new Random().nextInt(MemberNodes.size()));

    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setnRepresentationNode(Node nRepresentationNode) {
        this.nRepresentationNode = nRepresentationNode;
    }

    public void setnNeighborNodes(ArrayList<Node> nNeighborNodes) {
        this.MemberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node nNode) {
        this.MemberNodes.add(nNode);
    }

    // getter, minner
    public Node getnRepresentationNode() {
        return nRepresentationNode;
    }

    public ArrayList<Node> getnNeighborNodes() {
        return MemberNodes;
    }

    public Node getNode(int id) {
        for (int i = 0; i < MemberNodes.size(); i++) {
            if (MemberNodes.get(i).getID() == id) {
                return MemberNodes.remove(i);
            }
        }

        if (nRepresentationNode.getID() == id) {
            Node temp = nRepresentationNode;
            setRepresentationInGroupRandom();
            return temp;
        }

        return null;
    }
}
