package com.icubedm.n26_test.rest;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.StatisticsService;
import com.icubedm.n26_test.domain.Transaction;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation(value = "Posts a new transaction to the system", tags = {"Service"})
    @RequestMapping(value = "/transactions", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> postTransaction(@RequestBody Transaction transaction) {
        if (!statisticsService.postTransaction(transaction))
            return new ResponseEntity<>(NO_CONTENT);

        return new ResponseEntity<>(new Message("TRANSACTION PROCESSED"), CREATED);
    }

    @ApiOperation(value = "Returns statistics for the past 60 seconds", tags = {"Service"})
    @RequestMapping(value = "/statistics", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Statistics> getStatistics() {
        Statistics statistics = statisticsService.getStatistics();

        return new ResponseEntity<>(statistics, OK);
    }


    private static class Message {
        String msg;

        Message(String msg) {
            this.msg = msg;
        }
    }
}
