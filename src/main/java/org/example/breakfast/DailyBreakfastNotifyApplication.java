package org.example.breakfast;

import org.example.breakfast.config.AppContext;
import org.example.breakfast.service.*;

/**
 * Created by Robin on 25. 3. 11.
 * Description : 조식 알림 서비스 Main Application.
 */

public class DailyBreakfastNotifyApplication {

    public static void main(String[] args) {
        AppContext appContext = new AppContext();
        MealTimePolicy mealTimePolicy = appContext.mealTimePolicy();
        BreakfastNotifier notifier = appContext.breakfastNotifier();

        if (mealTimePolicy.shouldSendTodayBreakfastAlert()) {
            notifier.sendTodayBreakfastMenuAlert();
        } else if (mealTimePolicy.shouldSendNextBreakfastAlert()) {
            notifier.sendNextBreakfastMenuAlert();
        }
    }
}
