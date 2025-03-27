package org.example.breakfast.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.breakfast.model.Menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 25. 3. 12..
 * Description : 조식 엑셀을 읽어서 데이터로 파싱
 */
public class ExcelParserService {

    private static final int DATE_ROW_INTERVAL = 15;
    private static final int DATE_ROW_OFFSET = 4;
    private static final int MENU_START_OFFSET = 1;
    private static final int MENU_ROW_COUNT = 14;

    public List<Menu> parseBreakfastMenu(InputStream inputStream) throws IOException {
        List<Menu> breakfastMenus = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        int currentYear = LocalDate.now(ZoneId.of("Asia/Seoul")).getYear();
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        for (int blockStart = DATE_ROW_OFFSET; blockStart < sheet.getLastRowNum(); blockStart += DATE_ROW_INTERVAL) {
            processDateBlock(sheet, blockStart, currentYear, fullFormatter, breakfastMenus);
        }

        workbook.close();
        return breakfastMenus;
    }

    private void processDateBlock(Sheet sheet, int blockStart, int year, DateTimeFormatter formatter, List<Menu> breakfastMenus) {
        Row dateRow = sheet.getRow(blockStart);
        if (dateRow == null) return;

        for (int col = 0; col < dateRow.getLastCellNum(); col++) {
            LocalDate date = extractDate(dateRow.getCell(col), year, formatter);
            if (date == null) continue;

            List<String> menuItems = extractMenuItems(sheet, col, blockStart + MENU_START_OFFSET, blockStart + MENU_START_OFFSET + MENU_ROW_COUNT);
            if (!menuItems.isEmpty()) {
                breakfastMenus.add(new Menu(date, String.join(", ", menuItems), LocalDateTime.now()));
            }
        }
    }

    private LocalDate extractDate(Cell cell, int year, DateTimeFormatter formatter) {
        if (cell == null || cell.getCellType() != CellType.STRING) return null;
        String rawDate = cell.getStringCellValue().trim();
        if (!rawDate.matches("\\d{2}월 \\d{2}일")) return null;

        return LocalDate.parse(year + "년 " + rawDate, formatter);
    }

    private List<String> extractMenuItems(Sheet sheet, int col, int startRow, int endRow) {
        List<String> items = new ArrayList<>();
        for (int rowIdx = startRow; rowIdx < endRow; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            if (row == null) continue;

            Cell cell = row.getCell(col);
            if (cell == null || cell.getCellType() != CellType.STRING) continue;

            String value = cell.getStringCellValue().trim();
            if (isInvalidMenu(value)) continue;

            items.add(value);
        }
        return items;
    }

    private boolean isInvalidMenu(String value) {
        return value.isEmpty() || value.contains("포크") || value.contains("젓가락") || value.contains("￦");
    }
}
