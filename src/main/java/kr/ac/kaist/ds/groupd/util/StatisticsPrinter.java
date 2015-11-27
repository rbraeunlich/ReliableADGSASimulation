package kr.ac.kaist.ds.groupd.util;

import java.util.logging.Logger;

import peersim.core.Control;

public class StatisticsPrinter implements Control{
    
    public StatisticsPrinter(String prefix) {
    }

    @Override
    public boolean execute() {
        Logger.getLogger(this.getClass().getName()).info("Queries created: " +StatisticsCollector.searchQueriesCreated.toString());
        Logger.getLogger(this.getClass().getName()).info("Hops to destination: " +StatisticsCollector.hopsToDestination.toString());
        Logger.getLogger(this.getClass().getName()).info("Groups to destination: " + StatisticsCollector.groupsToDestination.toString());
        Logger.getLogger(this.getClass().getName()).info("Hops back to source: " +StatisticsCollector.hopsBackToSource.toString());
        return false;
    }

}
