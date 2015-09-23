package kr.ac.kaist.ds.groupd.example.token;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.core.Protocol;
import peersim.transport.Transport;
import peersim.vector.SingleValueHolder;

public class TokenProtocol extends SingleValueHolder implements CDProtocol {

	private boolean token;

	public TokenProtocol(String name) {
		super(name);
	}

	public boolean hasToken() {
		return token;
	}

	public void resetToken() {
		this.token = false;
	}

	@Override
	public void nextCycle(Node node, int pid) {
		if (token = true) {
			Linkable linkable = (Linkable) node.getProtocol(FastConfig
					.getLinkable(pid));
			int degree = linkable.degree();
			// get a random neighbour
			Node neighbor = linkable.getNeighbor(CommonState.r.nextInt(degree));
			TokenProtocol protocol = (TokenProtocol) neighbor.getProtocol(pid);
			protocol.setToken(true);
			resetToken();
		}

	}

	public void setToken(boolean b) {
		this.token = true;
	}
}
