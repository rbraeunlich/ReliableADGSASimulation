
package kr.ac.kaist.ds.groupd.interest.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import kr.ac.kaist.ds.groupd.groupname.GroupName;
import kr.ac.kaist.ds.groupd.groupname.GroupNameProtocol;
import kr.ac.kaist.ds.groupd.interest.InterestProtocol;
import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;

public class InterestProtocolImpl implements InterestProtocol {

    private static final String PAR_LINKABLE_PROTOCOL = "linkable";

    private static final String PAR_CLUSTER_COEFF = "coeff";

    private static final String PAR_NR_CAND_VOTES = "candVotes";

    private static final String PAR_REP_THRESHOLD = "repThreshold";

    private static final String PAR_NAMING_PROTOCOL = "naming";

    private List<Node> interestCommunity = new ArrayList<Node>();

    private int candidateVotes = 0;

    private int representativeVotes;

    private Node representative;

    private double[] interestVector;

    private double magnitude = 0.0;

    private boolean startElection = true;

    private final double clusteringCoefficient;

    private int linkableProtocolPid;

    private final int numberCandidateVotes;

    private final int representativeThreshold;

    private int namingProtocolPid;

    public InterestProtocolImpl(String prefix) {
        this.clusteringCoefficient = Configuration.getDouble(prefix + "." + PAR_CLUSTER_COEFF);
        this.linkableProtocolPid = Configuration.getPid(prefix + "." + PAR_LINKABLE_PROTOCOL);
        this.numberCandidateVotes = Configuration.getInt(prefix + "." + PAR_NR_CAND_VOTES);
        this.representativeThreshold = Configuration.getInt(prefix + "." + PAR_REP_THRESHOLD);
        this.namingProtocolPid = Configuration.getPid(prefix + "." + PAR_NAMING_PROTOCOL);
    }

    public double[] getInterest() {
        return interestVector;
    }

