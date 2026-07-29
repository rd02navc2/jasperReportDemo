package com.beyoung.surrounding.member.service;

import com.beyoung.surrounding.member.dto.MemberDTO;
import com.beyoung.surrounding.app.entity.LPJ_FILE;
import com.beyoung.surrounding.member.repository.LpjFileRepository;
import com.beyoung.surrounding.app.entity.LPK_FILE;
import com.beyoung.surrounding.member.repository.LpkFileRepository;
import com.beyoung.surrounding.member.repository.LsmFileRepository;
import com.beyoung.surrounding.member.repository.MemberCustomProjection;
import com.beyoung.surrounding.app.entity.LSM_FILE;
import com.beyoung.surrounding.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 會員服務層
 * 完整整合 MemberRes.java 的所有業務邏輯
 * 升級至 Java 21 / Spring Boot 3.4.3
 * 全面重構：落實標準 Java 小駝峰式命名規範
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
    private final LsmFileRepository lsmFileRepository;
    
    @Transactional(readOnly = true)
    public boolean isExistLpk(String memberId) throws Exception {
        boolean exists = memberRepository.isExistLpk(memberId);
        return exists; 
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTempMember(String center, String memberId, String cardId) throws Exception {
        log.info("開始執行 addTempMember 業務邏輯: center -> {}, memberId -> {}, cardId -> {}", center, memberId, cardId);
        
        try {
            memberRepository.addTempMember(center, memberId, cardId);
            log.info("臨時會員新增成功: memberId -> {}", memberId);
        } catch (Exception e) {
            log.error("新增臨時會員失敗，觸發事務回滾: memberId -> {}, 錯誤原因: {}", memberId, e.getMessage());
            throw e; 
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRsTempMember(String center, String memberId) throws Exception {
        log.info("準備新增 RS 臨時會員：center -> {}, memberId -> {}", center, memberId);

        if (memberRepository.isExistLpk(memberId)) {
            log.warn("新增 RS 臨時會員失敗：{} 已存在於系統中", memberId);
            throw new IllegalArgumentException(memberId + " 已存在，無法新增RS臨時會員");
        }

        try {
            memberRepository.addRsTempMember(center, memberId, memberId);
            log.info("addRsTempMember 處理完成：center -> {}, memberId -> {}", center, memberId);
        } catch (Exception e) {
            log.error("新增 RS 臨時會員時發生資料庫異常：", e);
            throw e; 
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMemberContact(String memberId, String mobile, String email, String addr) throws Exception {
        if (!memberRepository.isExistLpk(memberId)) {
            throw new Exception(memberId + " 不存在，無法更新聯絡資料");
        }
        memberRepository.updateMemberContact(memberId, mobile, email, addr);
        log.info("updateMemberContact 完成：memberId={}", memberId);
    }

    @Transactional(readOnly = true)
    public List<MemberDTO.CardDetailResponse> getAllCardByMemberIdDto(String memberId) {
        List<LPJ_FILE> cardList = memberRepository.getAllCardByMemberId(memberId);
        
        if (cardList == null || cardList.isEmpty()) {
            return Collections.emptyList();
        }

        List<MemberDTO.CardDetailResponse> result = new ArrayList<>();

        for (LPJ_FILE lpj : cardList) {
            MemberDTO.CardDetailResponse dto = MemberDTO.CardDetailResponse.builder()
                .lpj01(lpj.getLpj01())
                .lpj02(lpj.getLpj02())
                .lpj03(lpj.getLpj03())
                .lpj04(lpj.getLpj04() != null ? lpj.getLpj04().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null)
                .lpj06(lpj.getLpj06())
                .lpj07(lpj.getLpj07())
                .lpj09(lpj.getLpj09())
                .lpj12(lpj.getLpj12())
                .build();
            
            result.add(dto);
        }

        return result;
    }
    
    @Transactional(readOnly = true)
    public List<LPJ_FILE> getAllCardById(String id) throws Exception {
        log.info("業務邏輯 - 查詢會員所有卡片實體：id -> {}", id);
        List<LPJ_FILE> cardList = memberRepository.getAllCardById(id);
        return cardList != null ? cardList : Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public LPJ_FILE getPointByMemberId(String memberId) {
        log.info("業務邏輯 - 查詢會員Id點數實體：memberId -> {}", memberId);

        List<LPJ_FILE> cardList = memberRepository.getAllCardByMemberId(memberId);
        
        if (cardList == null || cardList.isEmpty()) {
            log.warn("查詢點數失敗：會員 {} 查無任何卡片紀錄", memberId);
            return null;
        }
        
        return cardList.get(0);
    }

    @Transactional(readOnly = true)
    public LPJ_FILE getPointById(String id) {
        log.info("業務邏輯 - 查詢身分證Id點數實體：id -> {}", id);
        LPJ_FILE lpj = memberRepository.getPointById(id);
        return lpj;
    }
    
    /**
     * 執行會員歸戶作業 (doHousehold)
     * * @param tempMemberId 臨時會員 ID (原 sTempMemberID)
     * @param id 正式會員身分證字號 (原 sID)
     * @return LpjFile 正式會員歸戶後的最新點數會卡實體
     * @throws Exception 歸戶檢核失敗或資料庫異動異常時拋出
     */
    @Transactional(rollbackFor = Exception.class)
    public LPJ_FILE doHousehold(LPJ_FILE tempMemberCard, String id) throws Exception {
        // 防呆檢查
        if (tempMemberCard == null || tempMemberCard.getLpj01() == null) {
            throw new Exception("傳入的臨時會員卡片物件或會員ID為空");
        }
        
        // 從傳入的物件中直接取得 tempMemberId 字串
        String tempMemberId = tempMemberCard.getLpj01(); 
        
        log.info("doHousehold 業務邏輯開始：tempMemberId -> {}, id -> {}", tempMemberId, id);

        // 1. 取得臨時會員的卡片清單，驗證是否存在
        List<LPJ_FILE> tempCardList = memberRepository.getAllCardByMemberId(tempMemberId);
        if (tempCardList == null || tempCardList.isEmpty()) {
            throw new Exception("無法執行歸戶：找不到臨時會員的卡片資料 -> " + tempMemberId);
        }
        
        // 取出臨時會員的第一筆卡片實體，並驗證其帳戶狀態是否為臨時狀態 '000'
        LPJ_FILE tempBean = tempCardList.get(0);
        if (!"000".equals(tempBean.getLpj02())) {
            throw new Exception("無法執行歸戶：該會員帳戶狀態不是 000, tempMemberId -> " 
                    + tempMemberId + ", id -> " + id);
        }

        // 2. 取得正式會員（依據身分證字號）的點數帳戶資訊
        LPJ_FILE targetBean = memberRepository.getMemberDirectly(id);
        if (targetBean == null || targetBean.getLpj03() == null) {
            throw new Exception("無法執行歸戶：查無此正式會員或該會員目前無主卡資訊 -> " + id);
        }

        // 3. 檢查正式會員的主卡卡號前綴是否符合特規格式
        String mainCardId = targetBean.getLpj03();
        if (!mainCardId.startsWith("7708")
                && !mainCardId.startsWith("EC")
                && !mainCardId.startsWith("APP")
                && !mainCardId.startsWith("TS")) {
            throw new Exception("無法執行歸戶：正式會員的主卡號非 7708、EC、APP 或 TS 開頭, mainCardId -> " 
                    + mainCardId + ", id -> " + id);
        }

        // 4. 安全檢核：驗證雙方的會員系統內部 ID 皆不可為空
        String tempLpj01 = tempBean.getLpj01();
        String targetLpj01 = targetBean.getLpj01();
        if (tempLpj01 == null || tempLpj01.isBlank() || targetLpj01 == null || targetLpj01.isBlank()) {
            throw new Exception("無法執行歸戶：臨時會員或正式會員的內部系統 ID 遺失, tempMemberId -> " 
                    + tempMemberId + ", id -> " + id);
        }

        log.info("通過核心檢核，開始進行資料庫跨表歸戶程序：tempMemberId -> {}, 正式會員內部ID -> {}", tempMemberId, targetLpj01);
        
        // 5. 呼叫 Repository 執行多表關聯的資料庫異動 (落實小駝峰命名)
        memberRepository.doHousehold(tempMemberId, tempBean, targetLpj01, mainCardId);

        log.info("歸戶資料庫異動成功，重新查詢正式會員最新點數狀態：id -> {}", id);

        // 6. 歸戶程序順利完成，重新查詢並回傳正式會員最新的會卡與點數狀態
        LPJ_FILE updatedFormalLpj = memberRepository.getPointById(id);
        
        log.info("doHousehold 業務邏輯執行完畢。");
        return updatedFormalLpj;
    }

    @Transactional(rollbackFor = Exception.class)
    public void doFormal(String tempMemberId, String name, String id,
                         String birthday, String mobile, String address,
                         String email) throws Exception {
        
        log.info("doFormal 開始：tempMemberId={}, name={}, id={}", tempMemberId, name, id);

        LPJ_FILE lpjBean = memberRepository.getPointByMemberId(tempMemberId);
        if (!"000".equals(lpjBean.getLpj02())) {
            throw new Exception("The user is not 000, can not doFormal : tempMemberId -> "
                    + tempMemberId + ", id -> " + id);
        }

        List<LPJ_FILE> existingCards = memberRepository.getAllCardById(id);
        if (!existingCards.isEmpty()) {
            throw new Exception("The user ID existed, can not doFormal : tempMemberId -> "
                    + tempMemberId + ", id -> " + id);
        }

        memberRepository.doFormal(tempMemberId, name, id, birthday, mobile, address, email);
        log.info("doFormal 完成：tempMemberId={}, id={}", tempMemberId, id);
    }

    @Transactional(readOnly = true)
    public LPK_FILE getMemberContact(String memberId) {
        // 1. 查詢會員基本資料實體
        LPK_FILE lpk = lpkFileRepository.findById(memberId)
                      .orElseThrow(() -> new RuntimeException("會員不存在"));

        // 2. 計算去年整年度的時間區間
        LocalDate startLocalDate = LocalDate.of(LocalDate.now().getYear() - 1, 1, 1);
        LocalDate endLocalDate = LocalDate.of(LocalDate.now().getYear() - 1, 12, 31);
        
        // 🛠️ 關鍵修正：將 LocalDate 安全轉換為 java.util.Date，確保與 Native SQL 參數型態完美對齊
        java.util.Date startDate = java.sql.Date.valueOf(startLocalDate);
        java.util.Date endDate = java.sql.Date.valueOf(endLocalDate);
        
        // 3. 統計點數總和（傳入已經過轉型的 java.util.Date 變數）
        Double totalPoints = lsmFileRepository.sumPointsByMemberID(memberId, startDate, endDate);

        // 4. 計算 VIP 等級
        int vipLevel = ("62".equals(lpk.getLpkud02()) || "66".equals(lpk.getLpkud02())) ? 1 : 0;
        
        // 5. 修正：將計算後的擴充欄位直接塞回原來的 lpk 實體中
        lpk.setTotalLsm08(totalPoints != null ? totalPoints : 0.0);
        lpk.setVipLevel(vipLevel);
        
        // 直接回傳 LpkFile 實體
        return lpk;
    }
    
    @Transactional(readOnly = true)
    public LPK_FILE getMemberContactById(String id) {
        // 1. 取得會員基本資訊
        LPK_FILE lpk = memberRepository.findMemberByCardNo(id);
        if (lpk == null) {
            throw new RuntimeException("找不到該卡號對應的會員: " + id);
        }
        
        // 2. 計算去年整年度的時間區間 (JSR-310 邏輯不變)
        LocalDate startLocalDate = LocalDate.of(LocalDate.now().getYear() - 1, 1, 1);
        LocalDate endLocalDate = LocalDate.of(LocalDate.now().getYear() - 1, 12, 31);
        
        // 關鍵修正：將 LocalDate 轉為 java.util.Date 以符合原生 SQL Repository 的參數型態
        java.util.Date startDate = java.sql.Date.valueOf(startLocalDate);
        java.util.Date endDate = java.sql.Date.valueOf(endLocalDate);
        
        // 3. 統計點數總和（使用已改為 java.util.Date 的原生 SQL Repository）
        Double totalPoints = lsmFileRepository.sumPointsByMemberID(lpk.getLpk01(), startDate, endDate);
        
        // 4. 計算 VIP 等級
        int vipLevel = ("62".equals(lpk.getLpkud02()) || "66".equals(lpk.getLpkud02())) ? 1 : 0;
        
        // 5. 將動態計算資料直接塞回原來的 lpk 實體擴充欄位中
        lpk.setTotalLsm08(totalPoints != null ? totalPoints : 0.0);
        lpk.setVipLevel(vipLevel);
        
        // 正確回傳 LpkFile 實體
        return lpk;
    }

    @Transactional(readOnly = true)
    public LPK_FILE getMemberContactByCardId(String cardId) {
        // 1. 取得會員基本資訊 (對齊小駝峰方法名)
        LPK_FILE lpk = memberRepository.getMemberContactByCardId(cardId);
        if (lpk == null || lpk.getLpk01() == null) {
            return null;
        }

        // 2. 本地化點數統計，設定去年區間
        LocalDate startLocalDate = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate endLocalDate = LocalDate.now().minusYears(1).withMonth(12).withDayOfMonth(31);
        
        // 🛠️ 關鍵修正：將 LocalDate 安全轉換為 java.util.Date，確保與 Native SQL 參數型態百分之百對齊
        java.util.Date startDate = java.sql.Date.valueOf(startLocalDate);
        java.util.Date endDate = java.sql.Date.valueOf(endLocalDate);
        
        // 傳入已經過轉型的 java.util.Date 變數
        Double totalPoints = lsmFileRepository.sumPointsByMemberID(lpk.getLpk01(), startDate, endDate);
        double totalLsm08 = (totalPoints != null) ? totalPoints : 0.0;
        
        log.info("本地查詢結果：cardId={}, totalLsm08={}", cardId, totalLsm08);

        // 3. 計算 VIP 等級
        int vipLevel = ("62".equals(lpk.getLpkud02()) || "66".equals(lpk.getLpkud02())) ? 1 : 0;

        // 4. 將動態統計資料直接塞回原來的 lpk 實體擴充欄位中
        lpk.setTotalLsm08(totalLsm08);
        lpk.setVipLevel(vipLevel);

        // 5. 正確回傳 LpkFile 實體
        return lpk;
    }
    
    @Transactional(readOnly = true)
    public LPK_FILE getMainCard(String cardId) {
        // 1. 呼叫 Repository 取得 LpkFile 實體
    	LPK_FILE lpk = memberRepository.getMainCard(cardId);
        
        // 2. 防禦性檢查：若查不到資料則回傳 null
        if (lpk == null) {
            return null;
        }

        // 3. 修正：直接回傳 LpkFile 實體物件，不轉換為 DTO
        return lpk;
    }
    
    @Transactional(readOnly = true)
    public List<LPJ_FILE> getAllCardByMemberId(String memberId) {
        log.info("內部 RPC 呼叫：查詢會員 {} 的所有卡號", memberId);
        List<LPJ_FILE> cardList = memberRepository.getAllCardByMemberId(memberId);
        return cardList != null ? cardList : Collections.emptyList();
    }
    
    @Transactional(readOnly = true)
    public LPJ_FILE getMemberByCardId(String cardId) {
        return lpjFileRepository.findByLpj03AndLpj02(cardId, "000")
                .orElseThrow(() -> new RuntimeException("找不到指定卡號的進場會員保留資料"));
    }

    @Transactional(readOnly = true)
    public boolean is000(String cardId) throws Exception {
        return memberRepository.is000(cardId);
    }

    public List<LSM_FILE> getPointHistByMemberId(String memberId, String startDate, String endDate) {
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public LPK_FILE getMemberByCardID3(String cardNO) {
        // 1. 透過投影向資料庫查詢特定欄位
        List<MemberCustomProjection> projections = memberRepository.findMemberByCardID3Raw(cardNO);
        
        if (projections == null || projections.isEmpty()) {
            return new LPK_FILE(); 
        }

        // 2. 獲取最後一筆紀錄（對齊原 for 迴圈一直 override 蓋到最後一筆的行為）
        // 善用 Java 21 的 SequencedCollection 特性，直接呼叫 .getLast()
        MemberCustomProjection target = projections.getLast();

        // 3. 映射回實體
        LPK_FILE bean = new LPK_FILE();
        bean.setLpk01(target.getLpk01());
        bean.setLpk03(target.getLpk03());
        bean.setLpk04(target.getLpk04());
        bean.setLpk18(target.getLpk18());
        // 註：若舊 LPK_FILE 的 lpkud02 有配合 Lombok，則使用相應的 Setter
        bean.setLpkud02(target.getLpkud02()); 
        
        return bean;
    }

}