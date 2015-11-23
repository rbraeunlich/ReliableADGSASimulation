
package kr.ac.kaist.ds.groupd.search.impl;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;
import kr.ac.kaist.ds.groupd.search.SearchProtocol;
import kr.ac.kaist.ds.groupd.search.SearchQuery;
import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;

public class SearchProtocolImpl implements SearchProtocol {

    private static final String PAR_NAME_PROTOCOL = "naming";

    private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

    private static final String PAR_LINKABLE_PROTOCOL = "link";

    private SearchQuery searchQuery;

    private SearchQuery receiveQuery;

    private int namingProtocolPid;

    private int interestGroupProtocolPid;

    private int linkableProtocolPid;

    private float probabilityPk;

    public SearchProtocolImpl(String prefix) {
        this.namingProtocolPid = Configuration.getPid(prefix + "." + PAR_NAME_PROTOCOL);
        this.interestGroupProtocolPid = Configuration.getPid(prefix + "."
                + PAR_INTEREST_GROUP_PROTOCOL);
        this.linkableProtocolPid = Configuration.getPid(prefix + "." + PAR_LINKABLE_PROTOCOL);
        this.probabilityPk = 0.6f;
    }

    /**
     * Performs the forward search based on the ADGSA algorithm.
     */
    private void performSearch(Node node, int pid) {
        // target reached
        if (searchQuery.getDestination() == node.getID()) {
            Logger.getLogger(this.getClass().getName()).info("Target reached");
        }
        sendQueryToNeighboursWithProbability(node, pid);
        Linkable linkable = (Linkable)node.getProtocol(linkableProtocolPid);
        Node neighbourWithHighestDegree = findNeighbourWithHighestDegree(linkable);

        SearchProtocol searchProtocol = (SearchProtocol)neighbourWithHighestDegree.getProtocol(pid);
        searchProtocol.setSearchQuery(searchQuery);

        checkResourceLocated(searchProtocol);

    }

    private void checkResourceLocated(SearchProtocol searchProtocol) {
        // using similarfunction;
    }

    private Node findNeighbourWithHighestDegree(Linkable linkable) {
        Node neighbourWithHighestDegree = linkable.getNeighbor(0);
        for (int i = 1; i < linkable.degree(); i++) {
            Node n = linkable.getNeighbor(i);
            if (((Linkable)n.getProtocol(linkableProtocolPid)).degree() > ((Linkable)neighbourWithHighestDegree
                    .getProtocol(linkableProtocolPid)).degree()) {
                neighbourWithHighestDegree = n;
            }
        }
        return neighbourWithHighestDegree;
    }

    private void sendQueryToNeighboursWithProbability(Node node, int pid) {
        Linkable linkable = (Linkable)node.getProtocol(linkableProtocolPid);
        for (int i = 0; i < linkable.degree(); i++) {
            if (probabilityPk < new Random().nextFloat()) {
                SearchProtocol searchProtocol = (SearchProtocol)linkable.getNeighbor(i)
                        .getProtocol(pid);
                searchProtocol.receiveSearchQuery(searchQuery);
            }
        }
    }

    /**
     * Performs the backtracking, based on our group naming scheme. backTracking
     * follow previous steps
     * 
     * @param protocolID
     * @param node
     */
    private void performBacktracking(Node node, int protocolID) {

    }

    /**
     * @see kr.ac.kaist.ds.groupd.search.SearchProtocol#setSearchQuery(kr.ac.kaist.ds.groupd.search.SearchQuery)
     */
    @Override
    public void receiveSearchQuery(SearchQuery q) {
        receiveQuery = q;
    }

    /**
     * @see kr.ac.kaist.ds.groupd.search.SearchProtocol#setSearchQuery(kr.ac.kaist.ds.groupd.search.SearchQuery)
     */
    public void setSearchQuery(SearchQuery q) {
        searchQuery = q;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
        if (searchQuery != null) {
            if (searchQuery.isBackward()) {
                performBacktracking(node, protocolID);
            } else {
                performSearch(node, protocolID);
            }
        }
    }

}
