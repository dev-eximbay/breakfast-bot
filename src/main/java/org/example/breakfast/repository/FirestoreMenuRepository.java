package org.example.breakfast.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.example.breakfast.dto.MenuDto;
import org.example.breakfast.model.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Robin on 25. 3. 11.
 * Description: Firestore에 저장된 조식 메뉴 데이터를 조회·추가·삭제하는 기능을 담당하는 서비스 클래스.
 */

public class FirestoreMenuRepository implements MenuRepository {
    private static final Logger log = LoggerFactory.getLogger(FirestoreMenuRepository.class);
    private static final String FIREBASE_CREDENTIAL = "FIREBASE_CREDENTIAL";
    private static final String FIRESTORE_COLLECTION = "breakfasts";
    private static Firestore firestore;

    public FirestoreMenuRepository() {
        try {
            initializeFirebase();
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생", e);
        }
    }

    private void initializeFirebase() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            log.info("Firebase가 이미 초기화되어 있습니다.");
            return;
        }

        String credentialsJson = System.getenv(FIREBASE_CREDENTIAL);
        if (credentialsJson == null || credentialsJson.isBlank()) {
            throw new IllegalStateException("환경 변수 '" + FIREBASE_CREDENTIAL + "'가 설정되지 않았습니다.");
        }

        try (var credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setDatabaseUrl("https://your-project-id.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            firestore = FirestoreClient.getFirestore();
            log.info("Firebase 초기화 완료.");
        }
    }

    private Firestore getFirestore() {
        if (firestore == null) {
            firestore = FirestoreClient.getFirestore();
        }
        return firestore;
    }

    @Override
    public Optional<String> fetchMenu(LocalDate date) {
        String dateFormat = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        Firestore db = getFirestore();
        DocumentReference docRef = db.collection(FIRESTORE_COLLECTION).document(dateFormat);

        try {
            DocumentSnapshot document = docRef.get().get();
            return document.exists() ? Optional.ofNullable(document.getString("menu")) : Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Firestore에서 데이터를 가져오는 중 오류 발생", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public void addMenu(Menu menu) {
        Firestore db = getFirestore();
        MenuDto dto = MenuDto.from(menu);
        String stringDate = menu.getDate().toString();
        DocumentReference docRef = db.collection(FIRESTORE_COLLECTION).document(stringDate);

        try {
            docRef.set(menu).get();
            log.info("날짜 {}에 '{}' 메뉴가 추가되었습니다.", stringDate, menu);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Firestore에 메뉴를 추가하는 중 오류 발생", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void saveMenus(List<Menu> menus) {
        Firestore db = FirestoreClient.getFirestore();
        List<MenuDto> menuDtos = menus.stream().map(MenuDto::from).toList();
        for (MenuDto menuDto : menuDtos) {
            db.collection("breakfasts")
                    .document(menuDto.getDateString()) // 날짜를 문서 ID로 사용
                    .set(menuDto);
        }
    }

    @Override
    public void deleteMenu(LocalDate date) {
        String dateFormat = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        Firestore db = getFirestore();
        DocumentReference docRef = db.collection(FIRESTORE_COLLECTION).document(dateFormat);

        try {
            docRef.delete().get();
            log.info("날짜 {}의 메뉴가 삭제되었습니다.", date);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Firestore에서 메뉴를 삭제하는 중 오류 발생", e);
            Thread.currentThread().interrupt();
        }
    }
}
