package org.example.breakfast.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Created by Robin on 25. 4. 5.
 * Description : 슬랙 메세지 포맷
 */
public class SlackMessageFormatter {
    private static final String MENTION_CHANNEL = "<!channel>";

    public static String formatTodayMenu(LocalDate baseDate, String menu) {
        String baseDateFormat = baseDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        return String.format(
                "%s \n"
                        + "*📅 날짜:* %s\n\n"
                        + "🍽 *오늘의 조식 메뉴* \n"
                        + "━━━━━━━━━━━━━━━━━━\n"
                        + "👉 %s\n"
                        + "━━━━━━━━━━━━━━━━━━\n"
                        + "😋 *Happy Snacking!* 🍪",
                MENTION_CHANNEL, baseDateFormat, menu
        );
    }

    public static String formatNextMenu(LocalDate alertDate, String menu) {
        String alertDateFormat = alertDate.format(DateTimeFormatter.ofPattern("MM-dd"));
        String displayName = alertDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        return String.format(
                "%s \n\n"
                        + "⏰ *%s(%s) 조식 메뉴* \n"
                        + "━━━━━━━━━━━━━━━━━━\n"
                        + "👉 %s\n"
                        + "━━━━━━━━━━━━━━━━━━\n"
                        + "😋 *Happy Snacking!* 🍪",
                MENTION_CHANNEL, alertDateFormat, displayName, menu
        );
    }
}
