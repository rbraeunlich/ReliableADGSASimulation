package kr.ac.kaist.ds.groupd.example.token;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;

public class TokenObserver implements Control {

	private static final String PAR_PROT = "protocol";

	private int pid;

	public TokenObserver(String name) {
		this.pid = Configuration.getPid(name + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {
		
		System.out.println("");
		for (int i = 0; i < Network.size(); i++) {
			Node node = (Node)Network.get(i);
			TokenProtocol protocol = (TokenProtocol) Network.get(i)
					.getProtocol(pid);
			if (protocol.hasToken()) {
				System.out.println("Token at Node number "
						+ Network.get(i).getID());
				protocol.resetToken();
			}
			protocol.clearQueue();
			Linkable linkable = (Linkable) node.getProtocol(FastConfig
		               .getLinkable(pid));
		         System.out.print("Neighbors of "+node.getID()+"are[");
		         for(int j=0; j<linkable.degree();j++)
		         {
		            System.out.print(linkable.getNeighbor(j).getID());
		         }
		         System.out.println("]\n");
		}
		
		return false;
	}

}
