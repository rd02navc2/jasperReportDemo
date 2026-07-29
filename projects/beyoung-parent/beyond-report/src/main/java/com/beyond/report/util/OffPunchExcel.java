package com.beyond.report.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class OffPunchExcel {

    public OffPunchExcel() {
    }

    public static void genOffPunchExcel(JSONArray rows, String sSheetName, String sTgtFile, JSONObject jsonObj) throws Exception {
        // 使用 try-with-resources 自動關閉資源，避免檔案鎖定與記憶體洩漏
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             OutputStream outt = new FileOutputStream(sTgtFile)) {

            Sheet rs = workbook.createSheet(sSheetName);

            // 1. 建立表頭樣式 (Header Style)
            Font font1 = workbook.createFont();
            font1.setFontHeightInPoints((short) 8);
            font1.setColor(IndexedColors.BLACK.getIndex());
            font1.setBold(false);

            CellStyle csHeader = workbook.createCellStyle();
            csHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            csHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            csHeader.setFont(font1);
            csHeader.setAlignment(HorizontalAlignment.CENTER);
            csHeader.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(csHeader);

            // 2. 預先建立內容樣式 (Data Cell Style) - 避免在迴圈內重複建立引發記憶體與樣式上限溢出
            CellStyle csString = createDataStyle(workbook, HorizontalAlignment.LEFT, IndexedColors.WHITE.getIndex());

            // 3. 設定表頭
            Row row0 = rs.createRow(0);
            String[] headers = {"日期", "員工編號", "姓名", "說明", "上班時間", "下班時間"};
            int[] columnWidths = {16, 16, 16, 20, 20, 20};

            for (int col = 0; col < headers.length; col++) {
                rs.setColumnWidth(col, 256 * columnWidths[col]);
                Cell cell = row0.createCell(col);
                cell.setCellStyle(csHeader);
                cell.setCellValue(headers[col]);
            }

            // 4. 填入內容
            int _iBeginRow = 1;
            if (rows != null) {
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject _row = rows.getJSONObject(i);
                    Row rowX = rs.createRow(_iBeginRow);

                    // 日期
                    createCell(rowX, 0, _row.optString("date", ""), csString);
                    // 員工編號
                    createCell(rowX, 1, _row.optString("code", ""), csString);
                    // 姓名
                    createCell(rowX, 2, _row.optString("cnname", ""), csString);
                    // 說明
                    createCell(rowX, 3, _row.optString("desc", ""), csString);
                    // 上班時間
                    createCell(rowX, 4, _row.optString("begin_time", ""), csString);
                    // 下班時間
                    createCell(rowX, 5, _row.optString("end_time", ""), csString);

                    _iBeginRow++;
                }
            }

            // 5. 寫出檔案
            workbook.write(outt);
        }
    }

    private static void createCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }

    private static CellStyle createDataStyle(XSSFWorkbook workbook, HorizontalAlignment align, short bgIndex) {
        CellStyle cs = workbook.createCellStyle();
        cs.setFillForegroundColor(bgIndex);
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(align);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cs);
        return cs;
    }

    private static void setBorders(CellStyle cs) {
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setWrapText(false);
    }
}