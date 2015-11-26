package kr.ac.kaist.ds.groupd.groupname.impl;

import java.util.Date;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;

public class StaticGroupNameProtocol implements GroupNameProtocol {

	private GroupName  groupName;

	private int interestProtocolPid;

	private long nodeId;

	public StaticGroupNameProtocol(String prefix) {
		this.interestProtocolPid = Configuration.getPid(prefix
				+ PAR_INTEREST_GROUP_PROTOCOL);
	}

	@Override
	public boolean compareWithGroupName(GroupName  otherName) {
		return otherName.equals(groupName);
	}

	@Override
	public GroupName  getGroupName() {
		return groupName;
	}

	@Override
	public GroupName  createGroupName() {
		if(this.groupName != null){
			return groupName;
		}
		Node node = findNode();
		InterestProtocolImpl interestProtocol = (InterestProtocolImpl) node.getProtocol(interestProtocolPid);
		Node representative = interestProtocol.getRepresentative();
		//FIXME for now we use the ID and do not generate a MAC address
		long id = representative.getID();
		long timestamp = new Date().getTime();
		groupName = new StaticGroupName(String.valueOf(id), timestamp);
		return groupName;
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
	
    @Override
    public void setGroupName(GroupName groupName) {
        this.groupName = groupName;
    }
}
