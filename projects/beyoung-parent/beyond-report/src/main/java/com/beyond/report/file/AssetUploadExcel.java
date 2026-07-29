package com.beyond.report.file;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
// import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.beyond.report.entity.ASSET_INVENTORY; // 請根據您專案中 ASSET_INVENTORY 的實際 package 路徑進行調整

public class AssetUploadExcel {

  public AssetUploadExcel() {
  }

  public static ArrayList<ASSET_INVENTORY> importAssetExcel(String sFile) throws Exception {
    int iBeginRow = 1;
    ArrayList<ASSET_INVENTORY> _al = new ArrayList<ASSET_INVENTORY>();
    String _sSelfNo = "";
    String _sModelNo = "";
    String _sType = "";
    String _sProdDesc = "";
    String _sOwnerId = "";
    
    try {
      ZipSecureFile.setMinInflateRatio(0);
      Workbook workbook = WorkbookFactory.create(new File(sFile));
      Sheet rs = workbook.getSheetAt(0);
      int _iEndRow = rs.getLastRowNum() + 1;
      
      for (int i = iBeginRow; i < _iEndRow; i++) {
        Cell c1 = rs.getRow(i).getCell(0);
        _sSelfNo = getContents(c1).trim();

        Cell c2 = rs.getRow(i).getCell(1);
        _sModelNo = getContents(c2).trim();

        Cell c3 = rs.getRow(i).getCell(2);
        _sType = getContents(c3).trim();

        Cell c4 = rs.getRow(i).getCell(3);
        _sProdDesc = getContents(c4).trim();

        Cell c5 = rs.getRow(i).getCell(4);
        _sOwnerId = getContents(c5).trim();

        ASSET_INVENTORY _entity = new ASSET_INVENTORY();
        _entity.setSelf_no(_sSelfNo);
        _entity.setModel_no(_sModelNo);
        _entity.setType(_sType);
        _entity.setProd_desc(_sProdDesc);
        _entity.setOwner_id(_sOwnerId);
        _al.add(_entity);
      }
      return _al;
    } catch (Exception e) {
      throw e;
    }
  }

  private static String getContents(Cell cell) {
    if (cell == null) {
        return "";
    }

    String _sValue = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    // 使用 cell.getCellType() 回傳的 CellType 列舉
    switch (cell.getCellType()) {
        case STRING:
            _sValue = cell.getStringCellValue();
            break;

        case NUMERIC:
            // 1. 換成通用的 DateUtil
            if (DateUtil.isCellDateFormatted(cell)) {
                Date _d = cell.getDateCellValue();
                _sValue = sdf.format(_d);
            } else {
                // 2. 建議使用 BigDecimal 或 DataFormatter 避免浮點數轉型溢位或小數點遺失
                _sValue = new BigDecimal(Double.toString(cell.getNumericCellValue())).toPlainString();
                // 若確定此欄位全是整數，可維持原本的： _sValue = String.valueOf((int) cell.getNumericCellValue());
            }
            break;

        case FORMULA:
            // 處理公式計算後的結果
            switch (cell.getCachedFormulaResultType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        _sValue = sdf.format(cell.getDateCellValue());
                    } else {
                        _sValue = new BigDecimal(Double.toString(cell.getNumericCellValue())).toPlainString();
                    }
                    break;
                case STRING:
                    _sValue = cell.getRichStringCellValue().getString();
                    break;
                case BOOLEAN:
                    _sValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                default:
                    _sValue = cell.getCellFormula();
                    break;
            }
            break;

        case BOOLEAN:
            _sValue = String.valueOf(cell.getBooleanCellValue());
            break;

        case BLANK:
        case ERROR:
        default:
            _sValue = "";
            break;
    }

    return _sValue;
  }

}