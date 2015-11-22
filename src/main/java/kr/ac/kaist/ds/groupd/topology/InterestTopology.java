
package kr.ac.kaist.ds.groupd.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import kr.ac.kaist.ds.groupd.information.GroupInformation;
import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import kr.ac.kaist.ds.groupd.interest.impl.InterestProtocolImpl;
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

    public InterestTopology(String prefix) {
        super(prefix);
        clusteringCoefficient = Configuration.getDouble(parName(prefix, PAR_CLUSTER_COEFF));
        pid = Configuration.getPid(parName(prefix, PAR_PROTOCOL));
        numberCandidateVotes = Configuration.getInt(parName(prefix, PAR_NR_CAND_VOTES));
        representativeThreshold = Configuration.getInt(parName(prefix, PAR_REP_THRESHOLD));
    }

    private String parName(String prefix, String parName) {
        return prefix + "." + parName;
    }

    @Override
    public void wire(Graph g) {
        System.out.println("start the Create Local Communities");
        createLocalCommunities(g);
        // createGlobalCommunities(g);
        System.out.println("start the revote");

        resetVotes(g);
    }

	/**
	 * Creates the local community by creating edges between nodes whose similarity is
	 * greater than the {@link #clusteringCoefficient}.
	 */
    private void createLocalCommunities(Graph g) {
        // since I do not know how to combine two Linkable protocols
        // I use this workaround which assumes, that all nodes can see each
        // other

        // add my source code here

        for (int i = 0; i < g.size(); i++) {
            Node node1 = (Node)g.getNode(i);
         //   System.out.println("test :"
           //         + InterestInitializer.getManagerGroups().existnodeInManagerGroups(node1));
        if(0 == i)
            InterestInitializer.getManagerGroups().addNode(node1);
        else
            if(false == InterestInitializer.getManagerGroups().existNodeInManagerGroups(
                            node1))
            {
                GroupInformation temp = new GroupInformation(InterestInitializer.getManagerGroups().getAllGroupInformations().size()+1);
                InterestInitializer.getManagerGroups().addGroupInformation(temp);
                InterestInitializer.getManagerGroups().setNowGroupIndex(temp);
                InterestInitializer.getManagerGroups().addNode(node1);
            }
            else if(true == InterestInitializer.getManagerGroups().existNodeInManagerGroups(
                    node1))
                continue;
        
            //  System.out.println("test :"
         //           + InterestInitializer.getManagerGroups().existnodeInManagerGroups(node1)+"\nsize:"+g.size());
        
            for (int j = i + 1; j < g.size(); j++) {
                Node node2 = (Node)g.getNode(j);
                double similarity = calculateSimilarity(node1, node2);
                if (similarity > clusteringCoefficient) {
                    g.setEdge(i, j);
                    InterestProtocol nodeProtocol = (InterestProtocol)node1.getProtocol(pid);
                    InterestProtocol node2Protocol = (InterestProtocol)node2.getProtocol(pid);
                    ((InterestProtocolImpl)nodeProtocol).addNeighbor(node2);
                    ((InterestProtocolImpl)node2Protocol).addNeighbor(node1);

                    if (false == InterestInitializer.getManagerGroups().existNodeInManagerGroups(
                            node2))
                        InterestInitializer.getManagerGroups().addNode(node2);

                }
            }

            System.out.println("the ManagementGroup("
                    + InterestInitializer.getManagerGroups().getNowGroupIndex()
                    + ")'s Node is "
                    + InterestInitializer
                            .getManagerGroups()
                            .getGroupInformation(
                                    InterestInitializer.getManagerGroups().getNowGroupIndex())
                            .getNeighborNodes().size());
        }
    }

	/**
	 * Calculates the similarity based on the cosine similarity. 
	 * See <a href="https://en.wikipedia.org/wiki/Cosine_similarity">Wikipedia<a/>
	 * @param node
	 * @param node2
	 * @return
	 */
	private double calculateSimilarity(Node node, Node node2) {
		InterestProtocolImpl nodeProtocol = (InterestProtocolImpl) node
				.getProtocol(pid);
		InterestProtocolImpl node2Protocol = (InterestProtocolImpl) node2
				.getProtocol(pid);
		double[] interest = nodeProtocol.getInterest();
		double[] interest2 = node2Protocol.getInterest();
		double dotProduct = calculateDotProduct(interest, interest2);
		double magnitude = nodeProtocol.getMagnitude();
		double magnitude2 = node2Protocol.getMagnitude();
		return dotProduct / (magnitude * magnitude2);
    }

	private double calculateDotProduct(double[] interest, double[] interest2) {
		if(interest.length != interest2.length){
			throw new IllegalArgumentException("Vectors are of unequal length");
		}
		double sum = 0.0;
		for(int i = 0; i < interest.length; i ++){
			sum += interest[i] * interest2[i];
        }
		return sum;
    }

	/**
	 * Creates the global community by initiating the voting mechanism for candidates and representatives.
	 */
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
			InterestProtocolImpl protocol = (InterestProtocolImpl) node
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
					.map(n -> (InterestProtocolImpl) n.getProtocol(pid))
					.forEach(p -> p.receiveCandidateVote());
		}
	}

	/**
	 * Every node, that crossed the {@link #representativeThreshold} is a
	 * prospective representative peer.
	 */
	private void potentialRepresentativeIndentification(Graph g) {
		for (Node node : new GraphIteratorWrapper(g)) {
			InterestProtocolImpl protocol = (InterestProtocolImpl) node
					.getProtocol(pid);
			Collection<Node> neighbours = protocol.getInterestCommunity();
			Collection<Node> candidates = new ArrayList<Node>(neighbours);
			candidates.add(node);
			List<Node> representativeCandidates = candidates
					.stream()
					.filter(n -> ((InterestProtocolImpl) n.getProtocol(pid))
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
			((InterestProtocolImpl) representative.getProtocol(pid))
					.receiveRepresentativeVote();
		}
	}

	/**
	 * The candidate with the most votes will be selected as representative.
	 */
	private void actualRepresentativeElection(Graph g) {
		// I will skip the special cases for now, see
		// A peer-to-peer recommender system for self-emerging user communities
		// based on gossip overlays (2013)
		// for that
		for (Node node : new GraphIteratorWrapper(g)) {
			InterestProtocolImpl protocol = (InterestProtocolImpl) node
					.getProtocol(pid);
			List<Node> neighbours = new ArrayList<Node>(
					protocol.getInterestCommunity());
			neighbours.add(node);
			List<Node> representative = neighbours
					.stream()
					.filter(n -> ((InterestProtocolImpl) n.getProtocol(pid))
							.getRepresentativeVotes() > 0)
					.sorted((n, n2) -> ((InterestProtocolImpl) n.getProtocol(pid))
							.getRepresentativeVotes().compareTo(
									((InterestProtocolImpl) n2.getProtocol(pid))
											.getRepresentativeVotes()))
					.limit(1).collect(Collectors.toList());
			protocol.setRepresentative(representative.get(0));
		}
	}

	private void resetVotes(Graph g) {
		for (int i = 0; i < g.size(); i++) {
			Node node = (Node) g.getNode(i);
			InterestProtocolImpl protocol = (InterestProtocolImpl) node
					.getProtocol(pid);
			protocol.resetVotes();
		}
	}

	/**
	 * Wraps a graph with an {@link Iterable} to be able to use it inside a for-each loop.
	 *
	 */
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
                    Node node = (Node)g.getNode(counter);
                    counter++;
                    return node;
                }
            };
        }

    }
}
