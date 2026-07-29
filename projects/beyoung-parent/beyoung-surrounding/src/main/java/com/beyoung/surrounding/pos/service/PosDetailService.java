package com.beyoung.surrounding.pos.service;

import com.beyoung.surrounding.pos.entity.TD;
import com.beyoung.surrounding.pos.entity.TP;
import com.beyoung.surrounding.pos.entity.TR;
import com.beyoung.surrounding.pos.bean.PosDetailBean;
import com.beyoung.surrounding.pos.repository.TDRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PosDetailService {

    private final TDRepository tdRepository;

    @Transactional(readOnly = true)
    public List<TD> getTDByInvoiceNO(Environment env, String center, String invoiceDate, String invoiceNO) throws Exception {
        
        // 1. 參數基本防呆檢查
        if (invoiceNO == null || invoiceNO.isBlank()) {
            log.warn("getTDByInvoiceNO 傳入的發票號碼為空");
            return java.util.Collections.emptyList();
        }

        log.info("查詢發票明細列表(Service)，店別: {}, 日期: {}, 發票號: {}", center, invoiceDate, invoiceNO);

        // 2. 呼叫我們剛才寫好的新版 TdRepository 原生 SQL 查詢
        // 備註：你新寫的 TdRepository 原生 SQL 條件已經能精準鎖定店別與發票號碼，因此直接帶入查詢即可
        List<TD> lEntity = tdRepository.getTDByInvoiceNO(center, invoiceNO);
        
        // 判斷並回傳結果
        if (lEntity != null) {
            return lEntity;
        }
        
        return java.util.Collections.emptyList();
    }
    
    
    public TD getTDByInvoiceNO(Environment env, String center, String invoiceNO) throws Exception {
        
        // 1. 參數基本防護
        if (invoiceNO == null || invoiceNO.isBlank()) {
            log.warn("getTDByInvoiceNO 傳入的發票號碼為空");
            return null;
        }

        log.info("查詢發票明細(Service)，店別: {}, 發票號: {}", center, invoiceNO);

        // 2. 呼叫新版 Repository 
        // 由於你的新版 Repository 查詢回傳的是 List<TD>，在此呼叫後取出第一筆符合的實體回傳
        // 備註：若你的 findByInvNoCustom 需要日期，可在這邊用 sInvoiceNO 前幾碼解析或帶入當日，目前以不改變原有 Dao 查法為主
        List<TD> tdList = tdRepository.getTDByInvoiceNO(center, invoiceNO);
        
        // 3. 判斷是否有資料，並精準回傳單一 TD 物件
        if (tdList != null && !tdList.isEmpty()) {
            return tdList.get(0); // 👈 取出第一筆主檔資料，完美對齊回傳型態 TD
        }
        
        // 4. 查無資料則回傳 null
        return null;
    }
    
    public List<PosDetailBean> combine(List<TD> lEntity) throws Exception {
    	
        if (lEntity == null || lEntity.isEmpty()) {
            return Collections.emptyList();
        }

        List<PosDetailBean> result = new ArrayList<>();
        
        // 1. 建立對應新 Entity 的自訂臨時物件或 Map 結構來進行記憶體分組
        LinkedHashMap<String, TD> hTD = new LinkedHashMap<>();
        HashMap<String, List<TR>> hTR = new HashMap<>();
        HashMap<String, TP> hTP = new HashMap<>();

        for (TD entity : lEntity) {
            String seqNo = entity.getSeqNo();
            if (seqNo == null) continue;

            // 保留舊有特殊商業邏輯：蓋掉時分秒的冒號、調整發票金額
            if (!hTD.containsKey(seqNo)) {
                TD tdClone = TD.builder()
                        .salDate(entity.getSalDate())
                        .storeNo(entity.getStoreNo())
                        .posNo(entity.getPosNo())
                        .trnNo(entity.getTrnNo())
                        .vipNo(entity.getVipNo() == null ? "" : entity.getVipNo())
                        .totSales(entity.getTotSales())
                        .invAmt(entity.getPromotAmt()) // 👈 沿用舊邏輯: 2023-10-19 改為放 PROMOT_AMT
                        .promotAmt(entity.getPromotAmt())
                        .salTime(entity.getSalTime() != null ? entity.getSalTime().replace(":", "") : "")
                        .salType(entity.getSalType())
                        .tentNo(entity.getTentNo())
                        .seqNo(seqNo)
                        .invNo(entity.getInvNo())
                        .build();
                hTD.put(seqNo, tdClone);
            }

            // 沿用舊有邏輯：如果原 entity 內自帶 TR 欄位，進行記憶體收集
            if (entity.getItemNo() != null) {
                List<TR> trList = hTR.computeIfAbsent(seqNo, k -> new ArrayList<>());
                TR tr = new TR();
                tr.setItemNo(entity.getItemNo());
                tr.setQty(entity.getQty());
                tr.setSalDate(entity.getSalDate());
                tr.setGrdAmt(entity.getGrdAmt());
                tr.setSalPrice(entity.getSalPrice());
                trList.add(tr);
            	}

            // 沿用舊有邏輯：如果原 entity 內自帶 TP 信用卡欄位，進行記憶體收集
            if (!hTP.containsKey(seqNo) && entity.getMemo3() != null) {
                TP tp = new TP();
                tp.setInstallmentPeriod(entity.getInstallmentPeriod());
                tp.setMemo3(entity.getMemo3());
                tp.setPayAmt(entity.getPayAmt());
                hTP.put(seqNo, tp);
            	}
        	}

	        // 2. 如果你在上一關的原生 SQL 查出來的 TD 裡「沒有」包含 TR/TP 的明細欄位
	        //    那我們要在這裡利用 Repository 進行高效的「批次一次性查詢」，避開 N+1 效能問題
	        /* List<String> salDates = lEntity.stream().map(TD::getSalDate).distinct().toList();
	        List<String> trnNos = lEntity.stream().map(TD::getTrnNo).distinct().toList();
	        
	        // 假設你的 Repository 有寫一鍵 Batch 查詢:
	        List<TR> dbTRs = trRepository.findBySalDateInAndTrnNoIn(salDates, trnNos);
	        List<TP> dbTPs = tpRepository.findBySalDateInAndTrnNoIn(salDates, trnNos);
	        
	        // 查出來後再依照對應的 key 丟進 hTR 與 hTP 即可。
	        // 如果原本的 SQL 已經透過 JOIN 查出了明細，則此步驟可忽略。
	        */
	
	        // 3. 組裝為前端需要的 PosDetailBean 結構
	        for (String sKey : hTD.keySet()) {
	            PosDetailBean bean = new PosDetailBean();
	            bean.setTrancation(hTD.get(sKey));
	            bean.setProduction(hTR.getOrDefault(sKey, new ArrayList<>()));
	            bean.setCreditCard(hTP.get(sKey)); // 若無則為 null
	            result.add(bean);
	        }
	
	        return result;
	    }
    
    
	public List<TD> getTDByCardNO(String center, String start, String end, String cardNo) {
	    List<Object[]> results = tdRepository.findDetailsByCardNoNative(center, start, end, cardNo);
	    
	    // 將 Stream 映射直接指向您的轉換方法
	    return results.stream()
	                  .map(this::mapRowToTD)
	                  .collect(Collectors.toList());
	}

	/**
	 * 完整實作物件映射邏輯
	 */
	private TD mapRowToTD(Object[] row) {
	    TD td = new TD();
	    
	    // 使用 asString 與 asDouble，徹底解決 ClassCastException
	    td.setSalDate(asString(row[0]));
	    td.setStoreNo(asString(row[1]));
	    td.setPosNo(asString(row[2]));
	    td.setTentNo(asString(row[3]));
	    td.setVipNo(asString(row[4]));
	    
	    td.setInvAmt(asDouble(row[5]));
	    td.setTotSales(asDouble(row[6]));
	    td.setPromotAmt(asDouble(row[7]));
	    
	    td.setSalTime(asString(row[8]));
	    td.setSeqNo(asString(row[9]));
	    td.setInvNo(asString(row[10]));
	    td.setSalType(asString(row[11]));
	    
	    td.setItemNo(asString(row[12]));
	    td.setQty(asDouble(row[13]));
	    td.setSalPrice(asDouble(row[14]));
	    td.setGrdAmt(asDouble(row[15]));
	    
	    td.setMemo3(asString(row[16]));
	    td.setPayAmt(asDouble(row[17]));
	    td.setInstallmentPeriod(asDouble(row[18]));
	    
	    return td;
	}
	
	private String asString(Object obj) {
	    return obj == null ? null : obj.toString();
	}
	
	private Double asDouble(Object obj) {
	    if (obj == null) return 0.0;
	    if (obj instanceof Number) {
	        return ((Number) obj).doubleValue();
	    }
	    try {
	        return Double.parseDouble(obj.toString());
	    } catch (NumberFormatException e) {
	        return 0.0;
	    }
	}

	public List<TD> getTDByCreditCardNO(Environment env, String center, String invoiceDateS, String invoiceDateE, String preCardNo, String endCardNo) {
        // 呼叫 JPA Repository 撈取資料 (回傳 Object[] 列表)
        List<Object[]> results = tdRepository.findDetailsByCreditCardNo(
            center, invoiceDateS, invoiceDateE, preCardNo, endCardNo
        );

        // 將結果轉為 TD 物件列表
        return results.stream()
                      .map(this::mapRowToTD)
                      .collect(Collectors.toList());
    }
	
}