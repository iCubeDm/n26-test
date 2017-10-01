package com.icubedm.n26_test.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    @Test
    public void testInitialState() {
        Statistics stat = new Statistics();

        assertEquals(0, stat.getCount());
        assertEquals(0.0, stat.getSum(), 0);
        assertEquals(0.0, stat.getAvg(), 0);
        assertEquals(0.0, stat.getMin(), 0);
        assertEquals(0.0, stat.getMax(), 0);
    }

    @Test
    public void testCreateWithCustomState() {
        double sum = 100.0;
        double avg = 53.0;
        double max = 123.0;
        double min = 12.0;
        long count = 23;
        Statistics stat = new Statistics(sum, avg, max, min, count);

        assertEquals(count, stat.getCount());
        assertEquals(sum, stat.getSum(), 0);
        assertEquals(avg, stat.getAvg(), 0);
        assertEquals(min, stat.getMin(), 0);
        assertEquals(max, stat.getMax(), 0);
    }

    @Test
    public void testStateMutation() {
        Statistics stat = new Statistics();

        Transaction tx1 = new Transaction(10.0, 1000);

        stat.addTransaction(tx1);
        assertEquals(1, stat.getCount());
        assertEquals(10.0, stat.getSum(), 0);
        assertEquals(10.0, stat.getAvg(), 0);
        assertEquals(10.0, stat.getMin(), 0);
        assertEquals(10.0, stat.getMax(), 0);

        Transaction tx2 = new Transaction(12.0, 1000);

        stat.addTransaction(tx2);
        assertEquals(2, stat.getCount());
        assertEquals(22.0, stat.getSum(), 0);
        assertEquals(11.0, stat.getAvg(), 0);
        assertEquals(10.0, stat.getMin(), 0);
        assertEquals(12.0, stat.getMax(), 0);
    }
}
