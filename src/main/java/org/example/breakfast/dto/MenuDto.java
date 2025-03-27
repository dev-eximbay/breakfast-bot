package org.example.breakfast.dto;

import com.google.cloud.Timestamp;
import org.example.breakfast.model.Menu;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Created by Robin on 25. 3. 27.
 * Description : Firebase에 전달하기 위한 MenuDto
 */
public class MenuDto {
    private Timestamp date;
    private String menu;
    private Timestamp createdAt;

    public MenuDto(Timestamp date, String menu, Timestamp createdAt) {
        this.date = date;
        this.menu = menu;
        this.createdAt = createdAt;
    }

    public Timestamp getDate() { return date; }
    public String getMenu() { return menu; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Timestamp -> yyyy-MM-dd 형식의 문자열 반환
    public String getDateString() {
        LocalDate localDate = this.date.toSqlTimestamp().toInstant()
                .atZone(ZoneId.of("UTC")).toLocalDate();
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE); // "yyyy-MM-dd"
    }

    // Menu -> MenuDto 변환 메서드
    public static MenuDto from(Menu menu) {
        Timestamp dateTs = Timestamp.ofTimeSecondsAndNanos(
                menu.getDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC), 0
        );
        Timestamp createdAtTs = Timestamp.ofTimeSecondsAndNanos(
                menu.getCreatedAt().toEpochSecond(ZoneOffset.UTC), 0
        );
        return new MenuDto(dateTs, menu.getMenu(), createdAtTs);
    }
}
