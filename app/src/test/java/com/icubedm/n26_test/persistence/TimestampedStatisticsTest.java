package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimestampedStatisticsTest {

    @Test
    public void testCreateWithTransaction() {

        long now = DateTimeUtil.nowEpochMilli();
        Transaction tx = new Transaction(12.0, now);

        TimestampedStatistics timestampedStatistics = new TimestampedStatistics(tx);
        Statistics statistics = timestampedStatistics.getStatistics();

        assertEquals(12.0, statistics.getSum(), 0);
        assertEquals(12.0, statistics.getAvg(), 0);
        assertEquals(1, statistics.getCount(), 0);
        assertEquals(12.0, statistics.getMax(), 0);
        assertEquals(12.0, statistics.getMin(), 0);
    }

    @Test
    public void testMergeWith() {

        long now = DateTimeUtil.nowEpochMilli();

        Transaction tx = new Transaction(12.0, now);
        TimestampedStatistics timestampedStatistics = new TimestampedStatistics(tx);

        Transaction tx1 = new Transaction(10.0, now);
        TimestampedStatistics timestampedStatistics1 = new TimestampedStatistics(tx1);

        Statistics statistics = timestampedStatistics.merge(timestampedStatistics1).getStatistics();

        assertEquals(22.0, statistics.getSum(), 0);
        assertEquals(11.0, statistics.getAvg(), 0);
        assertEquals(2, statistics.getCount(), 0);
        assertEquals(12.0, statistics.getMax(), 0);
        assertEquals(10.0, statistics.getMin(), 0);
    }
}
