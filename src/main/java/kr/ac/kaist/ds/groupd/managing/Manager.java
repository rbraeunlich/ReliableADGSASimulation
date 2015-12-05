package kr.ac.kaist.ds.groupd.managing;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;
import kr.ac.kaist.ds.groupd.search.SearchProtocol;
import kr.ac.kaist.ds.groupd.search.SearchQuery;
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

	private static final String PAR_SEARCH_PROTOCOL = "search";

	private static final String PAR_INIT = "init";

	private static final String PAR_MESSAGE_CYCLE = "messagecycle";

	private static final String PAR_NODE_CHANGE = "nodechange";

	private long startRangeMs;

	private long endRangeMs;

	private int interestGroupPid;

	private Random rand;

	private Date startTime;

	private Long interval;

	private NodeInitializer[] inits;

	private int searchProtocolPid;

	/**
	 * Determines in which cycles a new search query will be started.
	 */
	private int messageCycle;

	/**
	 * The most simple way to give every search query an idea. Of course not
	 * threadsafe...
	 */
	private int messageId = 0;

	private double nodeChanges;

	public Manager(String prefix) {
		this.startRangeMs = Configuration.getLong(prefix + "."
				+ PAR_TIME_RANGE_START_MS);
		this.endRangeMs = Configuration.getLong(prefix + "."
				+ PAR_TIME_RANGE_END_MS);
		this.interestGroupPid = Configuration.getPid(prefix + "."
				+ PAR_INTEREST_GROUP_PROTOCOL);
		this.searchProtocolPid = Configuration.getPid(prefix + "."
				+ PAR_SEARCH_PROTOCOL);
		this.messageCycle = Configuration.getInt(prefix + "."
				+ PAR_MESSAGE_CYCLE);
		this.nodeChanges = Configuration.getDouble(prefix + "."
				+ PAR_NODE_CHANGE);
		this.rand = new Random();
		Object[] tmp = Configuration.getInstanceArray(prefix + "." + PAR_INIT);
		inits = new NodeInitializer[tmp.length];
		for (int i = 0; i < tmp.length; ++i) {
			inits[i] = (NodeInitializer) tmp[i];
		}
	}

	/**
	 * Performs a change in the network, which means a node will change its
	 * location and connect with new nodes. This will cause a new election to
	 * take place.
	 */
	private void changeNetwork() {
		Node node = insertNewNodeIntoNetwork();
		InterestProtocol interestGroupProtocol = (InterestProtocol) node
				.getProtocol(interestGroupPid);
		interestGroupProtocol.startCommunityFormation(node, interestGroupPid);
		InterestProtocol protocol = (InterestProtocol) interestGroupProtocol
				.getRepresentative().getProtocol(interestGroupPid);
		protocol.performGroupNameSetting(
				interestGroupProtocol.getRepresentative(), interestGroupPid);
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
		Node removedNode = (Node) Network.remove(CommonState.r.nextInt(Network
				.size()));
		InterestProtocolImpl oldNodeInterestGroupProtocol = (InterestProtocolImpl) removedNode
				.getProtocol(interestGroupPid);
		removeNodeFromAllOldNeigbours(removedNode, oldNodeInterestGroupProtocol);
		Node newnode = (Node) Network.prototype.clone();
		InterestProtocolImpl newNodeInterestGroupProtocol = (InterestProtocolImpl) newnode
				.getProtocol(interestGroupPid);
		newNodeInterestGroupProtocol
				.setInterestVector(oldNodeInterestGroupProtocol.getInterest());
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
	 * Since the similarity is commutative, we do not need to check all nodes in
	 * the network
	 */
	private void removeNodeFromAllOldNeigbours(Node removedNode,
			InterestProtocol oldNodeInterestGroupProtocol) {
		Collection<Node> neighbours = oldNodeInterestGroupProtocol
				.getNeighbours();
		neighbours.stream()
				.map(n -> (InterestProtocol) n.getProtocol(interestGroupPid))
				.forEach(p -> p.removeNeighbour(removedNode));
	}

	/**
	 * @see peersim.core.Control#execute()
	 */
	public boolean execute() {
		double nrOfNodesToChange = nodeChanges;
		for (int i = 0; i < nrOfNodesToChange; i++) {
			changeNetwork();
		}
		if (CommonState.getIntTime() % messageCycle == 0) {
			int networkSize = Configuration.getInt("network.size");
			Random random = new Random();
			int destination = random.nextInt(networkSize);
			int source = random.nextInt(networkSize);
			Node node = Network.get(source);
			SearchProtocol protocol = (SearchProtocol) node
					.getProtocol(searchProtocolPid);
			protocol.addSearchQuery(new SearchQuery(source, destination,
					CommonState.getIntTime(), messageId));
			messageId++;
		}
		return false;
	}

}
