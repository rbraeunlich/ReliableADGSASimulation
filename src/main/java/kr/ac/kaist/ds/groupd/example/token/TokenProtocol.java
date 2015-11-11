package kr.ac.kaist.ds.groupd.example.token;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
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
			Node neighbor = linkable.getNeighbor(CommonState.r.nextInt(degree));  //randomal 하게 연결된 node로부터 token을 받아왔음 ㅇㅋ
			TokenProtocol protocol = (TokenProtocol) neighbor.getProtocol(pid);
			protocol.setToken(true);
			
			System.out.println(" pre: "+node.getID()+" now: "+neighbor.getID());
			
			resetToken();
		}

	}

	public void setToken(boolean b) {
		this.token = true;
	}
}
