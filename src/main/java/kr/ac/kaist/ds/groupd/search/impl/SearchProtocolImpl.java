package kr.ac.kaist.ds.groupd.search.impl;

import kr.ac.kaist.ds.groupd.search.SearchProtocol;
import kr.ac.kaist.ds.groupd.search.SearchQuery;
import peersim.core.Node;
import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;

public class SearchProtocolImpl implements SearchProtocol {

	private static final String PAR_NAME_PROTOCOL = "naming";

	private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

	private InterestProtocol interestProtocol;

	private GroupNameProtocol groupNameProtocol;

	private SearchQuery searchQuery;

	/**
	 * Performs the forward search based on the ADGSA algorithm.
	 */
	private void performSearch() {

	}
	/**
	 * Performs the backtracking, based on our group naming scheme.
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
		// TODO Auto-generated method stub
		
	}

}
