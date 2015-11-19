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
public interface GroupNameProtocol<T> extends CDProtocol {

	/**
	 * Compares this group name with another one and returns the percentage of similarity.
	 * @param otherName
	 * @return
	 */
	double compareWithGroupName(GroupName<T> otherName);

	GroupName<T> getGroupName();
	
	/**
	 * Forms a group names, saves it itself (replacing the existing one) and returns it.
	 */
	GroupName<T> createGroupName();
}
