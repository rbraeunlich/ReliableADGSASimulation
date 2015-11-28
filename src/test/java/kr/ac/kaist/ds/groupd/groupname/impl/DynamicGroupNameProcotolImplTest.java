
package kr.ac.kaist.ds.groupd.groupname.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Properties;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import peersim.config.Configuration;
import peersim.core.GeneralNode;
import peersim.core.Network;
import peersim.core.Node;

public class DynamicGroupNameProcotolImplTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        Properties prop = new Properties();
        prop.put("protocol.foo", "DynamicGroupNameProtocol");
        // workaround because property lookup works strangely, is needed to be
        // able to call Configuration.getPID
        prop.put("test", "foo");
        prop.put("protocol.foo.interestgroup", "bar");
        prop.put("protocol.bar", "InterestProtocolImpl");
        prop.put("protocol.bar.coeff", "0.5");
        //don't take the following two parameters too serious
        prop.put("protocol.bar.linkable", "foo");
        prop.put("protocol.bar.naming", "foo");
        prop.put("protocol.bar.candVotes", "1");
        prop.put("protocol.bar.repThreshold", "1");
        prop.put("protocol.foo.similarity", "0.5");
        prop.put("protocol.foo.bits", "4");
        prop.put("network.size", "3");
        Configuration.setConfig(prop);
        Network.reset();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        try {
            // workaround because there is no way to reset the configuration
            Field configField = Configuration.class.getDeclaredField("config");
            configField.setAccessible(true);
            configField.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGroupNameSingleNode() {
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        GeneralNode node = new GeneralNode(null);
        InterestProtocolImpl protocol = getInterestProtocolImplFrom(node);
        protocol.setRepresentative(node);
        Network.add(node);
        nameProtocol.nextCycle(node, Configuration.getPid("test"));
        GroupName createGroupName = nameProtocol.createGroupName(node);
        assertThat(createGroupName,
                is(equalTo(new DynamicGroupName("00" + Long.toBinaryString(node.getID())))));
    }

	private InterestProtocolImpl getInterestProtocolImplFrom(Node node) {
		return (InterestProtocolImpl) node.getProtocol(Configuration.getPid("protocol.foo.interestgroup"));
	}

    @Test
    public void createGroupNameSeveralNodes() {
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        GeneralNode node = new GeneralNode(null);
        GeneralNode node2 = new GeneralNode(null);
        GeneralNode node3 = new GeneralNode(null);
        Network.add(node);
        Network.add(node2);
        Network.add(node3);
        InterestProtocolImpl interestProtocol = getInterestProtocolImplFrom(node);
        interestProtocol.addNeighbor(node2);
        interestProtocol.addNeighbor(node3);
        interestProtocol.setRepresentative(node);
        getInterestProtocolImplFrom(node2).setRepresentative(node);
        getInterestProtocolImplFrom(node3).setRepresentative(node);
        nameProtocol.nextCycle(node, Configuration.getPid("test"));
        GroupName createGroupName = nameProtocol.createGroupName(node);
        assertThat(
                createGroupName,
                is(equalTo(new DynamicGroupName("0" + Long.toBinaryString(node.getID()) + "0"
                        + Long.toBinaryString(node2.getID()) + Long.toBinaryString(node3.getID())))));
    }

    @Test
    public void compareWithEqualName() {
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        Node node = Network.get(0);
        getInterestProtocolImplFrom(node).setRepresentative(node);
		nameProtocol.nextCycle(node, Configuration.getPid("test"));
        GroupName createGroupName = nameProtocol.createGroupName(node);
        assertThat(nameProtocol.compareWithGroupName(createGroupName), is(true));
    }

    @Test
    public void compareWithDifferentNameEqualEnough() {
        GeneralNode node = new GeneralNode(null);
        GeneralNode node2 = new GeneralNode(null);
        Network.add(node);
        Network.add(node2);
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        InterestProtocol interestProtocol = (InterestProtocol)node.getProtocol(Configuration
                .getPid("protocol.foo.interestgroup"));
        interestProtocol.addNeighbor(node2);
        nameProtocol.nextCycle(node, Configuration.getPid("test"));
        nameProtocol.createGroupName(node);
        assertThat(
                nameProtocol.compareWithGroupName(new DynamicGroupName("0000" + "0"
                        + Long.toBinaryString(node2.getID()))), is(true));
    }

    @Test
    public void compareWithDifferentNameNotEqualEnough() {
        GeneralNode node = new GeneralNode(null);
        GeneralNode node2 = new GeneralNode(null);
        GeneralNode node3 = new GeneralNode(null);
        Network.add(node);
        Network.add(node2);
        Network.add(node3);
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        InterestProtocol interestProtocol = getInterestProtocolImplFrom(node);
        interestProtocol.addNeighbor(node2);
        getInterestProtocolImplFrom(node2).setRepresentative(node);
        nameProtocol.nextCycle(node, Configuration.getPid("test"));
        nameProtocol.createGroupName(node);
        assertThat(
                nameProtocol.compareWithGroupName(new DynamicGroupName("0000000"
                        + Long.toBinaryString(node3.getID()))), is(false));
    }
}
