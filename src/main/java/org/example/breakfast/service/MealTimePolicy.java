package org.example.breakfast.service;

/**
 * Created by Robin on 25. 4. 2..
 * Description :
 */
public class MealTimePolicy {

    public static boolean isTodayBreakfastAlertTime() {
        return TimeChecker.isMorning();
    }

    public static boolean isTomorrowBreakfastAlertTime() {
        return TimeChecker.isEvening();
    }
}
