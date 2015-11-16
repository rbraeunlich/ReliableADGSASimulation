
package kr.ac.kaist.ds.groupd.imformation;

//camel Case

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node nRepresentationNode;

    private ArrayList<Node> aLMemberNodes;

    public void setRepresentationInGroupRandom() {
        if (aLMemberNodes.size() < 1)
            return;

        if (aLMemberNodes.remove(nRepresentationNode)) {
            nRepresentationNode = (Node)aLMemberNodes.get(
                    new Random().nextInt(aLMemberNodes.size())).clone();
            aLMemberNodes.remove(nRepresentationNode);
            return;
        } else
            return;
    }

    public void setRepresentationInGroupCompare() {
        return;
    }

    // setter, adder
    public void setnRepresentationNode(Node nRepresentationNode) {
        this.nRepresentationNode = nRepresentationNode;
    }

    public void setnNeighborNodes(ArrayList<Node> nNeighborNodes) {
        aLMemberNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node _nNode) {
        aLMemberNodes.add(_nNode);
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
                Node node = (Node)aLMemberNodes.get(i).clone();
                aLMemberNodes.remove(i);
                return node;
            }
        }

        return null;
    }
}
