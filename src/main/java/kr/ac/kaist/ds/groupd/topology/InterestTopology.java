package kr.ac.kaist.ds.groupd.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.SparseVector;

import peersim.config.Configuration;
import peersim.core.Node;
import peersim.dynamics.WireGraph;
import peersim.graph.Graph;

public class InterestTopology extends WireGraph {

	private static final String PAR_CLUSTER_COEFF = "coeff";
	private static final String PAR_PROTOCOL = "interest_protocol";
	private static final String PAR_NR_CAND_VOTES = "candVotes";
	private static final String PAR_REP_THRESHOLD = "repThreshold";

	private final double clusteringCoefficient;
	/**
	 * {@link InterestProtocol} pid
	 */
	private final int pid;
	private final int numberCandidateVotes;
	private final int representativeThreshold;

	@Override
	public void wire(Graph g) {
		System.out.println("start the wire");
		createLocalCommunities(g);
		createGlobalCommunities(g);
		System.out.println("start the revote");
		resetVotes(g);
	}
	
	private void createLocalCommunities(Graph g) {
		//since I do not know how to combine two Linkable protocols
		//I use this workaround which assumes, that all nodes can see each other
		for (int i = 0; i < g.size(); i++) {
			Node node1 = (Node) g.getNode(i);
			for (int j = i + 1; j < g.size(); j++) {
				Node node2 = (Node) g.getNode(j);
				double similarity = calculateSimilarity(node1, node2);
				if (similarity > clusteringCoefficient) {
					g.setEdge(i, j);
					InterestProtocol nodeProtocol = (InterestProtocol) node1
							.getProtocol(pid);
					InterestProtocol node2Protocol = (InterestProtocol) node2
							.getProtocol(pid);
					nodeProtocol.addNeighbor(node2);
					node2Protocol.addNeighbor(node1);
					
				}
			}
		}
	}

	
	public InterestTopology(String prefix) {
		super(prefix);
		clusteringCoefficient = Configuration.getDouble(parName(prefix,
				PAR_CLUSTER_COEFF));
		pid = Configuration.getPid(parName(prefix, PAR_PROTOCOL));
		numberCandidateVotes = Configuration.getInt(parName(prefix,
				PAR_NR_CAND_VOTES));
		representativeThreshold = Configuration.getInt(parName(prefix,
				PAR_REP_THRESHOLD));
	}

	private String parName(String prefix, String parName) {
		return prefix + "." + parName;
	}


	private double calculateSimilarity(Node node, Node node2) {
		InterestProtocol nodeProtocol = (InterestProtocol) node
				.getProtocol(pid);
		InterestProtocol node2Protocol = (InterestProtocol) node2
				.getProtocol(pid);
		SparseVector<Real> interest = nodeProtocol.getInterest();
		SparseVector<Real> interest2 = node2Protocol.getInterest();
		Real dotProduct = interest.times(interest2);
		Double magnitude = calculateMagnitude(interest);
		Double magnitude2 = calculateMagnitude(interest);
		return dotProduct.doubleValue() / (magnitude * magnitude2);
	}

	private Double calculateMagnitude(SparseVector<Real> interest) {
		Double sum = 0.0;
		for(int i = 0; i < interest.getDimension(); i++){
			sum += Math.pow(interest.get(i).doubleValue(), 2.0);
		}
		return Math.sqrt(sum);
	}

	private void createGlobalCommunities(Graph g) {
		candidateSelectionAndVote(g);
		potentialRepresentativeIndentification(g);
		actualRepresentativeElection(g);
	}

	/**
	 * The most similiar neighbours get a vote. The number of votes that can be
	 * given is defined {@link #numberCandidateVotes}
	 */
	private void candidateSelectionAndVote(Graph g) {
		for (Node node : new GraphIteratorWrapper(g)) {
			InterestProtocol protocol = (InterestProtocol) node
					.getProtocol(pid);
			Collection<Node> neighbours = protocol.getInterestCommunity();
			// FIXME I assume that no two neighbors have the same similarity
			TreeMap<Double, Node> votings = new TreeMap<Double, Node>();
			for (Node node2 : neighbours) {
				double similarity = calculateSimilarity(node, node2);
				votings.put(similarity, node2);
			}
			List<Entry<Double, Node>> listVotings = new ArrayList<Entry<Double, Node>>(
					votings.entrySet());
			listVotings.stream()
					.sorted((v, v2) -> v.getKey().compareTo(v2.getKey()))
					.limit(numberCandidateVotes).map(c -> c.getValue())
					.map(n -> (InterestProtocol) n.getProtocol(pid))
					.forEach((p) -> p.receiveCandidateVote());
		}
	}

	/**
	 * Every node, that crossed the {@link #representativeThreshold} is a
	 * prospective representative peer.
	 */
	private void potentialRepresentativeIndentification(Graph g) {
		for (Node node : new GraphIteratorWrapper(g)) {
			InterestProtocol protocol = (InterestProtocol) node
					.getProtocol(pid);
			Collection<Node> neighbours = protocol.getInterestCommunity();
			Collection<Node> candidates = new ArrayList<Node>(neighbours);
			candidates.add(node);
			List<Node> representativeCandidates = candidates
					.stream()
					.filter(n -> ((InterestProtocol) n.getProtocol(pid))
							.getCandidateVotes() > representativeThreshold)
					.collect(Collectors.toList());
			double minDistance = Double.MAX_VALUE;
			Node representative = null;
			for (Node node2 : representativeCandidates) {
				double similarity = calculateSimilarity(node, node2);
				if (1 - similarity < minDistance) {
					minDistance = 1 - similarity;
					representative = node2;
				}
			}
			((InterestProtocol) representative.getProtocol(pid))
					.receiveRepresentativeVote();
		}
	}

	private void actualRepresentativeElection(Graph g) {
		// I will skip the special cases for now, see
		// A peer-to-peer recommender system for self-emerging user communities
		// based on gossip overlays (2013)
		// for that
		for (Node node : new GraphIteratorWrapper(g)) {
			InterestProtocol protocol = (InterestProtocol) node
					.getProtocol(pid);
			List<Node> neighbours = new ArrayList<Node>(
					protocol.getInterestCommunity());
			neighbours.add(node);
			List<Node> representative = neighbours
					.stream()
					.filter(n -> ((InterestProtocol) n.getProtocol(pid))
							.getRepresentativeVotes() > 0)
					.sorted((n, n2) -> ((InterestProtocol) n.getProtocol(pid))
							.getRepresentativeVotes().compareTo(
									((InterestProtocol) n2.getProtocol(pid))
											.getRepresentativeVotes()))
					.limit(1).collect(Collectors.toList());
			protocol.setRepresentative(representative.get(0));
		}
	}

	private void resetVotes(Graph g) {
		for (int i = 0; i < g.size(); i++) {
			Node node = (Node) g.getNode(i);
			InterestProtocol protocol = (InterestProtocol) node
					.getProtocol(pid);
			protocol.resetVotes();
		}
	}

	private static class GraphIteratorWrapper implements Iterable<Node> {

		private Graph g;

		public GraphIteratorWrapper(Graph g) {
			this.g = g;
		}

		@Override
		public Iterator<Node> iterator() {
			return new Iterator<Node>() {

				private int counter = 0;

				@Override
				public boolean hasNext() {
					return counter < g.size();
				}

				@Override
				public Node next() {
					Node node = (Node) g.getNode(counter);
					counter++;
					return node;
				}
			};
		}

	}
}
