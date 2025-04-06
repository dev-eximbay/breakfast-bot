package org.example.breakfast;


import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.File;
import com.slack.api.model.Message;
import org.example.breakfast.config.AppContext;
import org.example.breakfast.model.Menu;
import org.example.breakfast.service.ExcelParserService;
import org.example.breakfast.repository.FirestoreMenuRepository;
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
    public static final String COMMAND_UPLOAD_BREAKFAST = "/조식등록";

    public static void main(String[] args) {
        String botToken = System.getenv("SLACK_BOT_TOKEN");
        String appToken = System.getenv("SLACK_APP_TOKEN");
        AppContext appContext = new AppContext();
        FirestoreMenuRepository menuRepository = appContext.menuRepository();
        ExcelParserService excelParserService = appContext.excelParserService();

        AppConfig config = AppConfig.builder()
                .singleTeamBotToken(botToken)
                .build();
        App app = new App(config);

        app.command(COMMAND_UPLOAD_BREAKFAST, (req, ctx) -> {
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
                        List<Menu> menus = excelParserService.parseBreakfastMenu(inputStream);
                        log.info("menus.toString() = {}", menus.toString());
                        menuRepository.saveMenus(menus);
                        inputStream.close();
                        return ctx.ack("엑셀 파일을 성공적으로 읽었습니다!");
                    } catch (Exception e) {
                        log.error("조식 메뉴 업로드 중 오류 발생", e);
                        return ctx.ack("파일 다운로드 중 오류 발생: " + e.getMessage());
                    }
                }
            }

            return ctx.ack("최근 메시지에서 첨부된 파일을 찾을 수 없습니다.");
        });

        try {
            appContext.socketModeApp(appToken, app).start();
        } catch (Exception e) {
            log.error("❌ Slack SocketModeApp 실행 중 치명적인 오류 발생", e);
            throw new IllegalStateException("Slack SocketModeApp 실행 실패", e);
        }
    }
}
