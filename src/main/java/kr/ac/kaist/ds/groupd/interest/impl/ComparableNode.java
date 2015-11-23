package kr.ac.kaist.ds.groupd.interest.impl;

import peersim.core.Node;
import peersim.core.Protocol;

/**
 * Wraps a node with a comparable interface.
 */
public class ComparableNode implements Node, Comparable<ComparableNode>{

    private Node node;


    public ComparableNode(Node n){
        this.node = n;
    }
    
    public Protocol getProtocol(int i) {
        return node.getProtocol(i);
    }

    public int getFailState() {
        return node.getFailState();
    }

    public int protocolSize() {
        return node.protocolSize();
    }

    public void setIndex(int index) {
        node.setIndex(index);
    }

    public void setFailState(int failState) {
        node.setFailState(failState);
    }

    public boolean isUp() {
        return node.isUp();
    }

    public int getIndex() {
        return node.getIndex();
    }

    public long getID() {
        return node.getID();
    }

    public Object clone() {
        return node.clone();
    }

    @Override
    public int compareTo(ComparableNode o) {
        return Long.compare(this.node.getID(), o.node.getID());
    }
}
