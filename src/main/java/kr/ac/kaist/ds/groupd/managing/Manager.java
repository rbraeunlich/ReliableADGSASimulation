package kr.ac.kaist.ds.groupd.managing;

import peersim.core.Control;

public class Manager implements Control {

	private static long PAR_TIME_RANGE_START_MS;

	private static long PAR_TIME_RANGE_END_MS;

	private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

	/**
	 * Performs a change in the network, which means a node will change its location
	 * and connect with new nodes. This will cause a new election to take place.
	 */
	private void changeNetwork() {

	}


	/**
	 * @see peersim.core.Control#execute()
	 */
	public boolean execute() {
		return false;
	}

}
