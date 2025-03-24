package org.example.breakfast;


import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Robin on 25. 3. 1.
 * Description : 조식 메뉴 업로드 Main Application
 */
public class BreakfastMenuUploadApplication {
    private static final Logger log = LoggerFactory.getLogger(BreakfastMenuUploadApplication.class);

    public static void main(String[] args) throws Exception {
        String botToken = System.getenv("SLACK_BOT_TOKEN");
        String appToken = System.getenv("SLACK_APP_TOKEN");

        AppConfig config = AppConfig.builder()
                .singleTeamBotToken(botToken)
                .build();

        App app = new App(config);

        app.command("/upload-breakfast", (req, ctx) -> {
            System.out.println("upload-breakfast success");
            return ctx.ack("upload success!");
        });

        new SocketModeApp(appToken, app).start();
    }
}