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
        String link = "https://dev-eximbay.github.io/breakfast-calendar";

        return String.format(
                "%s \n\n"
                        + "⏰ *%s(%s) 조식 메뉴*  🍽️\n"
                        + "━━━━━━━━━━━━━━━━━━\n"
                        + "👉 %s\n"
                        + "━━━━━━━━━━━━━━━━━━\n\n"
                        + "📅 *<%s|(신규 기능)조식 달력 보러가기!>* \n"
                        + "━━━━━━━━━━━━━━━━━━\n"
                        + "\uD83D\uDC68\u200D\uD83D\uDCBB *Thanks to <@U061TCFL3NX>!* 이제 조식 달력을 웹으로도 볼 수 있어요!",
                MENTION_CHANNEL, alertDateFormat, displayName, menu, link
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
