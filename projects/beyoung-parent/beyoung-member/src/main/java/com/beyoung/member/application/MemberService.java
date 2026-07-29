package com.beyoung.member.application;

import com.beyoung.member.api.client.BonusServiceClient;
import com.beyoung.member.domain.dto.MemberContactDTO;
import com.beyoung.member.domain.dto.MemberDTO;
import com.beyoung.member.domain.dto.MemberStatsDTO;
import com.beyoung.member.infrastructure.LpjFile;
import com.beyoung.member.infrastructure.LpjFileRepository;
import com.beyoung.member.infrastructure.LpkFile;
import com.beyoung.member.infrastructure.LpkFileRepository;
import com.beyoung.member.infrastructure.LplFile;
import com.beyoung.member.infrastructure.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 會員服務層
 * 完整整合 MemberRes.java 的所有業務邏輯
 * 升級至 Java 21 / Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	@PersistenceContext 
    private EntityManager entityManager;

    private final MemberRepository memberRepository;
    private final LpjFileRepository lpjFileRepository;
    private final LpkFileRepository lpkFileRepository; 
    private final BonusServiceClient bonusServiceClient; // 透過 FeignClient 直連
    
    @Autowired
    private BonusServiceClient bonusClient; // 使用 @Autowired 進行自動注入

    /**
     * 核心邏輯：提供帶有時間範圍的點數歷史查詢（原 Feign 呼叫結構）
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPointHistByMemberID(String sMemberID, 
                                                            String sStartDate, 
                                                            String sEndDate) {
        log.info("透過 Feign 呼叫 Bonus 服務查詢點數歷史：ID={}, Start={}, End={}", sMemberID, sStartDate, sEndDate);
        // 將參數封裝，透過網路傳送給 Bonus 服務
        Map<String, String> params = new HashMap<>();
        params.put("memberId", sMemberID);
        params.put("startDate", sStartDate != null ? sStartDate : "");
        params.put("endDate", sEndDate != null ? sEndDate : "");

        // 因為 member 端沒有 LsmFile 類別，接收端改用 List<Map> 或 DTO
        return bonusServiceClient.getPointHistByMemberID(params);
    }
    
    /**
     * 修正補全 - 多載方法
     * 解決控制器只帶 1 個參數傳入時的編譯錯誤，並將型態完美轉換對齊 Controller 宣告的 List<LplFile> 規格
     */
    @Transactional(readOnly = true)
    public List<LplFile> getPointHistByMemberID(String sMemberID) {
        log.info("控制器調用：依會員ID單一參數查詢點數歷程 -> {}", sMemberID);
        
        // 1. 調用 3 參數方法（預設不帶起訖日，由微服務內置處理或拉取全部歷史）
        List<Map<String, Object>> rawHistory = this.getPointHistByMemberID(sMemberID, "", "");
        
        // 2. 將微服務回傳的 List<Map> 數據流，安全映射組裝為 Controller 需要的 List<LplFile> 實體集合
        return rawHistory.stream().map(map -> {
            LplFile lpl = new LplFile();
            lpl.setLpl01(map.get("lpl01") != null ? map.get("lpl01").toString() : sMemberID);
            
            // 處理日期格式轉換
            if (map.get("lpl02") != null) {
                try {
                    lpl.setLpl02(java.sql.Date.valueOf(map.get("lpl02").toString()));
                } catch (Exception e) {
                    lpl.setLpl02(new java.sql.Date(System.currentTimeMillis()));
                }
            }
            
            // 處理點數欄位轉換
            if (map.get("lpl09") != null) {
                try {
                    lpl.setLpl09(Integer.parseInt(map.get("lpl09").toString()));
                } catch (NumberFormatException e) {
                    lpl.setLpl09(0);
                }
            } else {
                lpl.setLpl09(0);
            }
            return lpl;
        }).collect(Collectors.toList());
    }

    // =========================================================================
    // checkExistLPK
    // =========================================================================
    @Transactional(readOnly = true)
    public MemberDTO.ExistResponse checkExistLPK(String sMemberID) throws Exception {
        boolean exists = memberRepository.isExistLPK(sMemberID);
        if (exists) {
            return new MemberDTO.ExistResponse("Y", sMemberID + " 已存在");
        } else {
            return new MemberDTO.ExistResponse("N", "查無資料");
        }
    }

    // =========================================================================
    // addTempMember
    // =========================================================================
    @Transactional(rollbackFor = Exception.class)
    public void addTempMember(String sCenter, String sMemberID) throws Exception {
        if (memberRepository.isExistLPK(sMemberID)) {
            throw new Exception(sMemberID + " 已存在，無法新增暫時會員");
        }
        memberRepository.addTempMember(sCenter, sMemberID, sMemberID);
        log.info("addTempMember 完成：sCenter={}, sMemberID={}", sCenter, sMemberID);
    }

    // =========================================================================
    // addRSTempMember
    // =========================================================================
    @Transactional(rollbackFor = Exception.class)
    public void addRSTempMember(String sCenter, String sMemberID) throws Exception {
        if (memberRepository.isExistLPK(sMemberID)) {
            throw new Exception(sMemberID + " 已存在，無法新增RS臨時會員");
        }
        memberRepository.addRSTempMember(sCenter, sMemberID, sMemberID);
        log.info("addRSTempMember 完成：sCenter={}, sMemberID={}", sCenter, sMemberID);
    }

    // =========================================================================
    // updateMemberContact
    // =========================================================================
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberContact(String sMemberID, String sMobile,
                                     String sEmail, String sAddr) throws Exception {
        if (!memberRepository.isExistLPK(sMemberID)) {
            throw new Exception(sMemberID + " 不存在，無法更新聯絡資料");
        }
        memberRepository.updMemberContact(sMemberID, sMobile, sEmail, sAddr);
        log.info("updateMemberContact 完成：sMemberID={}", sMemberID);
    }

    // =========================================================================
    // getAllCardByMemberID
    // =========================================================================
    @Transactional(readOnly = true)
    public List<MemberDTO.CardDetailResponse> getAllCardByMemberID(String sMemberID) {
        List<LpjFile> cardList = memberRepository.getAllCardByMemberID(sMemberID);
        
        // 如果查無資料，直接回傳空清單，由 Controller 處理成功訊息
        if (cardList == null) return Collections.emptyList();

        return cardList.stream()
            .map(lpj -> MemberDTO.CardDetailResponse.builder()
                .lpj01(lpj.getLpj01())
                .lpj02(lpj.getLpj02())
                .lpj03(lpj.getLpj03())
                .lpj04(lpj.getLpj04())
                .lpj06(lpj.getLpj06())
                .lpj07(lpj.getLpj07())
                .lpj09(lpj.getLpj09())
                .lpj12(lpj.getLpj12())
                .build())
            .collect(Collectors.toList());
    }
    
    // =========================================================================
    // getAllCardByID
    // =========================================================================
    @Transactional(readOnly = true)
    public List<MemberDTO.CardDetailResponse> getAllCardByID(String sID) throws Exception {
        // 1. 透過 Repository 取得包含 LpjFile 的資料清單
        List<LpjFile> cardList = memberRepository.getAllCardByID(sID);

        // 2. 轉換為 DTO 列表 (確保只回傳前端需要的欄位)
        return cardList.stream()
            .map(lpj -> MemberDTO.CardDetailResponse.builder()
                .lpj01(lpj.getLpj01())
                .lpj02(lpj.getLpj02())
                .lpj03(lpj.getLpj03())
                .lpj04(lpj.getLpj04())
                .lpj06(lpj.getLpj06())
                .lpj07(lpj.getLpj07())
                .lpj09(lpj.getLpj09())
                .lpj12(lpj.getLpj12())
                .build())
            .collect(Collectors.toList());
    }

    // =========================================================================
    // 透過會員ID取得點數資訊
    // =========================================================================
    @Transactional(readOnly = true)
    public MemberDTO.PointResponse getPointByMemberID(String sMemberID) {
        LpjFile lpj = memberRepository.getPointByMemberID(sMemberID);
        if (lpj == null) return null;
        
        return MemberDTO.PointResponse.builder()
                .lpj01(lpj.getLpj01())
                .lpj02(lpj.getLpj02())
                .lpj03(lpj.getLpj03())
                // 假設有對應的 LpkFile 關聯或欄位
                .lpj07(lpj.getLpj07())
                .lpj12(lpj.getLpj12() != null ? lpj.getLpj12().doubleValue() : 0.0)
                .lpj14(lpj.getLpj12() != null ? lpj.getLpj14().doubleValue() : 0.0)
                .lpj15(lpj.getLpj12() != null ? lpj.getLpj15().doubleValue() : 0.0)
                .taLpj02(lpj.getLpj12() != null ? lpj.getTaLpj02().doubleValue() : 0.0)
                .taLpj03(lpj.getLpj12() != null ? lpj.getTaLpj03().doubleValue() : 0.0)
                .build();
    }

    // =========================================================================
    // 透過身分證ID取得點數資訊
    // =========================================================================
    @Transactional(readOnly = true)
    public MemberDTO.PointResponse getPointByID(String sID) {
        LpjFile lpj = memberRepository.getPointByID(sID);
        if (lpj == null) return null;

        return MemberDTO.PointResponse.builder()
                .lpj01(lpj.getLpj01())
                .lpj02(lpj.getLpj02())
                .lpj03(lpj.getLpj03())
                .lpj07(lpj.getLpj07())
                .lpj12(lpj.getLpj12() != null ? lpj.getLpj12().doubleValue() : 0.0)
                .lpj14(lpj.getLpj12() != null ? lpj.getLpj14().doubleValue() : 0.0)
                .lpj15(lpj.getLpj12() != null ? lpj.getLpj15().doubleValue() : 0.0)
                .taLpj02(lpj.getLpj12() != null ? lpj.getTaLpj02().doubleValue() : 0.0)
                .taLpj03(lpj.getLpj12() != null ? lpj.getTaLpj03().doubleValue() : 0.0)
                .build();
    }
    
    // =========================================================================
    // doHouseHold
    // =========================================================================
    @Transactional(rollbackFor = Exception.class)
    public LpjFile doHouseHold(String sTempMemberID, String sID) throws Exception {
        log.info("doHouseHold 開始：sTempMemberID={}, sID={}", sTempMemberID, sID);

        LpjFile tempBean = memberRepository.getPointByMemberID(sTempMemberID);
        if (!"000".equals(tempBean.getLpj02())) {
            throw new Exception("Can not do household : Not 000, TempMemberID -> "
                    + sTempMemberID + ", ID -> " + sID);
        }

        LpjFile targetBean = memberRepository.getMemberDirectly(sID);

		if (targetBean == null || targetBean.getLpj03() == null) {
		    throw new Exception("查無此會員或該會員無主卡資訊");
		}

        String mainCardNo = targetBean.getLpj03();
        if (!mainCardNo.startsWith("7708")
                && !mainCardNo.startsWith("EC")
                && !mainCardNo.startsWith("APP")
                && !mainCardNo.startsWith("TS")) {
            throw new Exception("Can not do household : The main card is not 7708 or APP, Card ID -> "
                    + mainCardNo + ", ID -> " + sID);
        }

        String tempLpj01   = tempBean.getLpj01();
        String targetLpj01 = targetBean.getLpj01();
        if (tempLpj01 != null && !tempLpj01.isBlank()
                && targetLpj01 != null && !targetLpj01.isBlank()) {
            log.info("doHouseHold 執行中：sTempMemberID={}, sMemberID={}", sTempMemberID, targetLpj01);
            memberRepository.doHouseHold(sTempMemberID, tempBean, targetLpj01, mainCardNo);
        } else {
            throw new Exception("Can not do household : TempMemberID -> "
                    + sTempMemberID + ", sID -> " + sID);
        }

        try {
            bonusServiceClient.updateLsmCardId(sTempMemberID, mainCardNo);
            log.info("成功通知 Bonus MS 轉移卡號: {} -> {}", sTempMemberID, mainCardNo);
        } catch (Exception e) {
            log.error("通知 Bonus MS 轉移卡號失敗", e);
            // 這裡建議拋出例外，確保事務回滾，保證資料一致性
            throw new Exception("卡號轉移失敗，請確認 Bonus MS 服務狀態");
        }
        
        return memberRepository.getPointByID(sID);
    }

    // =========================================================================
    // doFormal
    // =========================================================================
    @Transactional(rollbackFor = Exception.class)
    public void doFormal(String sTempMemberID, String sName, String sID,
                          String sBirthday, String sMobile, String sAddress,
                          String sEmail) throws Exception {
        log.info("doFormal 開始：sTempMemberID={}, sName={}, sID={}", sTempMemberID, sName, sID);

        LpjFile lpjBean = memberRepository.getPointByMemberID(sTempMemberID);
        if (!"000".equals(lpjBean.getLpj02())) {
            throw new Exception("The user is not 000, can not doFormal : TempMemberID -> "
                    + sTempMemberID + ", ID -> " + sID);
        }

        List<LpjFile> existingCards = memberRepository.getAllCardByID(sID);
        if (!existingCards.isEmpty()) {
            throw new Exception("The user ID existed, can not doFormal : TempMemberID -> "
                    + sTempMemberID + ", ID -> " + sID);
        }

        memberRepository.doFormal(sTempMemberID, sName, sID, sBirthday, sMobile, sAddress, sEmail);
        log.info("doFormal 完成：sTempMemberID={}, sID={}", sTempMemberID, sID);
    }

    // =========================================================================
    // getMemberContact
    // =========================================================================
    @Transactional(readOnly = true)
	public MemberContactDTO getMemberContact(String sMemberID) {
        // 1. 取得該會員名下所有的卡號 (使用 repository 中已存在的 findCardNosByLpj01)
        List<String> cardNos = lpjFileRepository.findCardNosByLpj01(sMemberID);
        
        // 如果該會員沒有卡號，進行防呆處理
        if (cardNos == null || cardNos.isEmpty()) {
            throw new RuntimeException("該會員無對應卡號: " + sMemberID);
        }
        
        // 假設取第一張卡號進行統計 (或是您需要遍歷所有卡號加總)
        String primaryCardNo = cardNos.get(0); 

        // 2. 透過 Feign 向 Bonus 微服務「索取」該卡號的統計結果
        MemberStatsDTO stats = bonusClient.getMemberStats(primaryCardNo, "2026-01-01", "2026-06-09");
        
        // 3. 取得會員主檔資料 (依據您的需求組合)
        LpkFile lpk = lpkFileRepository.findById(sMemberID).orElse(new LpkFile());
        
        int vipLevel = ("62".equals(lpk.getLpkud02()) || "66".equals(lpk.getLpkud02())) ? 1 : 0;
        
        return MemberContactDTO.builder()
                .lpkFile(lpk)
                .totalLsm08(stats != null ? stats.getTotalLsm08() : 0.0)
                .vipLevel(vipLevel)
                .build();
    }
    
 // =========================================================================
    // getMemberContactByID
    // =========================================================================
    @Transactional(readOnly = true)
    public MemberContactDTO getMemberContactByID(String sID) {
        // 1. 取得會員基本資訊 (您剛完成的 JPA 查詢)
        LpkFile lpk = memberRepository.findMemberByCardNo(sID); 
        
        // 2. 向 Bonus 微服務請求點數統計 (解耦關鍵)
        // 原始 SQL 的邏輯：SELECT SUM(lsm08)... 
        // 現在由 Bonus 服務透過其內部的 LSM 表計算並回傳
        MemberStatsDTO stats = bonusClient.getMemberStats(sID, "2026-01-01", "2026-12-31");
        
        // 3. 在 Member 端進行最後的「VIP 等級」邏輯計算 (遵循 SRP 原則)
        int vipLevel = ("62".equals(lpk.getLpkud02()) || "66".equals(lpk.getLpkud02())) ? 1 : 0;
        
        // 4. 組合回傳
        return MemberContactDTO.builder()
                .lpkFile(lpk)
                .totalLsm08(stats.getTotalLsm08())
                .vipLevel(vipLevel)
                .build();
    }
    
    // =========================================================================
    // getMemberContactByCardID
    // =========================================================================
    @Transactional(readOnly = true)
    public MemberDTO.MemberContactResponse getMemberContactByCardID(String sCardID) {
        // 1. 取得會員基本資訊
        LpkFile lpk = memberRepository.getMemberContactByCardID(sCardID);
        if (lpk == null || lpk.getLpk01() == null) {
            return null;
        }

        // 2. 透過 Feign 向 Bonus 服務請求點數統計 (補回這段缺失的邏輯)
        // 注意：這裡的日期參數建議改為動態或設定在 Constants 中
        MemberStatsDTO stats = bonusClient.getMemberStats(
                sCardID, 
                LocalDate.now().minusYears(1).withDayOfYear(1).toString(), 
                LocalDate.now().minusYears(1).withMonth(12).withDayOfMonth(31).toString()
        );
        
        log.info("Feign 呼叫結果：sCardID={}, stats={}", sCardID, stats);

        if (stats == null) {
            log.warn("Bonus 服務回傳為 null，無法計算點數");
            // 如果這裡回傳 null，API 的 data 就會是 null
        }

        // 3. 計算 VIP 等級 (這段邏輯必須保留在 Service 層以符合 SRP)
        int vipLevel = ("62".equals(lpk.getLpkud02()) || "66".equals(lpk.getLpkud02())) ? 1 : 0;

        // 4. 封裝並回傳
        return MemberDTO.MemberContactResponse.builder()
                .lpk01(lpk.getLpk01())
                .lpk03(lpk.getLpk03())
                .lpk04(lpk.getLpk04())
                .lpk05(lpk.getLpk05() != null ? lpk.getLpk05().toString() : "")
                .lpk14(lpk.getLpk14())
                .lpk15(lpk.getLpk15())
                .lpk18(lpk.getLpk18())
                .lpk19(lpk.getLpk19())
                .lpkUd02(lpk.getLpkud02())
                .totalLsm08(stats.getTotalLsm08()) // 補上點數欄位
                .vipLevel(String.valueOf(vipLevel)) // 補上正確的 VIP 等級計算
                .build();
    }
    
 // =========================================================================
    // getMainCard - 透過卡號取得主卡資訊
    // =========================================================================
    @Transactional(readOnly = true)
    public MemberDTO.MainCardResponse getMainCard(String sCardID) {
        // 1. 呼叫 Repository 取得 Entity
        LpkFile lpk = memberRepository.getMainCard(sCardID);
        
        // 2. 防禦性檢查：若查不到資料則回傳 null (或可拋出自定義 Exception)
        if (lpk == null) {
            return null;
        }

        // 3. 轉換為 DTO 回傳
        return MemberDTO.MainCardResponse.builder()
                .lpk04(lpk.getLpk04()) // 姓名
                .lpj03(sCardID)        // 卡號 (假設回傳入參的卡號)
                .build();
    }

    // =========================================================================
    // 供內部微服務 RPC 使用：僅回傳卡號清單
    // =========================================================================
    @Transactional(readOnly = true)
    public List<String> getCardNumbersByMemberID(String sMemberID) {
        log.info("內部 RPC 呼叫：查詢會員 {} 的所有卡號", sMemberID);
        
        // 假設 Repository 已有此方法，若無請參考下方步驟 2
        List<LpjFile> cardList = memberRepository.getAllCardByMemberID(sMemberID);
        
        if (cardList == null || cardList.isEmpty()) {
            return Collections.emptyList();
        }

        // 只提取 LPJ03 (卡號) 欄位並轉為 List<String>
        return cardList.stream()
                .map(LpjFile::getLpj03)
                .filter(cardNo -> cardNo != null && !cardNo.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }  
    
    // =========================================================================
    // getMemberByCardID (保留底層流程調用)
    // =========================================================================
    @Transactional(readOnly = true)
    public LpjFile getMemberByCardID(String cardID) {
        return lpjFileRepository.findByLpj03AndLpj02(cardID, "000")
                .orElseThrow(() -> new RuntimeException("找不到指定卡號的進場會員保留資料"));
    }
    
    // =========================================================================
    // getMemberByCardID2
    // =========================================================================
    @Transactional(readOnly = true)
    public LpkFile getMemberByCardID2(String sCardID) throws Exception {
        return memberRepository.getMemberByCardID2(sCardID);
    }

    // =========================================================================
    // getMemberByCardID3
    // =========================================================================
    @Transactional(readOnly = true)
    public LpkFile getMemberByCardID3(String sCardID) throws Exception {
        return memberRepository.getMemberByCardID3(sCardID);
    }

    // =========================================================================
    // getMemberByCardID4
    // =========================================================================
    @Transactional(readOnly = true)
    public LpkFile getMemberByCardID4(String sCardID) throws Exception {
        return memberRepository.getMemberByCardID4(sCardID);
    }

    // =========================================================================
    // is000
    // =========================================================================
    @Transactional(readOnly = true)
    public boolean is000(String sCardID) throws Exception {
        return memberRepository.is000(sCardID);
    }
}