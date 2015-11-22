package kr.ac.kaist.ds.groupd.groupname.impl;

import kr.ac.kaist.ds.groupd.groupname.GroupName;

public class DynamicGroupName implements GroupName<String> {

	private String groupName;

	public DynamicGroupName(String name) {
		this.groupName = name;
	}
	
	@Override
	public String getName() {
		return groupName;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DynamicGroupName other = (DynamicGroupName)obj;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DynamicGroupName [groupName=" + groupName + "]";
    }

	
}
