package kr.ac.kaist.ds.groupd.groupname.impl;

import peersim.config.Configuration;
import peersim.core.Node;
import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;

public class DynamicGroupNameProtocol implements GroupNameProtocol<Byte> {

	private static final String PAR_BITS_USED = "bits";
	private static final String PAR_SIMILARITY_THRESHOLD = "similarity";
	private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";
	private int interestProtocolPid;
	private double similarityThreshold;
	private int bitsUsed;

	public DynamicGroupNameProtocol(String prefix) {
		this.interestProtocolPid = Configuration.getPid(prefix
				+ PAR_INTEREST_GROUP_PROTOCOL);
		this.similarityThreshold = Configuration.getDouble(prefix + PAR_SIMILARITY_THRESHOLD);
		this.bitsUsed = Configuration.getInt(prefix+ PAR_BITS_USED);
	}

	@Override
	public void nextCycle(Node node, int protocolID) {
		// TODO Auto-generated method stub

	}

	@Override
	public double compareWithGroupName(GroupName<Byte> otherName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GroupName<Byte> getGroupName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroupName<Byte> createGroupName() {
		// TODO Auto-generated method stub
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
}
