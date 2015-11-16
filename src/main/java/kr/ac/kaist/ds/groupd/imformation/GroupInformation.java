
package kr.ac.kaist.ds.groupd.imformation;

//Ä«¸á Ç¥±â¹ý

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

class GroupInformation {

    private Node nRepresentationNode;

    private ArrayList<Node> aLNeighborNodes;

    public void setRepresentationInGroupRandom() {
        if (aLNeighborNodes.size() < 1)
            return;

        if (aLNeighborNodes.remove(nRepresentationNode)) {
            nRepresentationNode = (Node)aLNeighborNodes.get(
                    new Random().nextInt(aLNeighborNodes.size())).clone();
            aLNeighborNodes.remove(nRepresentationNode);
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
        aLNeighborNodes = nNeighborNodes;
    }

    public void addNeighborNode(Node _nNode) {
        aLNeighborNodes.add(_nNode);
    }

    // getter, minner
    public Node getnRepresentationNode() {
        return nRepresentationNode;
    }

    public ArrayList<Node> getnNeighborNodes() {
        return aLNeighborNodes;
    }

    public Node getNode(int id) {
        for (int i = 0; i < aLNeighborNodes.size(); i++) {
            if (aLNeighborNodes.get(i).getID() == id) {
                Node node = (Node)aLNeighborNodes.get(i).clone();
                aLNeighborNodes.remove(i);
                return node;
            }
        }

        return null;
    }
}
