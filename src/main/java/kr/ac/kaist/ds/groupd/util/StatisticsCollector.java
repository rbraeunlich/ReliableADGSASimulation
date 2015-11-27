
package kr.ac.kaist.ds.groupd.util;

import peersim.util.IncrementalStats;

public class StatisticsCollector {

    static IncrementalStats hopsToDestination = new IncrementalStats();

    static IncrementalStats groupsToDestination = new IncrementalStats();

    static IncrementalStats hopsBackToSource = new IncrementalStats();

    static IncrementalStats searchQueriesCreated = new IncrementalStats();

    public static void arrivedAtDestination(int nrOfHops, int nrOfGroups) {
        hopsToDestination.add(nrOfHops);
        groupsToDestination.add(nrOfGroups);
    }

    public static void arrivedBackAtSource(int nrOfHops) {
        hopsBackToSource.add(nrOfHops);
    }

    public static void queryCreated() {
        searchQueriesCreated.add(1);
    }

}
