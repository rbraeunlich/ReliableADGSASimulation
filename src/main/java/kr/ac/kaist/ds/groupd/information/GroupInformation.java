
package kr.ac.kaist.ds.groupd.information;

//camel Case

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node nRepresentationNode;

    private ArrayList<Node> memberNodes;

    private int nIndex;
    
    public GroupInformation(int index) {
        nRepresentationNode = null;
        memberNodes = new ArrayList<Node>();
        nIndex = index;
    }

    public void setRepresentationInGroupRandom() {
        if (memberNodes.size() < 1)
            return;

        nRepresentationNode = memberNodes.remove(new Random().nextInt(memberNodes.size()));

    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setnRepresentationNode(Node nRepresentationNode) {
        this.nRepresentationNode = nRepresentationNode;
    }

    public void setnNeighborNodes(ArrayList<Node> nNeighborNodes) {
        this.memberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node nNode) {
        this.memberNodes.add(nNode);
    }

    // getter, minner
    public Node getnRepresentationNode() {
        return nRepresentationNode;
    }

    public ArrayList<Node> getNeighborNodes() {
        return memberNodes;
    }

    public Node getNode(int id) {
        for (int i = 0; i < memberNodes.size(); i++) {
            if (memberNodes.get(i).getID() == id) {
                return memberNodes.remove(i);
            }
        }

        if (nRepresentationNode.getID() == id) {
            Node temp = nRepresentationNode;
            setRepresentationInGroupRandom();
            return temp;
        }

        return null;
    }

    public int getnIndex() {
        return nIndex;
    }

    public void setnIndex(int nIndex) {
        this.nIndex = nIndex;
    }
    
    
}
