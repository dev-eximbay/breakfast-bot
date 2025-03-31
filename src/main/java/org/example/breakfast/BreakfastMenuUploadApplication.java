package org.example.breakfast;


import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.File;
import com.slack.api.model.Message;
import org.example.breakfast.model.Menu;
import org.example.breakfast.service.ExcelParserService;
import org.example.breakfast.service.FirestoreMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

        app.command("/조식등록", (req, ctx) -> {
            String channelId = req.getPayload().getChannelId();
            ConversationsHistoryResponse history = ctx.client().conversationsHistory(r -> r.channel(channelId).limit(5));

            if (!history.isOk()) {
                return ctx.ack("메시지를 가져오는 데 실패했습니다: " + history.getError());
            }

            if (history.getMessages() == null || history.getMessages().isEmpty()) {
                return ctx.ack("최근 메시지가 없습니다.");
            }

            for (Message message : history.getMessages()) {
                if (message.getFiles() != null && !message.getFiles().isEmpty()) {
                    File slackFile = message.getFiles().get(0);
                    String fileUrl = slackFile.getUrlPrivateDownload();

                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
                        connection.setRequestProperty("Authorization", "Bearer " + botToken);
                        InputStream inputStream = connection.getInputStream();
                        ExcelParserService excelParserService = new ExcelParserService();
                        List<Menu> menus = excelParserService.parseBreakfastMenu(inputStream);
                        System.out.println("menus.toString() = " + menus.toString());
                        FirestoreMenuService firestoreMenuService = new FirestoreMenuService();
                        firestoreMenuService.saveMenus(menus);
                        inputStream.close();
                        return ctx.ack("엑셀 파일을 성공적으로 읽었습니다!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ctx.ack("파일 다운로드 중 오류 발생: " + e.getMessage());
                    }
                }
            }

            return ctx.ack("최근 메시지에서 첨부된 파일을 찾을 수 없습니다.");
        });

        new SocketModeApp(appToken, app).start();
    }
}
