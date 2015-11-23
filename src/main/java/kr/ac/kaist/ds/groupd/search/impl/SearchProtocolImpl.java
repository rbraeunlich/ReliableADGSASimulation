
package kr.ac.kaist.ds.groupd.search.impl;

import java.util.ArrayList;

import kr.ac.kaist.ds.groupd.search.SearchProtocol;
import kr.ac.kaist.ds.groupd.search.SearchQuery;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;
import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.example.token.TokenProtocol;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;

public class SearchProtocolImpl implements SearchProtocol {

    private static final String PAR_NAME_PROTOCOL = "naming";

    private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

    private InterestProtocol interestProtocol;

    private GroupNameProtocol groupNameProtocol;

    private SearchQuery searchQuery;

    static ArrayList<Long> checkForPeers;

    private ArrayList<Node> celculateForDegrees;

    public SearchProtocolImpl(String name) {
        searchQuery = new SearchQuery();
    }

    /**
     * Performs the forward search based on the ADGSA algorithm.
     */
    private void performSearch(Node node, int pid) {

        long appropriateDegreePeer;

        // 1~2
        if (searchQuery.getVisitedNodes().contains(node))
            return;
        else
            searchQuery.getVisitedNodes().add(node);

        // 3~4 token will be change searchquery.
        Linkable linkable = (Linkable)node.getProtocol(FastConfig.getLinkable(pid));

        for (int i = 0; i < linkable.degree(); i++) {
            celculateForDegrees.add(linkable.getNeighbor(i));
        }

        /*
         * verify that already came out path(node)
         */
        for (int i = 0; i < celculateForDegrees.size(); i++) {
            for (int j = 0; j < checkForPeers.size(); j++) {
                if (celculateForDegrees.get(i).getID() == checkForPeers.get(j)) {
                    celculateForDegrees.remove(i);
                    continue;
                }
            }
        }

        // 5~9
        appropriateDegreePeer = degree(celculateForDegrees, pid);

        // 10 ~ 11

    }

    // implement after
    boolean checkSuccessfulllySendingMessage() {
        return false;

    }

    /**
     * Return biggest Degree Number Id;
     * 
     * @param S
     * @return
     */
    private long degree(ArrayList<Node> S, int pid) {
        long result = -1;

        for (int i = 0; i < S.size(); i++) {
            Linkable linkable = (Linkable)S.get(i).getProtocol(FastConfig.getLinkable(pid));
            result = ((linkable.degree() > result) ? linkable.degree() : result);
        }
        return result;
    }

    /**
     * Performs the backtracking, based on our group naming scheme. backTracking
     * follow previous steps
     */
    private void performBacktracking() {

    }

    /**
     * @see kr.ac.kaist.ds.groupd.search.SearchProtocol#setSearchQuery(kr.ac.kaist.ds.groupd.search.SearchQuery)
     */
    public void setSearchQuery(SearchQuery q) {

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
        performSearch(node, protocolID);

    }

}
