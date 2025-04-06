package org.example.breakfast.service;

import org.example.breakfast.DailyBreakfastNotifyApplication;
import org.example.breakfast.repository.MenuRepository;
import org.example.breakfast.time.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created by Robin on 25. 4. 5.
 *  Description: 조식 알림 메시지를 구성하고 Slack으로 전송하는 역할을 담당.
 *  오늘 또는 다음 영업일 조식 메뉴를 조회하고, 형식화된 메시지를 슬랙으로 발송한다.
 */

public class BreakfastNotifier {
    private static final Logger log = LoggerFactory.getLogger(DailyBreakfastNotifyApplication.class);
    private final TimeProvider timeProvider;
    private final SlackBotMessageService slackBotService;
    private final MenuRepository menuRepository;

    public BreakfastNotifier(TimeProvider koreaTimeProvider, MenuRepository menuRepository, SlackBotMessageService slackBotService) {
        this.timeProvider = koreaTimeProvider;
        this.menuRepository = menuRepository;
        this.slackBotService = slackBotService;
    }

    public void sendTodayBreakfastMenuAlert() {
        LocalDate currentDate = timeProvider.nowDate();

        menuRepository.fetchMenu(currentDate).ifPresentOrElse(
                menu -> {
                    String message = SlackMessageFormatter.formatTodayMenu(currentDate, menu);
                    slackBotService.sendSlackMessage(message);
                },
                () -> log.error("오늘의 메뉴 정보가 없습니다.")
        );
    }

    public void sendNextBreakfastMenuAlert() {
        LocalDate currentDate = timeProvider.nowDate();
        LocalDate nextDate = getNextDay();

        menuRepository.fetchMenu(nextDate).ifPresentOrElse(
                menu -> {
                    String message = SlackMessageFormatter.formatNextMenu(currentDate, nextDate, menu);
                    slackBotService.sendSlackMessage(message);
                },
                () -> log.info("내일 메뉴 정보가 없습니다.")
        );
    }

    private LocalDate getNextDay() {
        LocalDate today = timeProvider.nowDate();
        LocalDate tomorrow = today.plusDays(1);
        if (today.getDayOfWeek() == DayOfWeek.FRIDAY) {
            LocalDate nextMonday = today.plusDays(3);
            return nextMonday;
        }
        return tomorrow;
    }
}
