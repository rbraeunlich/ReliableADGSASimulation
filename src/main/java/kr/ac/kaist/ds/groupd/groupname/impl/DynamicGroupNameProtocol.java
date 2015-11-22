
package kr.ac.kaist.ds.groupd.groupname.impl;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;

import java.util.Collection;
import java.util.HashSet;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;

public class DynamicGroupNameProtocol implements GroupNameProtocol<String> {

    private static final String PAR_BITS_USED = "bits";

    private static final String PAR_SIMILARITY_THRESHOLD = "similarity";

    private int interestProtocolPid;

    private double similarityThreshold;

    private int bitsUsed;

    private long nodeId;

    private GroupName<String> groupName;

    public DynamicGroupNameProtocol(String prefix) {
        this.interestProtocolPid = Configuration.getPid(prefix + "." + PAR_INTEREST_GROUP_PROTOCOL);
        this.similarityThreshold = Configuration.getDouble(prefix + "." + PAR_SIMILARITY_THRESHOLD);
        this.bitsUsed = Configuration.getInt(prefix + "." + PAR_BITS_USED);
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
        this.nodeId = node.getID();
    }

    @Override
    public double compareWithGroupName(GroupName<String> otherName) {
        String other = otherName.getName();
        String thisName = this.groupName.getName();
        int difference = other.length() - thisName.length();
        // the other one is shorter
        if (difference < 0) {
            other = fillNameToEqualLength(other, Math.abs(difference));
        } else if (difference > 0) {
            // this name is shorter
            thisName = fillNameToEqualLength(thisName, Math.abs(difference));
        }
        return compareNamesByBits(other, thisName);
    }

    /**
     * match the each string's Length (Abjust the Length as long)
     * 
     * @param other      short string than an other
     * @param difference Length of gap
     * @return
     */
    private String fillNameToEqualLength(String other, int difference) {
        String s = other;
        for (int i = 0; i < difference; i++) {
            s += "0";
        }
        return s;
    }

    private double compareNamesByBits(String other, String thisName) {
        double nodesChanged = 0;
        for (int i = 0; i < (other.length() / bitsUsed); i++) {
            for (int j = 0; j < bitsUsed; j++) {
                if (other.charAt(i * bitsUsed + j) != thisName.charAt(i*bitsUsed + j)) {
                    nodesChanged++;
                    break;
                }
            }
        }
        return 1 - (nodesChanged / (other.length() / bitsUsed));
    }

    @Override
    public GroupName<String> getGroupName() {
        return groupName;
    }

    @Override
    public GroupName<String> createGroupName() {
        Node node = findNode();
        InterestProtocolImpl interestProtocol = (InterestProtocolImpl)node
                .getProtocol(interestProtocolPid);
        Collection<Node> interestCommunity = new HashSet<>(interestProtocol.getInterestCommunity());
        interestCommunity.add(node);
        String groupId = interestCommunity.stream()
                .sorted((n1, n2) -> Long.compare(n1.getID(), n2.getID()))
                .map(n -> Long.toBinaryString(n.getID())).map(s -> {
                    if (s.length() >= bitsUsed) {
                        return s.substring(0, bitsUsed);
                    } else {
                        for (int i = 0; i <= (bitsUsed - s.length()); i++) {
                            s = "0" + s;
                        }
                        return s;
                    }

                }).reduce("", (s1, s2) -> s1 + s2);
        groupName = new DynamicGroupName(groupId);
        return groupName;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Node findNode() {
        for (int i = 0; i < Network.size(); i++) {
            if (Network.get(i).getID() == nodeId) {
                return Network.get(i);
            }
        }
        return null;
    }
}
