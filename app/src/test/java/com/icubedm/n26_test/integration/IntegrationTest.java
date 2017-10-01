package com.icubedm.n26_test.integration;

import com.google.gson.Gson;
import com.icubedm.n26_test.Application;
import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class IntegrationTest {

    static Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private String postPath = "/transactions";
    private String getPath = "/statistics";
    private Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSinglePostAndGet() throws Exception {
        long now = DateTimeUtil.nowEpochMilli();

        Transaction tx = new Transaction(200.0, now);

        logger.info("Calling POST /transactions");
        String response = postTransaction(tx, 201);

        logger.info("Service response 201: {}", response);

        logger.info("Calling GET /statistics");

        Statistics statistics = getStatistics();

        logger.info("Received statistics from the service. Starting Assertions");
        assertEquals(1, statistics.getCount());
        assertEquals(200.0, statistics.getSum(), 0);
        assertEquals(200.0, statistics.getAvg(), 0);
        assertEquals(200.0, statistics.getMin(), 0);
        assertEquals(200.0, statistics.getMax(), 0);

        logger.info("Trying to post late transaction");
        Transaction lateTx = new Transaction(100, now - 100000);

        response = postTransaction(lateTx, 204);
        assertEquals("{\"msg\":\"TRANSACTION IS LATE\"}", response);
    }

    @Test
    public void testBigParallelPosting() throws Exception {

        long now = DateTimeUtil.nowEpochMilli();
        final int number = 2000;

        final CountDownLatch latch = new CountDownLatch(number);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < number; i++) {
            executor.execute(() -> {
                Transaction transaction = new Transaction(10.0, now);
                try {
                    postTransaction(transaction, 201);
                } catch (Exception e) {
                    logger.error("Exception occured: {}", e.getMessage());
                }
                latch.countDown();
            });
        }

        latch.await();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        Statistics statistics = getStatistics();

        assertEquals(2000, statistics.getCount());
        assertEquals(20000.0, statistics.getSum(), 0);
        assertEquals(10.0, statistics.getAvg(), 0);
        assertEquals(10.0, statistics.getMin(), 0);
        assertEquals(10.0, statistics.getMax(), 0);
    }

    private Statistics getStatistics() throws Exception {

        String raw = mockMvc.perform(get(getPath))
                .andExpect(status().is(200))
                .andReturn().getResponse()
                .getContentAsString();
        return gson.fromJson(raw, Statistics.class);
    }

    private String postTransaction(Transaction tx, int expectedCode) throws Exception {
        return mockMvc.perform(post(postPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(tx)))
                .andExpect(status().is(expectedCode))
                .andReturn().getResponse()
                .getContentAsString();
    }
}
