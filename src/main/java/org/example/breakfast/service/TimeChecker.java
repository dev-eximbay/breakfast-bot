package org.example.breakfast.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Created by Robin on 25. 4. 2.
 * Description :
 */
public class TimeChecker {
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    public static boolean isMorning() {
        LocalTime now = LocalTime.now(KOREA_ZONE_ID);
        return now.isBefore(LocalTime.NOON);
    }

    public static boolean isEvening() {
        LocalTime now = LocalTime.now(KOREA_ZONE_ID);
        return now.isAfter(LocalTime.NOON);
    }
}
