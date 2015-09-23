package kr.ac.kaist.ds.groupd.example.token;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class TokenObserver implements Control {

	private static final String PAR_PROT = "protocol";

	private int pid;

	public TokenObserver(String name) {
		this.pid = Configuration.getPid(name + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {
		for (int i = 0; i < Network.size(); i++) {
			TokenProtocol protocol = (TokenProtocol) Network.get(i)
					.getProtocol(pid);
			if (protocol.hasToken()) {
				System.out.println("Token at Node number "
						+ Network.get(i).getID());
				protocol.resetToken();
			}
		}
		return false;
	}

}
