package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.StatisticsResult;
import com.icubedm.n26_test.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class StatisticsRepository {

    final int[] array = new int[60];

    public void addNewTransaction(Transaction transaction) {

    }

    public StatisticsResult getStatistics() {

        return null;
    }
}
