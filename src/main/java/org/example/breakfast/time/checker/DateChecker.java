package org.example.breakfast.time.checker;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created by Robin on 25. 4. 5.
 * Description :
 */
public class DateChecker {

    public static boolean isWeekday(LocalDate day) {
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    public static boolean isWeekend(LocalDate day) {
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
