package org.example.breakfast.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SlackBotService {
    private static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    private static final String CHANNEL_ID = "#조식-메뉴";  // 원하는 Slack 채널
    private static final Logger log = LoggerFactory.getLogger(SlackBotService.class);

    public static void sendSlackMessage(String message) {
        if (SLACK_BOT_TOKEN == null || CHANNEL_ID == null) {
            log.error("Slack 환경 변수가 설정되지 않았습니다.");
            return;
        }

        Slack slack = Slack.getInstance();
        try {
            slack.methods(SLACK_BOT_TOKEN).chatPostMessage(req -> req
                    .channel(CHANNEL_ID)
                    .text(message));
            log.info("✅ Slack 메시지 전송 완료!");
        } catch (IOException | SlackApiException e) {
            log.error("Slack 메세지 전송 중 오류 발생", e);
        }
    }
}
