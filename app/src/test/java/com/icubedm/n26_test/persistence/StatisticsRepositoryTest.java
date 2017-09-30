package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsRepositoryTest {

    StatisticsRepository repository = new StatisticsRepository();

    @Test
    public void testAddNewTransaction() {

        long now = DateTimeUtil.nowEpochMilli();
        double amount = 10.0;

        Transaction tx = new Transaction(amount, now - 2000);
        Transaction tx1 = new Transaction(amount, now - 3000);
        Transaction tx2 = new Transaction(amount, now - 3000);
        Transaction oldTx = new Transaction(amount, now - 62000);
        repository.addNewTransaction(tx);
        repository.addNewTransaction(tx1);
        repository.addNewTransaction(tx2);
        repository.addNewTransaction(oldTx);

        Statistics stat = repository.getStatistics();
        assertEquals(3, stat.getCount());
        assertEquals(30.0, stat.getSum(), 0);
        assertEquals(10.0, stat.getAvg(), 0);
        assertEquals(10.0, stat.getMin(), 0);
        assertEquals(10.0, stat.getMax(), 0);
    }
}
