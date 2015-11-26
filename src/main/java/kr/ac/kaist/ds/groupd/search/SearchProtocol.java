package kr.ac.kaist.ds.groupd.search;

import peersim.cdsim.CDProtocol;

public interface SearchProtocol extends CDProtocol {

	/**
	 * Sets the search query at this node. This will result in the node performing
	 * the search algorithm the next time it executes.
	 * @param q the search query
	 */
	void setSearchQuery(SearchQuery q);

	SearchQuery getSearchQuery();

}
