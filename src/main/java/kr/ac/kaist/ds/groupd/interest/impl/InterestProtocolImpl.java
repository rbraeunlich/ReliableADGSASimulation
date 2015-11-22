package kr.ac.kaist.ds.groupd.interest.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import peersim.core.Linkable;
import peersim.core.Node;
import peersim.core.Protocol;

public class InterestProtocolImpl implements Protocol, Linkable {

	private List<Node> interestCommunity = new ArrayList<Node>();
    //private List<Node> interestCommunity = new ArrayList<Node>();

    private int candidateVotes = 0;

    private int representativeVotes;

    private Node representative;

	private double[] interestVector;
	private double magnitude = 0.0;


	public InterestProtocolImpl(String prefix) {
    }

	public double[] getInterest() {
        return interestVector;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<Node> getInterestCommunity() {
        return interestCommunity;
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
	 * Sets candidate and representative votings to zero.
	 * Has to be called after an election finishes.
	 */
    public void resetVotes() {
        candidateVotes = 0;
        representativeVotes = 0;
    }

    public Node getRepresentative() {
        return representative;
    }

    @Override
    public void onKill() {
        // FIXME
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
        if (contains(neighbour)) {
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

	public void setInterestVector(double[] interestVector) {
        this.interestVector = interestVector;
		this.magnitude = calculateMagnitude(this.interestVector);
    }
	
	/**
	 * Returns the <a href="https://en.wikipedia.org/wiki/Magnitude_%28mathematics%29#Euclidean_vector_space">magnitude</a>
	 * of the vector that is returned by {@link #getInterest()}. The magnitude is precomputed because it is needed quite often.
	 * @return
	 */
	public double getMagnitude(){
		return magnitude;
	}
	
	/**
	 * Calculates the <a href="https://en.wikipedia.org/wiki/Magnitude_%28mathematics%29#Euclidean_vector_space">magnitude</a>
	 * of a vector.
	 * @param vector
	 * @return
	 */
	private double calculateMagnitude(double[] vector) {
		double sum = 0.0;
		for(int i = 0; i < vector.length; i++){
			sum += Math.pow(vector[i], 2.0);
		}
		return Math.sqrt(sum);
	}
}
