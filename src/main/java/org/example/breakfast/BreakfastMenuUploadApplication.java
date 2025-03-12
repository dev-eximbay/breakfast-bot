package org.example.breakfast;

import com.slack.api.methods.SlackApiException;
import org.example.breakfast.model.Menu;
import org.example.breakfast.service.ExcelParserService;
import org.example.breakfast.service.FirestoreMenuService;
import org.example.breakfast.service.SlackFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by Robin on 25. 3. 1.
 * Description : 조식 메뉴 알림 Main Application
 */
public class BreakfastMenuUploadApplication {
    private static final Logger log = LoggerFactory.getLogger(BreakfastMenuUploadApplication.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("파일 ID가 전달되지 않았습니다.");
            return;
        }

        String fileId = args[0];

        try {

            // Slack에서 파일 다운로드
            SlackFileService slackFileService = new SlackFileService();
            String filePath = slackFileService.downloadLatestBreakfastFile(fileId, "조식-메뉴");
            log.info("엑셀 파일 다운로드 완료: {}", filePath);

            // 엑셀 파일 파싱
            ExcelParserService excelParserService = new ExcelParserService();
            List<Menu> menus = excelParserService.parseBreakfastMenu(filePath);
            log.info("총 {}개의 조식 메뉴가 파싱됨", menus.size());

            // Firestore 저장
            FirestoreMenuService menuService = null;
            try {
                menuService = new FirestoreMenuService();
            } catch (IOException e) {
                log.error("Firebase 초기화 중 오류 발생", e);
            }

            if (menuService != null) {
                menuService.saveMenus(menus);
                log.info("Firestore에 조식 엑셀 데이터 저장 완료!");
            }

        } catch (IOException | SlackApiException e) {
            log.error("엑셀 파일 처리 중 오류 발생", e);
        }
    }
}