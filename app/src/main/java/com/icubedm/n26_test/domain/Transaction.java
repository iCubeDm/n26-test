package com.icubedm.n26_test.domain;

public class Transaction {

    private final double amount;
    private final long timestamp;

    public Transaction(double amount,
                       int timestamp) {

        this.amount = amount;
        this.timestamp = timestamp;
    }

    public boolean isLate(long currentEpochMillis) { return (currentEpochMillis - timestamp) / 1000 > 60; }

    public double getAmount() { return amount; }
}
