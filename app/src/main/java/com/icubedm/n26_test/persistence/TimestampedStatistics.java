package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;

public class TimestampedStatistics {

    public static TimestampedStatistics EMPTY = new TimestampedStatistics(-1, Statistics.EMPTY);

    private final long second;
    private final Statistics statistics;

    private TimestampedStatistics(long second, Statistics statistics) {
        this.second = second;
        this.statistics = statistics;
    }

    public TimestampedStatistics(Transaction transaction) {
        this(transaction.getEpochMillis() / 1000,
             new Statistics(transaction.getAmount(), transaction.getAmount(), transaction.getAmount(), 1)
        );
    }

    public long getSecond() {
        return second;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public TimestampedStatistics merge(TimestampedStatistics other) {
        if (second > other.second) {
            return this;
        } else if (second < other.second) {
            return other;
        } else {
            return new TimestampedStatistics(second, statistics.mergeWith(other.statistics));
        }
    }
}
