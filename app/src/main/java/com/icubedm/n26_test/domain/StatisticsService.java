package com.icubedm.n26_test.domain;

import com.icubedm.n26_test.persistence.StatisticsRepository;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    public boolean postTransaction(Transaction transaction) {

        long now = DateTimeUtil.nowEpochMilli();

        if(transaction.isLate(now))
            return false;

        statisticsRepository.addNewTransaction(transaction);
        return true;
    }

    public StatisticsResult getStatistics() {

        return statisticsRepository.getStatistics();
    }
}
