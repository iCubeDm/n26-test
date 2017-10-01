package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;

public class TimestampedStatistics {

    private long timestamp;
    private Statistics statistics;

    TimestampedStatistics(long epochMillis, Statistics statistics) {
        this.timestamp = epochMillis / 1000;
        this.statistics = statistics;
    }

    void addTransaction(Transaction transaction) {
        this.statistics.addTransaction(transaction);
    }

    public Statistics getStatistics() {
        return statistics;
    }

    boolean isLateFor(long epochMillis) {
        long l = epochMillis / 1000;
        return l - this.timestamp >= 60;
    }

    boolean isSameSecond(long epochMillis) {
        return this.timestamp == (epochMillis / 1000);
    }

    TimestampedStatistics mergeWith(TimestampedStatistics anotherStatistics) {
        Statistics anotherStat = anotherStatistics.getStatistics();

        long count = this.statistics.getCount() + anotherStat.getCount();
        double sum = this.statistics.getSum() + anotherStat.getSum();
        double avg = sum / count;
        double max = Math.max(statistics.getMax(), anotherStat.getMax());
        double min = Math.min(statistics.getMin(), anotherStat.getMin());

        return new TimestampedStatistics(DateTimeUtil.nowEpochMilli(), new Statistics(sum, avg, max, min, count));
    }
}
