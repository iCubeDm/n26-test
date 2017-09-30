package com.icubedm.n26_test.domain;

import com.icubedm.n26_test.persistence.StatisticsRepository;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticsService {

    static Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private StatisticsRepository statisticsRepository;

    public boolean postTransaction(Transaction transaction) {

        long now = DateTimeUtil.nowEpochMilli();

        logger.info("Received a new transaction {}", transaction);

        if(transaction.isLateFor(now)){
            logger.warn("Transaction {} is older than 60 second. Skipping.");
            return false;
        }

        statisticsRepository.addNewTransaction(transaction);
        return true;
    }

    public Statistics getStatistics() {
        logger.info("Requested application statistics for past 60 seconds");
        return statisticsRepository.getStatistics();
    }
}

