package com.icubedm.n26_test.persistence;

import com.icubedm.n26_test.domain.Statistics;
import com.icubedm.n26_test.domain.Transaction;
import com.icubedm.n26_test.util.DateTimeUtil;
import jdk.nashorn.internal.runtime.options.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.icubedm.n26_test.util.DateTimeUtil.nowEpochMilli;
import static java.util.Objects.nonNull;

@Component
public class StatisticsRepository {

    static final Logger logger = LoggerFactory.getLogger(StatisticsRepository.class);

    /**
     * While bean in spring boot by default is in the singleton scope, we don't need to have storage to be static
     */
    private final List<AtomicReference<TimestampedStatistics>> storage = Stream.generate(() -> TimestampedStatistics.EMPTY)
            .map(AtomicReference::new)
            .limit(60)
            .collect(Collectors.toList());

    public void addNewTransaction(Transaction transaction) {
        int bucket = (int) ((transaction.getEpochMillis() / 1000) % 60);
        AtomicReference<TimestampedStatistics> atomic = storage.get(bucket);
        atomic.accumulateAndGet(new TimestampedStatistics(transaction), TimestampedStatistics::merge);
    }

    public Optional<Statistics> getStatistics() {
        long second = DateTimeUtil.nowEpochMilli() / 1000;
        int bucket = (int) (second % 60);
        Optional<Statistics> result = IntStream.rangeClosed(bucket + 1, bucket + 60)
                .map(i -> i % 60)
                .mapToObj(storage::get)
                .map(AtomicReference::get)
                .filter(ts -> ts.getSecond() > second - 60)
                .map(TimestampedStatistics::getStat)
                .filter(Statistics::isNotEmpty)
                .reduce(Statistics::merge);

        return result;
    }

}
