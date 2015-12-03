
package kr.ac.kaist.ds.groupd.statistics;

import java.util.HashSet;
import java.util.Set;

import peersim.util.IncrementalStats;

public class StatisticsCollector {

    static IncrementalStats hopsToDestination = new IncrementalStats();

    static IncrementalStats groupsToDestination = new IncrementalStats();

    static IncrementalStats hopsBackToSource = new IncrementalStats();

    static IncrementalStats searchQueriesCreated = new IncrementalStats();
    
    static IncrementalStats gossipUsedForBacktracking = new IncrementalStats();
    
    static Set<Integer> sentUniqueMessageIds = new HashSet<>();

    static Set<Integer> returnedUniqueMessageIds = new HashSet<>();

    static Set<Integer> destinationUniqueMessageIds = new HashSet<>();

    public static void arrivedAtDestination(int nrOfHops, int nrOfGroups, int id) {
        hopsToDestination.add(nrOfHops);
        groupsToDestination.add(nrOfGroups);
        destinationUniqueMessageIds.add(id);
    }

    public static void arrivedBackAtSource(int nrOfHops, int messageId) {
        hopsBackToSource.add(nrOfHops);
        returnedUniqueMessageIds.add(messageId);
    }

    public static void queryCreated(int messageId) {
        sentUniqueMessageIds.add(messageId);
        searchQueriesCreated.add(1);
    }

    public static void fellBackToGossiping(){
    	gossipUsedForBacktracking.add(1);
    }
}
