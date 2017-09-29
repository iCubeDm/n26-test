package com.icubedm.n26_test.domain;

/**
 * author: dmitry.yakubovsky
 * date:   29.09.17
 */
public class StatisticsResult {

    /**
     * is a double specifying
     * the total sum of transaction
     * value in the last 60 seconds
     */
    public double sum;

    /**
     * is a double specifying
     * the average amount of
     * transaction value in the
     * last 60 seconds
     */
    public double avg;

    /**
     * is a double specifying
     * single highest transaction
     * value in the last 60 seconds
     */
    public double max;

    /**
     * is a double specifying
     * single lowest transaction value
     * in the last 60 seconds
     */
    public double min;

    /**
     * is a long specifying
     * the total number of transactions
     * happened in the last 60 seconds
     */
    public long count;
}
