package com.beyond.report.file;

import com.beyond.report.entity.BUDGET_DAY_DETAIL;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class BudgetUploadExcel {

    private static final int BEGIN_ROW = 1;

    /**
     * 匯入 Excel 預算檔案
     */
    public static List<BUDGET_DAY_DETAIL> importBudgetExcel(String filePath) throws Exception {
        List<BUDGET_DAY_DETAIL> detailList = new ArrayList<>();
        
        try {
            ZipSecureFile.setMinInflateRatio(0);
            
            Workbook workbook = WorkbookFactory.create(new File(filePath));
            Sheet sheet = workbook.getSheetAt(0);
            
            int lastRowNum = sheet.getLastRowNum();
            
            for (int i = BEGIN_ROW; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) {
                    break;
                }
                
                String floor = getCellValue(row.getCell(0)).trim();
                String deptId = getCellValue(row.getCell(1)).trim();
                String deptName = getCellValue(row.getCell(2)).trim();
                String counterId = getCellValue(row.getCell(3)).trim();
                
                if (counterId.isEmpty()) {
                    break;
                }
                
                String counterName = getCellValue(row.getCell(4)).trim();
                String orgName = getCellValue(row.getCell(5)).trim();
                
                // 驗證歸屬部別
                if (!orgName.isEmpty() && 
                    !"營一部".equals(orgName) && 
                    !"營二部".equals(orgName) && 
                    !"營三部".equals(orgName)) {
                    throw new Exception("[歸屬部別]格式錯誤(EX：營一部 / 營二部 / 營三部)，目前值：" + orgName);
                }
                
                // 讀取 1-31 日
                String[] dayValues = new String[31];
                for (int j = 0; j < 31; j++) {
                    dayValues[j] = getCellValue(row.getCell(6 + j)).trim();
                }
                
                BUDGET_DAY_DETAIL detail = new BUDGET_DAY_DETAIL();
                detail.setFloor(floor);
                detail.setDept_id(deptId);
                detail.setDept_name(deptName);
                detail.setCounter_id(counterId);
                detail.setCounter_name(counterName);
                detail.setOrg_name(orgName);
                
                // 設定每日預算值
                detail.setB_01(parseInt(dayValues[0]));
                detail.setB_02(parseInt(dayValues[1]));
                detail.setB_03(parseInt(dayValues[2]));
                detail.setB_04(parseInt(dayValues[3]));
                detail.setB_05(parseInt(dayValues[4]));
                detail.setB_06(parseInt(dayValues[5]));
                detail.setB_07(parseInt(dayValues[6]));
                detail.setB_08(parseInt(dayValues[7]));
                detail.setB_09(parseInt(dayValues[8]));
                detail.setB_10(parseInt(dayValues[9]));
                detail.setB_11(parseInt(dayValues[10]));
                detail.setB_12(parseInt(dayValues[11]));
                detail.setB_13(parseInt(dayValues[12]));
                detail.setB_14(parseInt(dayValues[13]));
                detail.setB_15(parseInt(dayValues[14]));
                detail.setB_16(parseInt(dayValues[15]));
                detail.setB_17(parseInt(dayValues[16]));
                detail.setB_18(parseInt(dayValues[17]));
                detail.setB_19(parseInt(dayValues[18]));
                detail.setB_20(parseInt(dayValues[19]));
                detail.setB_21(parseInt(dayValues[20]));
                detail.setB_22(parseInt(dayValues[21]));
                detail.setB_23(parseInt(dayValues[22]));
                detail.setB_24(parseInt(dayValues[23]));
                detail.setB_25(parseInt(dayValues[24]));
                detail.setB_26(parseInt(dayValues[25]));
                detail.setB_27(parseInt(dayValues[26]));
                detail.setB_28(parseInt(dayValues[27]));
                detail.setB_29(parseInt(dayValues[28]));
                detail.setB_30(parseInt(dayValues[29]));
                detail.setB_31(parseInt(dayValues[30]));
                
                detailList.add(detail);
            }
            
            workbook.close();
            log.info("Excel 匯入完成，共 {} 筆資料", detailList.size());
            
        } catch (Exception e) {
            log.error("匯入 Excel 失敗: {}", e.getMessage(), e);
            throw new Exception("Excel 匯入失敗: " + e.getMessage(), e);
        }
        
        return detailList;
    }

    /**
     * 取得 Cell 值 (使用最新 POI API)
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            CellType cellType = cell.getCellType();
            
            switch (cellType) {
                case STRING:
                    return cell.getStringCellValue();
                    
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        return sdf.format(date);
                    } else {
                        double value = cell.getNumericCellValue();
                        if (value == (long) value) {
                            return String.valueOf((long) value);
                        }
                        // 使用 DecimalFormat 避免科學記號
                        return String.valueOf(value);
                    }
                    
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                    
                case FORMULA:
                    try {
                        CellType resultType = cell.getCachedFormulaResultType();
                        switch (resultType) {
                            case NUMERIC:
                                double numValue = cell.getNumericCellValue();
                                if (numValue == (long) numValue) {
                                    return String.valueOf((long) numValue);
                                }
                                return String.valueOf(numValue);
                            case STRING:
                                return cell.getStringCellValue();
                            default:
                                return cell.getCellFormula();
                        }
                    } catch (Exception e) {
                        return cell.getCellFormula();
                    }
                    
                case BLANK:
                    return "";
                    
                default:
                    return "";
            }
            
        } catch (Exception e) {
            log.warn("讀取 Cell 值失敗: {}", e.getMessage());
            return "";
        }
    }

    /**
     * 將字串轉為整數
     */
    private static int parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        try {
            return (int) Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}