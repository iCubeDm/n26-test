package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.icubedm.n26_test.util.DateTimeUtil.nowEpochMilli;
import static java.util.Objects.nonNull;

@Component
public class StatisticsRepository {

    static final Logger logger = LoggerFactory.getLogger(StatisticsRepository.class);

    /**
     * While bean in spring boot by default is in the singleton scope, we don't need to have storage to be static
     */
    List<TimestampedStatistics> storage = Collections.synchronizedList(new ArrayList<TimestampedStatistics>(60));

    public void addNewTransaction(Transaction transaction) {
        cleanup();
        for (TimestampedStatistics stat : storage) {
            if (nonNull(stat) && stat.isSameSecond(transaction.getEpochMillis())) {
                stat.addTransaction(transaction);
                return;
            }
        }
        storage.add(new TimestampedStatistics(
                transaction.getEpochMillis(),
                new Statistics().addTransaction(transaction))
        );
    }

    public Statistics getStatistics() {
        cleanup();
        return storage.stream()
                .filter(Objects::nonNull)
                .reduce(TimestampedStatistics::mergeWith)
                .orElse(new TimestampedStatistics(nowEpochMilli(), new Statistics()))
                .getStatistics();
    }

    private long cleanup() {
        long now = nowEpochMilli();
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i) != null && storage.get(i).isLateFor(now))
                storage.set(i, null);
        }
        logger.info("Storage cleanup is done for {}", now);
        return now;
    }
}
