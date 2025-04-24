package org.example.breakfast.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class SlackMessageFormatterTest {

    @Test
    void formatNextMenu() {
/*        // Given
        LocalDate alertDate = LocalDate.of(2025, 4, 8); // 화요일
        String menu = "스팸 도시락";

        String expectedDateFormat = alertDate.format(DateTimeFormatter.ofPattern("MM-dd"));
        String expectedDayOfWeek = alertDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        String expected = String.format("<!channel> \n\n" +
                        "⏰ *%s(%s) 조식 메뉴* \n" +
                        "━━━━━━━━━━━━━━━━━━\n" +
                        "\uD83D\uDC49 %s\n" +
                        "━━━━━━━━━━━━━━━━━━\n" +
                        "\uD83D\uDE0B *Happy Snacking!* \uD83C\uDF6A",
                expectedDateFormat, expectedDayOfWeek, menu);

        // When
        String result = SlackMessageFormatter.formatNextMenu(alertDate, menu);
        // Then
        assertEquals(expected, result);*/
    }
}