
package kr.ac.kaist.ds.groupd.managing;

import java.util.Date;
import java.util.Random;

import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;
import kr.ac.kaist.ds.groupd.topology.InterestInitializer;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class Manager implements Control {

    private static final String PAR_TIME_RANGE_START_MS = "start";

    private static final String PAR_TIME_RANGE_END_MS = "end";

    private static final String PAR_INTEREST_GROUP_PROTOCOL = "interestgroup";

    private static final String PAR_INIT = "init";

    private long startRangeMs;

    private long endRangeMs;

    private int interestGroupPid;

    private Random rand;

    private Date startTime;

    private Long interval;

    private NodeInitializer[] inits;

    public Manager(String prefix) {
        this.startRangeMs = Configuration.getLong(prefix + "." + PAR_TIME_RANGE_START_MS);
        this.endRangeMs = Configuration.getLong(prefix + "." + PAR_TIME_RANGE_END_MS);
        this.interestGroupPid = Configuration.getPid(prefix + "." + PAR_INTEREST_GROUP_PROTOCOL);
        this.rand = new Random();
        Object[] tmp = Configuration.getInstanceArray(prefix + "." + PAR_INIT);
        inits = new NodeInitializer[tmp.length];
        for (int i = 0; i < tmp.length; ++i) {
            inits[i] = (NodeInitializer)tmp[i];
        }
    }

    /**
     * Performs a change in the network, which means a node will change its
     * location and connect with new nodes. This will cause a new election to
     * take place.
     */
    private void changeNetwork() {
        Node node = insertNewNodeIntoNetwork();
        InterestProtocolImpl interestGroupProtocol = (InterestProtocolImpl)node
                .getProtocol(interestGroupPid);
        //TODO should we also start the election for other nodes?
        interestGroupProtocol.startElection();
    }

    /**
     * This method removes an existing node, creates a new one, copies the
     * interst vector of the old one to the new one and inserts the new node
     * into the network again. It is necessary to create a new node because the
     * state of the removed one is set to FAILED after removal.
     * 
     * @return the newly created and inserted node
     */
    private Node insertNewNodeIntoNetwork() {
        Node removedNode = (Node)Network.remove(CommonState.r.nextInt(Network.size()));
        InterestProtocolImpl oldNodeInterestGroupProtocol = (InterestProtocolImpl)removedNode
                .getProtocol(interestGroupPid);
        Node newnode = (Node)Network.prototype.clone();
        InterestProtocolImpl newNodeInterestGroupProtocol = (InterestProtocolImpl)newnode
                .getProtocol(interestGroupPid);
        newNodeInterestGroupProtocol.setInterestVector(oldNodeInterestGroupProtocol.getInterest());
        for (int j = 0; j < inits.length; ++j) {
            NodeInitializer initializer = inits[j];
            if (initializer instanceof InterestInitializer) {
                continue;
            }
            initializer.initialize(newnode);
        }
        Network.add(newnode);
        return newnode;
    }

    /**
     * @see peersim.core.Control#execute()
     */
    public boolean execute() {
        if (interval == null) {
            startTime = new Date();
            long range = endRangeMs - startRangeMs + 1;
            // compute a fraction of the range, 0 <= frac < range
            long fraction = (long)(range * rand.nextDouble());
            interval = fraction + startRangeMs;
        }
        if (new Date().getTime() - startTime.getTime() >= interval) {
            interval = null;
            changeNetwork();
        }
        return false;
    }

}
