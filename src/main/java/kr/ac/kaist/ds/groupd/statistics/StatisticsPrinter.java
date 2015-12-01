package kr.ac.kaist.ds.groupd.statistics;

import java.util.logging.Logger;

import kr.ac.kaist.ds.groupd.search.SearchProtocol;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class StatisticsPrinter implements Control{
    
    private static final String PAR_SEARCH_PROTOCOL = "search";
    private int searchPid;
    
    public StatisticsPrinter(String prefix) {
        this.searchPid = Configuration.getPid(prefix + "." + PAR_SEARCH_PROTOCOL);
    }

    @Override
    public boolean execute() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Queries created: " +StatisticsCollector.searchQueriesCreated.toString());
        logger.info("Hops to destination: " +StatisticsCollector.hopsToDestination.toString());
        logger.info("Groups to destination: " + StatisticsCollector.groupsToDestination.toString());
        logger.info("Hops back to source: " +StatisticsCollector.hopsBackToSource.toString());
        logger.info("Gossiping needed for backtracking: " +StatisticsCollector.gossipUsedForBacktracking.toString());
        logger.info("Unique queries send: " + StatisticsCollector.sentUniqueMessageIds);
        logger.info("Unique queries returned: " + StatisticsCollector.returnedUniqueMessageIds);
        int numberOfFullQueues = collectNumberOfFullQueues();
        logger.info("Full queues: " + numberOfFullQueues);
        return false;
    }

    private int collectNumberOfFullQueues() {
        int fullOnes = 0;
        for(int i = 0; i < Network.size(); i++){
            SearchProtocol protocol = (SearchProtocol)Network.get(i).getProtocol(searchPid);
            if(protocol.isQueueFull()){
                fullOnes++;
            }
        }
        return fullOnes;
    }

}
