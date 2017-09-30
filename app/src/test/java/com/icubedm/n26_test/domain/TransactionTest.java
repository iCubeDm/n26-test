package com.icubedm.n26_test.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void testInit() {
        double amount = 10.0;
        int timestamp = 1000;
        Transaction tx = new Transaction(amount, timestamp);

        assertEquals(amount, tx.getAmount(), 0);
        assertEquals(timestamp, tx.getEpochMillis());
    }

    @Test
    public void testIsLateFor() {
        double amount = 10.0;
        int timestamp = 1000;
        Transaction tx = new Transaction(amount, timestamp);

        assertTrue(tx.isLateFor(timestamp + 60001));
        assertTrue(tx.isLateFor(timestamp + 60000));
        assertFalse(tx.isLateFor(timestamp + 59999));
    }
}
