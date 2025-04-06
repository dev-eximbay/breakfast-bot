package org.example.breakfast.time.checker;

import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Created by Robin on 25. 4. 2.
 * Description :
 */
public class TimeChecker {

    public static boolean isMorning(LocalTime time) {
        return time.isBefore(LocalTime.NOON);
    }

    public static boolean isEvening(LocalTime time) {
        return time.isAfter(LocalTime.NOON);
    }
}
