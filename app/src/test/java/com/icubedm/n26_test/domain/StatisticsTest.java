package com.icubedm.n26_test.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        double sum = 1000.0;
        double avg = 40.0;
        double max = 123.0;
        double min = 12.0;
        long count = 25;
        Statistics stat = new Statistics(sum, max, min, count);

        assertEquals(count, stat.getCount());
        assertEquals(sum, stat.getSum(), 0);
        assertEquals(avg, stat.getAvg(), 0);
        assertEquals(min, stat.getMin(), 0);
        assertEquals(max, stat.getMax(), 0);
    }

    @Test
    public void testMerge() {
        Statistics stat = new Statistics(10.0, 10.0, 10.0, 1);
        Statistics stat1 = new Statistics(12.0, 12.0, 12.0, 1);

        Statistics result = stat.mergeWith(stat1);

        assertEquals(2, result.getCount());
        assertEquals(22.0, result.getSum(), 0);
        assertEquals(11.0, result.getAvg(), 0);
        assertEquals(10.0, result.getMin(), 0);
        assertEquals(12.0, result.getMax(), 0);
    }

    @Test
    public void testMergeWithEmpty() {
        Statistics stat = new Statistics(10.0, 10.0, 10.0, 1);

        Statistics result = stat.mergeWith(Statistics.EMPTY);

        assertEquals(1, result.getCount());
        assertEquals(10.0, result.getSum(), 0);
        assertEquals(10.0, result.getAvg(), 0);
        assertEquals(10.0, result.getMin(), 0);
        assertEquals(10.0, result.getMax(), 0);

        result = Statistics.EMPTY.mergeWith(stat);

        assertEquals(1, result.getCount());
        assertEquals(10.0, result.getSum(), 0);
        assertEquals(10.0, result.getAvg(), 0);
        assertEquals(10.0, result.getMin(), 0);
        assertEquals(10.0, result.getMax(), 0);
    }
}
