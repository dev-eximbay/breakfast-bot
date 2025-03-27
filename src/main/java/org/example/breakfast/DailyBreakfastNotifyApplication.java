package org.example.breakfast;

import org.example.breakfast.service.FirestoreMenuService;
import org.example.breakfast.service.SlackBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        try {
            FirestoreMenuService menuService = new FirestoreMenuService();
            String today = LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.println("today = " + today);

            menuService.fetchMenu(today).ifPresentOrElse(
                    menu -> {
                        String message = String.format(
                                "*📅 날짜:* %s\n\n"
                                        + "🍽 *오늘의 조식 메뉴* \n"
                                        + "━━━━━━━━━━━━━━━━━━\n"
                                        + "👉 %s\n"
                                        + "━━━━━━━━━━━━━━━━━━\n"
                                        + "😋 *Happy Snacking!* 🍪",
                                today, menu
                        );
                        log.info("Slack으로 보낼 메시지: {}", message);
                        SlackBotService.sendSlackMessage(message);
                    },
                    () -> log.error("오늘의 메뉴 정보가 없습니다.")
            );
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생", e);
        }
    }
}
