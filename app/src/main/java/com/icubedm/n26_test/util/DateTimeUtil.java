package com.icubedm.n26_test.util;

import java.time.ZonedDateTime;

/**
 * author: dmitry.yakubovsky
 * date:   29.09.17
 */
public class DateTimeUtil {

    public static long nowEpochMilli() {
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
}
