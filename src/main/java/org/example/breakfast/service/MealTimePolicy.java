package org.example.breakfast.service;

import org.example.breakfast.time.TimeProvider;
import org.example.breakfast.time.checker.DateChecker;
import org.example.breakfast.time.checker.TimeChecker;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Robin on 25. 4. 2.
 * Description : 아침 식사 알림 정책
 */
public class MealTimePolicy {
    private final TimeProvider timeProvider;

    public MealTimePolicy(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public boolean shouldSendTodayBreakfastAlert() {
        LocalDate currentDate = timeProvider.nowDate();
        LocalTime currentTime = timeProvider.nowTime();
        return TimeChecker.isMorning(currentTime) && DateChecker.isWeekday(currentDate);
    }

    public boolean shouldSendNextBreakfastAlert() {
        LocalDate currentDate = timeProvider.nowDate();
        LocalTime currentTime = timeProvider.nowTime();
        return TimeChecker.isEvening(currentTime) && DateChecker.isWeekday(currentDate);
    }
}
