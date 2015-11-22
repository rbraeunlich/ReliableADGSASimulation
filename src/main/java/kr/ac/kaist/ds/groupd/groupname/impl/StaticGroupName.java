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

    @Override
    public int hashCode() {
        //2^5-1
        final int prime = 31;
        int result = 1;
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StaticGroupName other = (StaticGroupName)obj;
        if (null == macAddress) {
            if (null != other.macAddress)
                return false;
        } else if (!macAddress.equals(other.macAddress))
            return false;
        
        if (null == timestamp) {
            if (null != other.timestamp)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }

	
}
