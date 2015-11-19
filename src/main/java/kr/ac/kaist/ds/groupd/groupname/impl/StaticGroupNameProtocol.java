package kr.ac.kaist.ds.groupd.groupname.impl;

import java.util.Date;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;
import kr.ac.kaist.ds.groupd.topology.InterestProtocol;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;

public class StaticGroupNameProtocol implements GroupNameProtocol<String> {

	private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

	private GroupName<String> groupName;

	private int interestProtocolPid;

	private long nodeId;

	public StaticGroupNameProtocol(String prefix) {
		this.interestProtocolPid = Configuration.getPid(prefix
				+ PAR_INTEREST_GROUP_PROTOCOL);
	}

	@Override
	public double compareWithGroupName(GroupName<String> otherName) {
		if (otherName.equals(groupName)) {
			return 1.0;
		}
		return 0;
	}

	@Override
	public GroupName<String> getGroupName() {
		return groupName;
	}

	@Override
	public GroupName<String> createGroupName() {
		if(this.groupName != null){
			return groupName;
		}
		Node node = findNode();
		InterestProtocol interestProtocol = (InterestProtocol) node.getProtocol(interestProtocolPid);
		Node representative = interestProtocol.getRepresentative();
		//FIXME for now we use the ID and do not generate a MAC address
		long id = representative.getID();
		long timestamp = new Date().getTime();
		return new StaticGroupName(String.valueOf(id), timestamp);
	}

	private Node findNode() {
		for (int i = 0; i < Network.size(); i++) {
			if (Network.get(i).getID() == nodeId) {
				return Network.get(i);
			}
		}
		return null;
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
		this.nodeId = node.getID();
	}
}
