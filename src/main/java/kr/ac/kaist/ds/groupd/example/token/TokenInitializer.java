package kr.ac.kaist.ds.groupd.example.token;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class TokenInitializer implements Control {

	private static final String PAR_NUMBER = "number";

	private static final String PAR_PROT = "protocol";

	private final int number;

	private final int pid;

	public TokenInitializer(String prefix) {
		number = Configuration.getInt(prefix + PAR_NUMBER, 1);
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {
		int size = Network.size();
		for (int i = 0; i < number; i++) {
			Node node = Network.get(CommonState.r.nextInt(size));
			TokenProtocol protocol = (TokenProtocol) node.getProtocol(pid);
			protocol.setToken(true);
		}
		return false;
	}

}
