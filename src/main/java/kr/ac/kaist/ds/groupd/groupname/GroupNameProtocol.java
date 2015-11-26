package kr.ac.kaist.ds.groupd.groupname;

import peersim.cdsim.CDProtocol;

/**
 * To align the different group naming schemes we place them under the same
 * interface. Every {@link GroupNameProtocol} instance holds group name of the
 * group the current node belongs to.
 * 
 * @param <S>
 *            the actual type how the name is formed.
 */
public interface GroupNameProtocol extends CDProtocol {

	static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";
	/**
	 * Compares this group name with another one and returns true is the name is considered
	 * to be as similar to the others, so it can be considered the same groupname again.
	 * @param otherName
	 * @return
	 */
	boolean compareWithGroupName(GroupName otherName);

	GroupName getGroupName();
	
	/**
	 * Forms a group names, saves it itself (replacing the existing one) and returns it.
	 */
	GroupName createGroupName();
	
	void setGroupName(GroupName groupName);
}
