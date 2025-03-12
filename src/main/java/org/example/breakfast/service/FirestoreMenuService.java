package org.example.breakfast.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Robin on 25. 3. 11.
 * Description :
 */

public class FirestoreMenuService {
    private static final Logger log = LoggerFactory.getLogger(FirestoreMenuService.class);
    private static final String FIREBASE_CREDENTIALS = "FIREBASE_CREDENTIALS";
    private static final String FIRESTORE_COLLECTION = "breakfasts";
    private static Firestore firestore;

    public FirestoreMenuService() throws IOException {
        initializeFirebase();
    }

    private void initializeFirebase() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            log.info("Firebase가 이미 초기화되어 있습니다.");
            return;
        }

        String credentialsJson = System.getenv(FIREBASE_CREDENTIALS);
        if (credentialsJson == null || credentialsJson.isBlank()) {
            throw new IllegalStateException("환경 변수 '" + FIREBASE_CREDENTIALS + "'가 설정되지 않았습니다.");
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

    public Optional<String> fetchMenu(String date) {
        Firestore db = getFirestore();
        DocumentReference docRef = db.collection(FIRESTORE_COLLECTION).document(date);

        try {
            DocumentSnapshot document = docRef.get().get();
            return document.exists() ? Optional.ofNullable(document.getString("menu")) : Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Firestore에서 데이터를 가져오는 중 오류 발생", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    public void addMenu(String date, String menu) {
        Firestore db = getFirestore();
        DocumentReference docRef = db.collection(FIRESTORE_COLLECTION).document(date);

        try {
            docRef.set(new Menu(menu)).get();
            log.info("날짜 {}에 '{}' 메뉴가 추가되었습니다.", date, menu);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Firestore에 메뉴를 추가하는 중 오류 발생", e);
            Thread.currentThread().interrupt();
        }
    }

    public void deleteMenu(String date) {
        Firestore db = getFirestore();
        DocumentReference docRef = db.collection(FIRESTORE_COLLECTION).document(date);

        try {
            docRef.delete().get();
            log.info("날짜 {}의 메뉴가 삭제되었습니다.", date);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Firestore에서 메뉴를 삭제하는 중 오류 발생", e);
            Thread.currentThread().interrupt();
        }
    }

    private static class Menu {
        private String menu;

        public Menu() {}

        public Menu(String menu) {
            this.menu = menu;
        }

        public String getMenu() {
            return menu;
        }
    }
}
