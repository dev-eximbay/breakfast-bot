package org.example.breakfast.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.files.FilesInfoRequest;
import com.slack.api.methods.response.conversations.ConversationsInfoResponse;
import com.slack.api.methods.response.files.FilesInfoResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Robin on 25. 3. 12.
 * Description : 슬랙 파일 업로드 관련 Service
 */

public class SlackFileService {
    private static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");
    private static final String ALLOWED_CHANNEL = "조식-메뉴"; // 조식-메뉴 채널만 허용
    private static final Logger log = LoggerFactory.getLogger(SlackFileService.class);

    public String downloadLatestBreakfastFile(String fileId, String channelId) throws IOException, SlackApiException {
        Slack slack = Slack.getInstance();

        // 채널 정보 확인
        if (!isAllowedChannel(slack, channelId)) {
            throw new RuntimeException("❌ 이 명령어는 '#" + ALLOWED_CHANNEL + "' 채널에서만 사용할 수 있습니다.");
        }

        // 파일 정보 가져오기
        FilesInfoResponse response = slack.methods(SLACK_BOT_TOKEN)
                .filesInfo(FilesInfoRequest.builder().file(fileId).build());

        if (response.isOk()) {
            String fileUrl = response.getFile().getUrlPrivateDownload();
            File file = new File("menu.xlsx");

            // Slack 파일 다운로드 (권한 필요)
            FileUtils.copyURLToFile(new URL(fileUrl), file);

            return file.getAbsolutePath();
        } else {
            throw new RuntimeException("파일을 다운로드할 수 없습니다: " + response.getError());
        }
    }

    private static boolean isAllowedChannel(Slack slack, String channelId) throws IOException, SlackApiException {
        ConversationsInfoResponse response = slack.methods(SLACK_BOT_TOKEN)
                .conversationsInfo(req -> req.channel(channelId));

        if (response.isOk() && response.getChannel() != null) {
            String channelName = response.getChannel().getName();
            return ALLOWED_CHANNEL.equals(channelName);
        }

        return false;
    }
}
