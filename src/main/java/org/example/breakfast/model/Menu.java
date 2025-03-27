package org.example.breakfast.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Robin on 25. 3. 12.
 * Description : Menu 정보
 */
public class Menu {
    private LocalDate date;
    private String menu;
    private LocalDateTime createdAt;

    public Menu() {} // Firebase나 Jackson에 필요

    public Menu(LocalDate date, String menu, LocalDateTime createdAt) {
        this.date = date;
        this.menu = menu;
        this.createdAt = createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMenu() {
        return menu;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

