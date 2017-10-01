package com.icubedm.n26_test.domain;

import java.util.Optional;

public class Statistics {

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

    public Statistics(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = Optional.of(max);
        this.min = Optional.of(min);
        this.count = count;
    }

    public Statistics() {
    }

    public Statistics(Transaction transaction) {
        this();
        this.addTransaction(transaction);
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

    /**
     * @param transaction new transaction
     * @return !!! Method returns mutated object !!!
     */
    public Statistics addTransaction(Transaction transaction) {
        this.count++;
        this.avg = (this.sum + transaction.getAmount()) / this.count;
        this.sum += transaction.getAmount();

        this.max = max.isPresent()
                ? Optional.of(Math.max(this.max.get(), transaction.getAmount()))
                : Optional.of(transaction.getAmount());

        this.min = min.isPresent()
                ? Optional.of(Math.min(this.min.get(), transaction.getAmount()))
                : Optional.of(transaction.getAmount());

        return this;
    }
}
