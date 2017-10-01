package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;

public class TimestampedStatistics {

    public static TimestampedStatistics EMPTY = new TimestampedStatistics(-1, Statistics.EMPTY);

    private final long second;
    private final Statistics stat;

    private TimestampedStatistics(long second, Statistics stat) {
        this.second = second;
        this.stat = stat;
    }

    public TimestampedStatistics(Transaction transaction) {
        this(transaction.getEpochMillis() / 1000, new Statistics(transaction.getAmount(), transaction.getAmount(), transaction.getAmount(), 1));
    }

    public long getSecond() {
        return second;
    }

    public Statistics getStat() {
        return stat;
    }

    public TimestampedStatistics merge(TimestampedStatistics other) {
        if (second > other.second) {
            return this;
        } else if (second < other.second) {
            return other;
        } else {
            return new TimestampedStatistics(second, stat.merge(other.stat));
        }
    }
}
