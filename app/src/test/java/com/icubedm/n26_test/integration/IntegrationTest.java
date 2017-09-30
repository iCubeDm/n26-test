package com.icubedm.n26_test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
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

        String rawResponse = getStatistics();

        Statistics statistics = gson.fromJson(rawResponse, Statistics.class);

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

    private String getStatistics() throws Exception {
        return mockMvc.perform(get(getPath))
                .andExpect(status().is(200))
                .andReturn().getResponse()
                .getContentAsString();
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
