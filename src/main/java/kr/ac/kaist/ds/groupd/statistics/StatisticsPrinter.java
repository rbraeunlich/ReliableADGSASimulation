
package kr.ac.kaist.ds.groupd.statistics;

import java.util.logging.Logger;

import peersim.core.CommonState;
import peersim.core.Control;

public class StatisticsPrinter implements Control {

    public StatisticsPrinter(String prefix) {
    }

    @Override
    public boolean execute() {
        if (CommonState.getPhase() == CommonState.POST_SIMULATION) {
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.info("Search Queries created:\t" + StatisticsCollector.searchQueriesCreated.getN());
            logger.info("Search Queries reached destination:\t" + StatisticsCollector.hopsToDestination.getN());
            logger.info("Search Queries reached source:\t" + StatisticsCollector.hopsBackToSource.getN());
            logger.info("Average hops to for returning:\t" + StatisticsCollector.hopsBackToSource.getAverage());
//            logger.info(
//                    "Groups to destination: " + StatisticsCollector.groupsToDestination.toString());
            logger.info("Gossiping needed for backtracking:\t"
                    + StatisticsCollector.gossipUsedForBacktracking.toString());
            logger.info("Unique queries send:\t" + StatisticsCollector.sentUniqueMessageIds);
            logger.info("Unique queries reached destination:\t" + StatisticsCollector.destinationUniqueMessageIds);
            logger.info("Unique queries reached source:\t" + StatisticsCollector.returnedUniqueMessageIds);
        }
        return false;
    }

}
