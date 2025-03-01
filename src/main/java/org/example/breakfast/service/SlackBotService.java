package org.example.breakfast.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;

public class SlackBotService {
    private static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    private static final String CHANNEL_ID = "#조식-메뉴";  // 원하는 Slack 채널

    public static void sendSlackMessage(String message) {
        Slack slack = Slack.getInstance();
        try {
            slack.methods(SLACK_BOT_TOKEN).chatPostMessage(req -> req
                    .channel(CHANNEL_ID)
                    .text(message));
            System.out.println("✅ Slack 메시지 전송 완료!");
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendSlackMessage("🍽 *테스트 메시지* - 조식 메뉴 봇 설정 완료!");
    }
}
