package com.icubedm.n26_test.domain;

import java.util.Optional;

public class Statistics {

    public static Statistics EMPTY = new Statistics(0, 0, 0, 0);

    /**
     * is a double specifying
     * the total sum of transaction
     * value in the last 60 seconds
     */
    private double sum = 0;

    /**
     * is a double specifying
     * the average amount of
     * transaction value in the
     * last 60 seconds
     */
    private double avg = 0;

    /**
     * is a double specifying
     * single highest transaction
     * value in the last 60 seconds
     */
    private Optional<Double> max = Optional.empty();

    /**
     * is a double specifying
     * single lowest transaction value
     * in the last 60 seconds
     */
    private Optional<Double> min = Optional.empty();

    /**
     * is a long specifying
     * the total number of transactions
     * happened in the last 60 seconds
     */
    private long count = 0;

    public Statistics(double sum, double max, double min, long count) {
        this.sum = sum;
        this.avg = sum / count;
        this.max = Optional.of(max);
        this.min = Optional.of(min);
        this.count = count;
    }

    public Statistics() {
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max.orElse(0.0);
    }

    public double getMin() {
        return min.orElse(0.0);
    }

    public long getCount() {
        return count;
    }

    public Statistics merge(Statistics other) {
        if (other == EMPTY) {
            return this;
        } else if (this == EMPTY) {
            return other;
        } else {
            return new Statistics(
                    sum + other.sum,
                    max.isPresent()
                            ? Math.max(this.getMax(), other.getMax())
                            : other.getMax(),
                    min.isPresent()
                            ? Math.min(this.getMin(), other.getMin())
                            : other.getMin(),
                    count + other.count);
        }
    }

    public boolean isNotEmpty() {
        return this != EMPTY;
    }
}
