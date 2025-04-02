package org.example.breakfast;

import org.example.breakfast.service.FirestoreMenuService;
import org.example.breakfast.service.MealTimePolicy;
import org.example.breakfast.service.SlackBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Robin on 25. 3. 11.
 * Description : 조식 알림 서비스 Main Application
 */

public class DailyBreakfastNotifyApplication {
    private static final Logger log = LoggerFactory.getLogger(DailyBreakfastNotifyApplication.class);

    public static void main(String[] args) {
        if (MealTimePolicy.isTodayBreakfastAlertTime()) {
            sendTodayBreakfastMenuAlert();

        } else if (MealTimePolicy.isTomorrowBreakfastAlertTime()) {
            sendNextBreakfastMenuAlert();
        }
    }

    private static void sendTodayBreakfastMenuAlert() {
        try {
            FirestoreMenuService menuService = new FirestoreMenuService();
            String today = LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_LOCAL_DATE);

            menuService.fetchMenu(today).ifPresentOrElse(
                    menu -> {
                        String message = String.format(
                                "<!channel> \n"
                                        + "*📅 날짜:* %s\n\n"
                                        + "🍽 *오늘의 조식 메뉴* \n"
                                        + "━━━━━━━━━━━━━━━━━━\n"
                                        + "👉 %s\n"
                                        + "━━━━━━━━━━━━━━━━━━\n"
                                        + "😋 *Happy Snacking!* 🍪",
                                today, menu
                        );
                        SlackBotService.sendSlackMessage(message);
                    },
                    () -> log.error("오늘의 메뉴 정보가 없습니다.")
            );
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생", e);
        }
    }

    private static void sendNextBreakfastMenuAlert() {
        try {
            FirestoreMenuService menuService = new FirestoreMenuService();
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
            String today = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String nextDay = getNextDay().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String nextDayMessage;
            if (getNextDay().minusDays(1).isEqual(now)) {
                nextDayMessage = "내일의";
            } else {
                nextDayMessage = "다음";
            }
            menuService.fetchMenu(nextDay).ifPresentOrElse(
                    menu -> {
                        String message = String.format(
                                "<!channel> \n"
                                        + "*📅 날짜:* %s\n\n"
                                        + "⏰ *%s 조식 메뉴* \n"
                                        + "━━━━━━━━━━━━━━━━━━\n"
                                        + "👉 %s\n"
                                        + "━━━━━━━━━━━━━━━━━━\n",
                                today, nextDayMessage, menu
                        );
                        SlackBotService.sendSlackMessage(message);
                    },
                    () -> log.info("내일 메뉴 정보가 없습니다.")
            );
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생", e);
        }
    }

    private static LocalDate getNextDay() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate tomorrow = today.plusDays(1);
        if (today.getDayOfWeek() == DayOfWeek.FRIDAY) {
            LocalDate nextMonday = today.plusDays(3);
            return nextMonday;
        }
        return tomorrow;
    }
}
