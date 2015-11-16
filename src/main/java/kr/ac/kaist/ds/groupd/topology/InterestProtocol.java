
package kr.ac.kaist.ds.groupd.topology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.SparseVector;

import peersim.core.Linkable;
import peersim.core.Node;
import peersim.core.Protocol;

public class InterestProtocol implements Protocol, Linkable {

	private List<Node> interestCommunity = new ArrayList<Node>();
    //private List<Node> interestCommunity = new ArrayList<Node>();

    private int candidateVotes = 0;

    private int representativeVotes;

    private Node representative;

    private SparseVector<Real> interestVector;

    public InterestProtocol(String prefix) {
    }

    public SparseVector<Real> getInterest() {
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

    public void receiveCandidateVote() {
        candidateVotes++;
    }

    public int getCandidateVotes() {
        return candidateVotes;
    }

    public void receiveRepresentativeVote() {
        representativeVotes++;
    }

    public Integer getRepresentativeVotes() {
        return representativeVotes;
    }

    public void setRepresentative(Node node) {
        this.representative = node;
    }

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

    public void setInterestVector(SparseVector<Real> interestVector) {
        this.interestVector = interestVector;
    }
}
