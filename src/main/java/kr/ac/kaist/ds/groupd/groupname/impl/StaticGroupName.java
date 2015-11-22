package kr.ac.kaist.ds.groupd.groupname.impl;

import kr.ac.kaist.ds.groupd.groupname.GroupName;

public class StaticGroupName implements GroupName<String> {

	private String macAddress;
	private Long timestamp;

	public StaticGroupName(String macAddress, Long timestamp) {
		this.macAddress = macAddress;
		this.timestamp = timestamp;
	}

	@Override
	public String getName() {
		return macAddress + timestamp;
	}

}
