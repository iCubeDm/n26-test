package com.icubedm.n26_test.domain;

import com.icubedm.n26_test.persistence.StatisticsRepository;
import com.icubedm.n26_test.util.DateTimeUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest {

    @Mock
    private StatisticsRepository repository;

    @InjectMocks
    private StatisticsService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostTransaction_SuccessfulTimestampValidation() {
        double amount = 10.0;
        long now = DateTimeUtil.nowEpochMilli();
        Transaction tx = new Transaction(amount, now);

        doNothing().when(repository).addNewTransaction(tx);

        boolean success = service.postTransaction(tx);

        assertTrue(success);
    }

    @Test
    public void testPostTransaction_FailedTimestampValidation() {
        double amount = 10.0;
        long now = DateTimeUtil.nowEpochMilli() - 60000;
        Transaction tx = new Transaction(amount, now);

        boolean success = service.postTransaction(tx);

        verify(repository, never()).addNewTransaction(any());
        assertFalse(success);
    }

//    @Test
//    public void testGetStatistics() {
//
//        Statistics stat = new Statistics();
//
//        when(repository.getStatistics()).thenReturn(stat);
//
//        Statistics result = service.getStatistics();
//
//        assertEquals(stat, result);
//    }

}
