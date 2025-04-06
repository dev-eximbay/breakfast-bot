package org.example.breakfast.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import org.example.breakfast.repository.FirestoreMenuRepository;
import org.example.breakfast.service.*;
import org.example.breakfast.time.TimeProvider;

import java.io.IOException;

/**
 * Created by Robin on 25. 4. 5.
 * Description :
 */
public class AppContext {
    private final TimeProvider timeProvider = TimeProvider.ofKorea();
    private final FirestoreMenuRepository menuRepository = new FirestoreMenuRepository();
    private final SlackBotMessageService slackService = new SlackBotMessageService();

    public TimeProvider timeProvider() {
        return timeProvider;
    }

    public FirestoreMenuRepository menuRepository() {
        return menuRepository;
    }

    public SlackBotMessageService slackBotMessageService() {
        return slackService;
    }

    public MealTimePolicy mealTimePolicy() {
        return new MealTimePolicy(timeProvider);
    }

    public BreakfastNotifier breakfastNotifier() {
        return new BreakfastNotifier(timeProvider, menuRepository, slackService);
    }

    public ExcelParserService excelParserService() {
        return new ExcelParserService();
    }

    public SocketModeApp socketModeApp(String appToken, App app) {
        try {
            return new SocketModeApp(appToken, app);
        } catch (IOException e) {
            throw new IllegalStateException("Slack SocketModeApp 초기화 실패", e);
        }
    }
}
