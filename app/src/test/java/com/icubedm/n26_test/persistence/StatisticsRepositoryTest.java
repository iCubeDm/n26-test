package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class StatisticsRepositoryTest {

    StatisticsRepository repository = new StatisticsRepository();

    @Test
    public void testAddNewTransaction() {

        long now = DateTimeUtil.nowEpochMilli();
        double amount = 10.0;

        Transaction tx = new Transaction(amount, now - 2000);
        Transaction tx1 = new Transaction(amount, now - 3000);
        Transaction tx2 = new Transaction(amount, now - 3000);
        Transaction oldTx = new Transaction(amount, now - 62000);
        repository.addNewTransaction(tx);
        repository.addNewTransaction(tx1);
        repository.addNewTransaction(tx2);
        repository.addNewTransaction(oldTx);

        Statistics stat = repository.getStatistics();
        assertEquals(3, stat.getCount());
        assertEquals(30.0, stat.getSum(), 0);
        assertEquals(10.0, stat.getAvg(), 0);
        assertEquals(10.0, stat.getMin(), 0);
        assertEquals(10.0, stat.getMax(), 0);
    }

    @Test
    public void testConcurrentWriteRequests_ShouldRegisterEveryTransaction() throws Exception {

        AtomicLong postCounter = new AtomicLong(0);
        final long now = DateTimeUtil.nowEpochMilli();
        for (int j = 0; j < 10; j++) {

            final int number = new Random().nextInt(1_000_000);

            System.out.println(String.format("Test with %s requests", number));

            final CountDownLatch latch = new CountDownLatch(number);
            ExecutorService executor = Executors.newFixedThreadPool(5);


            for (int i = 0; i < number; i++) {
                executor.execute(() -> {
                    long randomTimestamp = now - new Random().nextInt(30) * 1000;
                    if (Math.random() > 0) {
                        repository.addNewTransaction(new Transaction(10.0, randomTimestamp));
                        postCounter.getAndIncrement();
                    } else {
                        repository.getStatistics();
                    }
                    latch.countDown();
                });
            }

            latch.await();

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);

            Statistics stat = repository.getStatistics();
            assertEquals(postCounter.get(), stat.getCount());
            assertEquals(10.0 * postCounter.get(), stat.getSum(), 0);
            assertEquals(10.0, stat.getAvg(), 0);
            assertEquals(10.0, stat.getMin(), 0);
            assertEquals(10.0, stat.getMax(), 0);
        }
    }
}
