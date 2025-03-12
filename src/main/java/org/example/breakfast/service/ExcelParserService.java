package org.example.breakfast.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.breakfast.model.Menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 25. 3. 12..
 * Description : 조식 엑셀을 읽어서 데이터로 파싱
 */
public class ExcelParserService {

    public List<Menu> parseBreakfastMenu(String filePath) throws IOException {
        List<Menu> breakfastMenus = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        for (Row row : sheet) {
            if (row.getRowNum() < 2) continue; // 헤더 스킵

            Cell dateCell = row.getCell(0);  // 날짜 셀
            Cell menuCell = row.getCell(1);  // 메뉴 셀

            if (dateCell == null || menuCell == null) continue;

            LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), formatter);
            String menu = menuCell.getStringCellValue().trim();

            // 메뉴가 비어있거나 불필요한 항목(포크, 젓가락) 제외
            if (!menu.isEmpty() && !menu.contains("포크") && !menu.contains("젓가락")) {
                breakfastMenus.add(new Menu(date, menu, LocalDateTime.now()));
            }
        }

        workbook.close();
        return breakfastMenus;
    }
}
