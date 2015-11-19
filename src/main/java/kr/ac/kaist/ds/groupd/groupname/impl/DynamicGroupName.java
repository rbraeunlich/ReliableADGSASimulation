package kr.ac.kaist.ds.groupd.groupname.impl;

import kr.ac.kaist.ds.groupd.groupname.GroupName;

public class DynamicGroupName implements GroupName<Byte> {

	private byte groupName;

	public DynamicGroupName(byte name) {
		this.groupName = name;
	}
	
	@Override
	public Byte getName() {
		return groupName;
	}

}
