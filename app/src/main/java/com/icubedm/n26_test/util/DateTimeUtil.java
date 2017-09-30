package com.icubedm.n26_test.util;

import java.time.ZonedDateTime;

public class DateTimeUtil {

    public static long nowEpochMilli() {
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
}
