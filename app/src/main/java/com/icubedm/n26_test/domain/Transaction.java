package com.icubedm.n26_test.domain;

import com.google.common.base.MoreObjects;

public class Transaction {

    private double amount;
    private long timestamp;

    /**
     * used for the serialization purposes
     */
    public Transaction() {}

    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    boolean isLateFor(long currentEpochMillis) {
        long l = currentEpochMillis - timestamp;
        return l >= 60000;
    }

    double getAmount() { return amount; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("amount", amount)
                .add("timestamp", timestamp)
                .toString();
    }

    public long getEpochMillis() {
        return timestamp;

    }
}