    @Override
    public Object clone() {
        try {
            InterestProtocolImpl clone = (InterestProtocolImpl)super.clone();
            clone.interestCommunity = new ArrayList<Node>();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Increases the candidate votes by one.
     */
    public void receiveCandidateVote() {
        candidateVotes++;
    }

    public int getCandidateVotes() {
        return candidateVotes;
    }

    /**
     * Increases the representative votes by one.
     */
    public void receiveRepresentativeVote() {
        representativeVotes++;
    }

    public Integer getRepresentativeVotes() {
        return representativeVotes;
    }

    public void setRepresentative(Node node) {
        this.representative = node;
    }

    /**
     * Sets candidate and representative votings to zero. Has to be called after
     * an election finishes.
     * 
     * @param protocolID
     * @param node
     */
    private void resetVotes(Node node, int protocolID) {
        resetVotes();
        for (Node n : getNeighbours()) {
            InterestProtocol protocol = (InterestProtocol)n.getProtocol(protocolID);
            protocol.resetVotes();
        }
    }

    public Node getRepresentative() {
        return representative;
    }

    public void setInterestVector(double[] interestVector) {
        this.interestVector = interestVector;
        this.magnitude = calculateMagnitude(this.interestVector);
    }

    /**
     * Returns the <a href=
     * "https://en.wikipedia.org/wiki/Magnitude_%28mathematics%29#Euclidean_vector_space">
     * magnitude</a> of the vector that is returned by {@link #getInterest()}.
     * The magnitude is precomputed because it is needed quite often.
     * 
     * @return
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Calculates the <a href=
     * "https://en.wikipedia.org/wiki/Magnitude_%28mathematics%29#Euclidean_vector_space">
     * magnitude</a> of a vector.
     * 
     * @param vector
     * @return
     */
    private double calculateMagnitude(double[] vector) {
        double sum = 0.0;
        for (int i = 0; i < vector.length; i++) {
            sum += Math.pow(vector[i], 2.0);
        }
        return Math.sqrt(sum);
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
        if (startElection) {
            startCommunityFormation(node, protocolID);
            startElection = false;
            if (isThisNodeRepresentativeForSomeone(node, protocolID)) {
                GroupNameProtocol namingProtocol = (GroupNameProtocol)node
                        .getProtocol(namingProtocolPid);
                createAndSetGroupName(namingProtocol, node, protocolID);
            }
        }
    }

    private boolean isThisNodeRepresentativeForSomeone(Node node, int protocolID) {
        return getNeighbours().stream().map(n -> (InterestProtocol)n.getProtocol(protocolID))
                .anyMatch(p -> node.equals(p.getRepresentative()));
    }

    private void createAndSetGroupName(GroupNameProtocol namingProtocol, Node node, int protocolID) {
        GroupName groupName = namingProtocol.createGroupName(node);
        getNeighbours().stream()
            .filter(n -> node.equals(((InterestProtocol)n.getProtocol(protocolID)).getRepresentative()))
            .map(n -> (GroupNameProtocol)n.getProtocol(namingProtocolPid))
            .forEach(p -> p.setGroupName(groupName));
    }

    private void startCommunityFormation(Node node, int protocolID) {
        createLocalCommunities(node, protocolID);
        createGlobalCommunities(node, protocolID);
        resetVotes(node, protocolID);
    }

    /**
     * Creates the local community by creating edges between nodes whose
     * similarity is greater than the {@link #clusteringCoefficient}.
     * 
     * @param protocolID
     * @param node
     */
    private void createLocalCommunities(Node node, int protocolID) {
        // since I do not know how to combine two Linkable protocols
        // I use this workaround which assumes, that all nodes can see each
        // other
        Linkable links = (Linkable)node.getProtocol(linkableProtocolPid);
        for (int i = 0; i < links.degree(); i++) {
            Node neighbour = links.getNeighbor(i);
            double similarity = calculateSimilarity(node, neighbour, protocolID);
            if (similarity > clusteringCoefficient) {
                InterestProtocolImpl node2Protocol = (InterestProtocolImpl)neighbour
                        .getProtocol(protocolID);
                this.addNeighbor(neighbour);
                node2Protocol.addNeighbor(node);
            }
        }
    }

    /**
     * Calculates the similarity based on the cosine similarity. See
     * <a href="https://en.wikipedia.org/wiki/Cosine_similarity">Wikipedia<a/>
     * 
     * @param node
     * @param node2
     * @param protocolID
     * @return
     */
    private double calculateSimilarity(Node node, Node node2, int protocolID) {
        InterestProtocolImpl nodeProtocol = (InterestProtocolImpl)node.getProtocol(protocolID);
        InterestProtocolImpl node2Protocol = (InterestProtocolImpl)node2.getProtocol(protocolID);
        double[] interest = nodeProtocol.getInterest();
        double[] interest2 = node2Protocol.getInterest();
        double dotProduct = calculateDotProduct(interest, interest2);
        double magnitude = nodeProtocol.getMagnitude();
        double magnitude2 = node2Protocol.getMagnitude();
        return dotProduct / (magnitude * magnitude2);
    }

    private double calculateDotProduct(double[] interest, double[] interest2) {
        if (interest.length != interest2.length) {
            throw new IllegalArgumentException("Vectors are of unequal length");
        }
        double sum = 0.0;
        for (int i = 0; i < interest.length; i++) {
            sum += interest[i] * interest2[i];
        }
        return sum;
    }

    /**
     * Creates the global community by initiating the voting mechanism for
     * candidates and representatives.
     * 
     * @param protocolID
     * @param node
     */
    private void createGlobalCommunities(Node node, int protocolID) {
        candidateSelectionAndVote(node, protocolID);
        potentialRepresentativeIndentification(node, protocolID);
        actualRepresentativeElection(node, protocolID);
    }

    /**
     * The most similiar neighbours get a vote. The number of votes that can be
     * given is defined {@link #numberCandidateVotes}
     * 
     * @param protocolID
     * @param node
     */
    private void candidateSelectionAndVote(Node node, int protocolID) {
        Map<Node, Double> votings = new HashMap<Node, Double>();
        for (Node neighbour : getNeighbours()) {
            double similarity = calculateSimilarity(node, neighbour, protocolID);
            votings.put(new ComparableNode(neighbour), similarity);
        }
        List<Entry<Node, Double>> listVotings = new ArrayList<Entry<Node, Double>>(
                votings.entrySet());
        listVotings.stream().sorted((v, v2) -> v.getValue().compareTo(v2.getValue()))
                .limit(numberCandidateVotes).map(c -> c.getKey())
                .map(n -> (InterestProtocolImpl)n.getProtocol(protocolID))
                .forEach(p -> p.receiveCandidateVote());
    }

    /**
     * Every node, that crossed the {@link #representativeThreshold} is a
     * prospective representative peer.
     */
    private void potentialRepresentativeIndentification(Node node, int protocolID) {
        Collection<Node> candidates = new ArrayList<Node>(getNeighbours());
        candidates.add(node);
        List<Node> representativeCandidates = candidates.stream()
                .filter(n -> ((InterestProtocolImpl)n.getProtocol(protocolID))
                        .getCandidateVotes() >= representativeThreshold)
                .collect(Collectors.toList());
        double minDistance = Double.MAX_VALUE;
        Node representative = null;
        for (Node node2 : representativeCandidates) {
            double similarity = calculateSimilarity(node, node2, protocolID);
            if (1 - similarity < minDistance) {
                minDistance = 1 - similarity;
                representative = node2;
            }
        }
        if (representative != null) {
            // we just give one representative vote at the moment
            ((InterestProtocolImpl)representative.getProtocol(protocolID))
                    .receiveRepresentativeVote();
        }
    }

    /**
     * The candidate with the most votes will be selected as representative.
     */
    private void actualRepresentativeElection(Node node, int protocolID) {
        Collection<Node> candidates = new ArrayList<Node>(getNeighbours());
        candidates.add(node);
        Optional<Node> representative = candidates.stream()
                .filter(n -> ((InterestProtocolImpl)n.getProtocol(protocolID))
                        .getRepresentativeVotes() > 0)
                .max((n, n2) -> ((InterestProtocolImpl)n.getProtocol(protocolID))
                        .getRepresentativeVotes()
                        .compareTo(((InterestProtocolImpl)n2.getProtocol(protocolID))
                                .getRepresentativeVotes()));
        if (representative.isPresent()
                && !hasNodeDifferentReachableRepresentative(representative.get(), protocolID)) {
            this.setRepresentative(representative.get());
        } else if (representative.isPresent()
                && hasNodeDifferentReachableRepresentative(representative.get(), protocolID)) {
            InterestProtocol otherInterestProtocol = (InterestProtocol)representative.get()
                    .getProtocol(protocolID);
            this.setRepresentative(otherInterestProtocol.getRepresentative());
        } else {
            takeMostSimilarRepresentativeFromNeighbours(node, protocolID);
        }
    }

    private boolean hasNodeDifferentReachableRepresentative(Node node, int protocolID) {
        InterestProtocol interestProtocol = (InterestProtocol)node.getProtocol(protocolID);
        if (interestProtocol.getRepresentative() != null) {
            if (getNeighbours().contains(interestProtocol.getRepresentative())) {
                return true;
            }
        }
        return false;
    }

    private void takeMostSimilarRepresentativeFromNeighbours(Node node, int protocolID) {
        Map<Node, Double> similarities = getNeighbours().stream()
                .collect(Collectors.toMap(n -> n, n -> {
                    return calculateSimilarity(node, n, protocolID);
                }));
        Node potentialRepresentative = similarities.entrySet().stream().reduce((e1, e2) -> {
            if (e1.getValue() > e2.getValue()) {
                return e1;
            }
            return e2;
        }).map(e -> e.getKey()).get();
        InterestProtocol otherInterestProtocol = (InterestProtocol)potentialRepresentative
                .getProtocol(protocolID);
        // did the most similar node choose a representative that is reachable
        // from here?
        if (hasNodeDifferentReachableRepresentative(node, protocolID)) {
            setRepresentative(otherInterestProtocol.getRepresentative());
        } else {
            setRepresentative(potentialRepresentative);
        }
    }

    @Override
    public void startElection() {
        this.startElection = true;
    }

    @Override
    public Collection<Node> getNeighbours() {
        return interestCommunity;
    }

    @Override
    public void setNeighbours(Set<Node> neighbours) {
        this.interestCommunity = new ArrayList<>(neighbours);
    }

    @Override
    public boolean removeNeighbour(Node toRemove) {
        return interestCommunity.remove(toRemove);
    }

    @Override
    public void resetVotes() {
        candidateVotes = 0;
        representativeVotes = 0;
    }

    @Override
    public int degree() {
        return interestCommunity.size();
    }

    @Override
    public Node getNeighbor(int i) {
        return interestCommunity.get(i);
    }

    @Override
    public boolean addNeighbor(Node neighbour) {
        if (interestCommunity.contains(neighbour)) {
            return false;
        }
        return interestCommunity.add(neighbour);
    }

    @Override
    public boolean contains(Node neighbor) {
        return interestCommunity.contains(neighbor);
    }

    @Override
    public void pack() {
    }

    @Override
    public void onKill() {
    }

}
