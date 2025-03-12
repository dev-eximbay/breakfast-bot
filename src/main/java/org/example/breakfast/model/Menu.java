package org.example.breakfast.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Robin on 25. 3. 12.
 * Description : Menu 정보
 */
public record Menu(LocalDate date, String menu, LocalDateTime regDate) {
}
