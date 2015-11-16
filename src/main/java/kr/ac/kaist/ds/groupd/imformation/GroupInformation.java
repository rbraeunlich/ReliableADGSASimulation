
package kr.ac.kaist.ds.groupd.imformation;

//camel Case

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node nRepresentationNode;

    private ArrayList<Node> aLMemberNodes;

    public GroupInformation() {
        nRepresentationNode = null;
        aLMemberNodes = new ArrayList<Node>();
    }

    public void setRepresentationInGroupRandom() {
        if (aLMemberNodes.size() < 1)
            return;

        nRepresentationNode = aLMemberNodes.remove(new Random().nextInt(aLMemberNodes.size()));

    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setnRepresentationNode(Node nRepresentationNode) {
        this.nRepresentationNode = nRepresentationNode;
    }

    public void setnNeighborNodes(ArrayList<Node> nNeighborNodes) {
        this.aLMemberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node nNode) {
        this.aLMemberNodes.add(nNode);
    }

    // getter, minner
    public Node getnRepresentationNode() {
        return nRepresentationNode;
    }

    public ArrayList<Node> getnNeighborNodes() {
        return aLMemberNodes;
    }

    public Node getNode(int id) {
        for (int i = 0; i < aLMemberNodes.size(); i++) {
            if (aLMemberNodes.get(i).getID() == id) {
                return aLMemberNodes.remove(i);
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
