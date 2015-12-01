package kr.ac.kaist.ds.groupd.search;

import peersim.cdsim.CDProtocol;

public interface SearchProtocol extends CDProtocol {

    /**
     * Adds the search query to this node's queue. This will result in the node performing
     * the search algorithm the next time it executes.
     * @param q the search query
     */
    void addSearchQuery(SearchQuery searchQuery);

    boolean isQueueFull();

}
