package kr.ac.kaist.ds.groupd.topology;

import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

/**
 * It is necessary to form a community up front.
 */
public class VotingInitializer implements Control {

	private static final String PAR_PROT = "protocol";

	private int pid;

	public VotingInitializer(String prefix) {
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {
		for (int i = 0; i < Network.size(); i++) {
			Node node = Network.get(i);
			InterestProtocol protocol = (InterestProtocol) node
					.getProtocol(pid);
			protocol.startCommunityFormation(node, pid);
		}
		//we gotta create the community first and then set the group names or
		//else some nodes end up without name
		for (int i = 0; i < Network.size(); i++) {
			Node node = Network.get(i);
			InterestProtocol protocol = (InterestProtocol) node
					.getProtocol(pid);
			protocol.performGroupNameSetting(node, pid);
		}
		return false;
	}

}
