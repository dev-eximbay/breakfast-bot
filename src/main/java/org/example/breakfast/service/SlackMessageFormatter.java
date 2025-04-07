package org.example.breakfast.service;

import org.jetbrains.annotations.NotNull;

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
    public static final TextStyle TEXT_STYLE_SHORT = TextStyle.SHORT;
    public static final Locale LOCALE_KOREAN = Locale.KOREAN;

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
        String alertDateFormat = getAlertDateFormat(alertDate);
        String displayName = getDisplayName(alertDate);

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

    @NotNull
    private static String getAlertDateFormat(LocalDate alertDate) {
        return alertDate.format(DateTimeFormatter.ofPattern("MM-dd"));
    }

    @NotNull
    private static String getDisplayName(LocalDate alertDate) {
        return alertDate.getDayOfWeek().getDisplayName(TEXT_STYLE_SHORT, LOCALE_KOREAN);
    }
}
