package kr.ac.kaist.ds.groupd.groupname.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;
import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;

public class DynamicGroupNameProcotolImplTest {
    
    @BeforeClass
    public static void setUpBeforeClass() {
        Properties prop = new Properties();
        prop.put("protocol.foo", "DynamicGroupNameProtocol");
        //workaround because property lookup works strangely, is needed to be able to call Configuration.getPID
        prop.put("test", "foo");
        prop.put("protocol.foo.interestgroup", "bar");
        prop.put("protocol.bar", "InterestProtocolImpl");
        prop.put("protocol.foo.similarity", "0.8");
        prop.put("protocol.foo.bits", "3");
        prop.put("network.size", "3");
        Configuration.setConfig(prop);
        Network.reset();
    }
    
    @Test
    public void createGroupNameSingleNode(){
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        nameProtocol.nextCycle(Network.get(0), Configuration.getPid("test"));
        GroupName<String> createGroupName = nameProtocol.createGroupName();
        assertThat(createGroupName, is(equalTo(new DynamicGroupName("000"))));
    }
    
    @Test
    public void createGroupNameSeveralNodes(){
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        Node node = Network.get(0);
        InterestProtocolImpl interestProtocol = (InterestProtocolImpl)node.getProtocol(Configuration.getPid("protocol.foo.interestgroup"));
        interestProtocol.addNeighbor(Network.get(1));
        interestProtocol.addNeighbor(Network.get(2));
        nameProtocol.nextCycle(node, Configuration.getPid("test"));
        GroupName<String> createGroupName = nameProtocol.createGroupName();
        assertThat(createGroupName, is(equalTo(new DynamicGroupName("000001010"))));
    }
    
    @Test
    public void compareWithEqualName(){
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        nameProtocol.nextCycle(Network.get(0), Configuration.getPid("test"));
        GroupName<String> createGroupName = nameProtocol.createGroupName();
        assertThat(nameProtocol.compareWithGroupName(createGroupName), is(1.0));
    }
    
    @Test
    public void compareWithDifferentName(){
        Node node = Network.get(0);
        DynamicGroupNameProtocol nameProtocol = new DynamicGroupNameProtocol("protocol.foo");
        InterestProtocolImpl interestProtocol = (InterestProtocolImpl)node.getProtocol(Configuration.getPid("protocol.foo.interestgroup"));
        interestProtocol.addNeighbor(Network.get(1));
        nameProtocol.nextCycle(Network.get(0), Configuration.getPid("test"));
        nameProtocol.createGroupName();
        assertThat(nameProtocol.compareWithGroupName(new DynamicGroupName("000111")), is(0.5));
    }
}
