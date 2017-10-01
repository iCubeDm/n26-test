package com.icubedm.n26_test.domain;

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
    private double max = 0;

    /**
     * is a double specifying
     * single lowest transaction value
     * in the last 60 seconds
     */
    private double min = 0;

    /**
     * is a long specifying
     * the total number of transactions
     * happened in the last 60 seconds
     */
    private long count = 0;

    public Statistics(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
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
        return max;
    }

    public double getMin() {
        return min;
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
        this.max = Math.max(this.max, transaction.getAmount());
        if (this.min == 0 || this.min > transaction.getAmount()) this.min = transaction.getAmount();
        return this;
    }
}
