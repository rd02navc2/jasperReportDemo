package com.beyond.report.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.report.bean.ResponseBean;
import com.beyond.report.entity.ASSET_INVENTORY;
import com.beyond.report.entity.ASSET_INVENTORY_HEADER;
import com.beyond.report.entity.ASSET_SCRAPPED;
import com.beyond.report.entity.ASSET_TYPE;
import com.beyond.report.entity.EMPLOYEE;
import com.beyond.report.projection.AssetInventoryProjection;
import com.beyond.report.repository.AssetInventoryHeaderRepository;
import com.beyond.report.repository.AssetInventoryRepository;
import com.beyond.report.repository.EmployeeRepository;
import com.beyond.report.util.GetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetInventoryHeaderRepository assetInventoryHeaderRepository;
    private final AssetInventoryRepository assetInventoryRepository;
    private final EmployeeRepository employeeRepository;
    
    private static final Set<String> ALLOWED_SIDX = Set.of("self_no", "model_no", "dept_name", "owner_name", "inventory_date");

    // ==================== 查詢方法 ====================

    /**
     * 取得所有盤點日期 (依日期降序)
     */
    @Transactional(readOnly = true)
    public List<ASSET_INVENTORY_HEADER> getInventoryDate() {
        log.info("開始查詢資產盤點日期清單");
        return assetInventoryHeaderRepository.findAllOrderByInventoryDateDesc();
    }

    /**
     * 取得特定盤點日期的資產清單
     */
    @Transactional(readOnly = true)
    public List<AssetInventoryProjection> getAssetInventory(String inventoryDate, String sidx, String sord) throws Exception {
        // 白名單校驗與預設值處理
        String safeSidx = (sidx != null && ALLOWED_SIDX.contains(sidx.toLowerCase())) ? sidx.toLowerCase() : "inventory_date";
        String safeSord = ("DESC".equalsIgnoreCase(sord)) ? "DESC" : "ASC";

        if ("dept_name".equals(safeSidx)) {
            return assetInventoryRepository.findInventoryOrderByDept(inventoryDate, safeSidx, safeSord);
        } else {
            return assetInventoryRepository.findInventoryOrderGeneral(inventoryDate, safeSidx, safeSord);
        }
    }

    /**
     * 取得所有資產類型
     */
    @Transactional(readOnly = true)
    public List<ASSET_TYPE> getAssetType() {
        return assetInventoryRepository.getAssetType();
    }

    /**
     * 依資產編號查詢最新盤點資料
     */
    @Transactional(readOnly = true)
    public ASSET_INVENTORY getAssetInventoryBySelfNo(String selfNo) {
        return assetInventoryRepository.findLatestBySelfNo(selfNo);
    }

    /**
     * 依員工編號查詢資產清單
     */
    @Transactional(readOnly = true)
    public List<ASSET_INVENTORY> getAssetInventoryByCode(String code, String sidx, String sord) {
        return assetInventoryRepository.findByOwnerId(code);
    }

    /**
     * 依員工登入ID查詢員工資料
     */
    @Transactional(readOnly = true)
    public EMPLOYEE getEmployeeById(String loginId) {
        return employeeRepository.findById(loginId).orElse(null);
    }

    /**
     * 依員工代碼查詢員工資料
     */
    @Transactional(readOnly = true)
    public EMPLOYEE getEmployeeByCode(String employeeCode) {
        return employeeRepository.findByCode(employeeCode);
    }

    /**
     * 取得最新盤點狀態
     */
    @Transactional(readOnly = true)
    public ASSET_INVENTORY_HEADER getStaffAssetStatus() {
        return assetInventoryHeaderRepository.findLatestInventoryStatus();
    }

    /**
     * 取得報廢資產清單
     */
    @Transactional(readOnly = true)
    public List<ASSET_SCRAPPED> getAssetScrapped(String fromDate, String endDate, String selfNo, String sidx, String sord) {
        String safeSidx = (sidx != null && ALLOWED_SIDX.contains(sidx.toLowerCase())) ? sidx.toLowerCase() : "self_no";
        String safeSord = ("DESC".equalsIgnoreCase(sord)) ? "DESC" : "ASC";
        
        return assetInventoryRepository.findScrappedAssets(fromDate, endDate, selfNo, safeSidx, safeSord);
    }

    /**
     * 取得尚未確認的員工清單
     */
    @Transactional(readOnly = true)
    public List<ASSET_INVENTORY> getInformStaff() {
        return assetInventoryRepository.findUnconfirmedStaff();
    }

    /**
     * 取得員工郵件清單
     */
    @Transactional(readOnly = true)
    public List<EMPLOYEE> getStaffMail(List<ASSET_INVENTORY> assetList) {
        if (assetList == null || assetList.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> ownerIds = assetList.stream()
                .map(ASSET_INVENTORY::getOwner_id)
                .filter(id -> id != null && !id.isEmpty())
                .collect(Collectors.toList());
        
        if (ownerIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        return employeeRepository.findByEmployeeIds(ownerIds);
    }

    // ==================== 驗證方法 ====================

    /**
     * 驗證是否可以發起新的盤點
     */
    @Transactional(readOnly = true)
    public ResponseBean isValidate() {
        ResponseBean bean = new ResponseBean();
        String today = GetDateTime.getTodayDateW("-");
        
        try {
            ASSET_INVENTORY_HEADER latest = assetInventoryHeaderRepository.findLatestInventoryStatus();
            
            if (latest != null && latest.getInventory_date() != null) {
                // 檢查是否有未結案的盤點
                if (latest.getClose_date() == null) {
                    bean.setCode("9999");
                    bean.setMessage("上一次的資產盤點尚未結案");
                    return bean;
                }
                
                // 檢查今日是否已有盤點
                if (today.equals(latest.getInventory_date())) {
                    bean.setCode("9999");
                    bean.setMessage("今日已有產生資產盤點資料");
                    return bean;
                }
            }
            
            bean.setCode("0000");
            
        } catch (Exception e) {
            log.error("驗證盤點狀態失敗: ", e);
            bean.setCode("9999");
            bean.setMessage("驗證失敗：" + e.getMessage());
        }
        
        return bean;
    }

    // ==================== 新增/儲存方法 ====================

    /**
     * 儲存匯入的資產資料
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveImport(JSONArray jaData, String loginId) throws Exception {
        String today = GetDateTime.getTodayDateW("-");
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // 檢查今日是否已有盤點
        ASSET_INVENTORY_HEADER existing = assetInventoryHeaderRepository.findByInventoryDate(today);
        if (existing != null) {
            throw new Exception("今日已有資產盤點資料");
        }

        // 1. 插入 Header
        ASSET_INVENTORY_HEADER header = new ASSET_INVENTORY_HEADER();
        header.setInventory_date(today);
        header.setGenerate_type("1"); // 匯入
        header.setClose_date(now);
        header.setAccess_id(loginId);
        header.setAccess_date(now);
        header.setRemark("匯入資產盤點資料");
        assetInventoryHeaderRepository.save(header);

        // 2. 處理每一筆資產資料
        for (int i = 0; i < jaData.length(); i++) {
            JSONObject jo = jaData.getJSONObject(i);
            
            // 安全取得各欄位 (避免 JSON 缺少 key 拋出 Exception)
            String rawType = jo.optString("type", "");
            String typeId = extractTypeId(rawType);
            String selfNo = jo.optString("self_no", "");
            String modelNo = jo.optString("model_no", "");
            String prodDesc = jo.optString("prod_desc", "");
            String ownerId = jo.optString("owner_id", "");
            
            // 處理維護紀錄
            String maintainHist = jo.optString("maintain_hist", "");
            if (!maintainHist.isEmpty() && !selfNo.isEmpty()) {
                saveMaintainHist(selfNo, maintainHist, loginId);
            }
            
            // 取得員工資料
            EMPLOYEE emp = null;
            if (!ownerId.isEmpty()) {
                emp = getEmployeeByCode(ownerId);
            }

            // 插入資產明細
            ASSET_INVENTORY asset = new ASSET_INVENTORY();
            asset.setInventory_date(today);
            asset.setSelf_no(selfNo);
            asset.setModel_no(modelNo);
            asset.setType(typeId);
            asset.setProd_desc(prodDesc);
            asset.setOwner_id(ownerId);
            asset.setOwner_name(emp != null ? emp.getCNNAME() : null);
            asset.setTitle(emp != null ? emp.getTITLE() : null);
            asset.setDept_name(emp != null ? emp.getDEPT_NAME() : null);
            
            assetInventoryRepository.save(asset);
        }
        
        log.info("匯入資產盤點資料完成，共 {} 筆", jaData.length());
    }

    /**
     * 儲存/更新資產盤點資料
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveAsset(String inventoryDate, JSONArray jaData, String loginId) throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < jaData.length(); i++) {
            JSONObject jo = jaData.getJSONObject(i);
            
            // 使用 optString 避免 JSONKeyNotFoundException
            String rawType = jo.optString("type", "");
            String typeId = extractTypeId(rawType);
            String selfNo = jo.optString("self_no", "");
            String modelNo = jo.optString("model_no", "");
            String prodDesc = jo.optString("prod_desc", "");
            String ownerId = jo.optString("owner_id", "");
            
            if (selfNo.isEmpty()) {
                continue; // 若無 self_no 則跳過
            }

            // 處理維護紀錄
            String maintainHist = jo.optString("maintain_hist", "");
            if (!maintainHist.isEmpty()) {
                saveMaintainHist(selfNo, maintainHist, loginId);
            }
            
            // 處理備註紀錄
            String memoHist = jo.optString("memo_hist", "");
            if (!memoHist.isEmpty()) {
                saveMemoHist(selfNo, memoHist, loginId);
            }
            
            // 取得員工資料
            EMPLOYEE emp = null;
            if (!ownerId.isEmpty()) {
                emp = getEmployeeByCode(ownerId);
            }

            // 檢查資產是否存在，決定新增或更新
            ASSET_INVENTORY existing = assetInventoryRepository.findByInventoryDateAndSelfNo(inventoryDate, selfNo);
            
            if (existing != null) {
                // 更新
                existing.setModel_no(modelNo);
                existing.setType(typeId);
                existing.setProd_desc(prodDesc);
                existing.setOwner_id(ownerId);
                existing.setOwner_name(emp != null ? emp.getCNNAME() : null);
                existing.setTitle(emp != null ? emp.getTITLE() : null);
                existing.setDept_name(emp != null ? emp.getDEPT_NAME() : null);
                assetInventoryRepository.save(existing);
            } else {
                // 新增
                ASSET_INVENTORY asset = new ASSET_INVENTORY();
                asset.setInventory_date(inventoryDate);
                asset.setSelf_no(selfNo);
                asset.setModel_no(modelNo);
                asset.setType(typeId);
                asset.setProd_desc(prodDesc);
                asset.setOwner_id(ownerId);
                asset.setOwner_name(emp != null ? emp.getCNNAME() : null);
                asset.setTitle(emp != null ? emp.getTITLE() : null);
                asset.setDept_name(emp != null ? emp.getDEPT_NAME() : null);
                assetInventoryRepository.save(asset);
            }
        }

        // 更新 Header 備註
        ASSET_INVENTORY_HEADER header = assetInventoryHeaderRepository.findByInventoryDate(inventoryDate);
        if (header != null) {
            header.setRemark("異動盤點資料");
            header.setAccess_id(loginId);
            header.setAccess_date(now);
            assetInventoryHeaderRepository.save(header);
        }
        
        log.info("儲存資產盤點資料完成，共 {} 筆", jaData.length());
    }

    /**
     * 發起新的資產盤點
     */
    @Transactional(rollbackFor = Exception.class)
    public void newAsset(String loginId) throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String today = GetDateTime.getTodayDateW("-");

        // 1. 複製上一期的資產資料
        assetInventoryRepository.copyAssetsFromPrevious(today);

        // 2. 插入新的 Header
        ASSET_INVENTORY_HEADER header = new ASSET_INVENTORY_HEADER();
        header.setInventory_date(today);
        header.setGenerate_type("2"); // 發起盤點
        header.setAccess_id(loginId);
        header.setAccess_date(now);
        header.setRemark("發起資產盤點");
        assetInventoryHeaderRepository.save(header);
        
        log.info("發起新盤點成功，盤點日期：{}", today);
    }

    // ==================== 刪除方法 ====================

    /**
     * 刪除特定盤點日期的所有資料
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(String inventoryDate) throws Exception {
        // 先刪除明細
        assetInventoryRepository.deleteByInventoryDate(inventoryDate);
        // 再刪除 Header
        assetInventoryHeaderRepository.deleteByInventoryDate(inventoryDate);
        log.info("刪除盤點資料完成，盤點日期：{}", inventoryDate);
    }

    /**
     * 刪除單一資產
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String inventoryDate, String selfNo, String loginId) throws Exception {
        // 刪除資產
        assetInventoryRepository.deleteByInventoryDateAndSelfNo(inventoryDate, selfNo);
        
        // 更新 Header 備註
        ASSET_INVENTORY_HEADER header = assetInventoryHeaderRepository.findByInventoryDate(inventoryDate);
        if (header != null) {
            header.setRemark("刪除資產編號：" + selfNo);
            header.setAccess_id(loginId);
            header.setAccess_date(new Timestamp(System.currentTimeMillis()));
            assetInventoryHeaderRepository.save(header);
        }
        
        log.info("刪除資產成功，資產編號：{}，盤點日期：{}", selfNo, inventoryDate);
    }

    /**
     * 報廢資產
     */
    @Transactional(rollbackFor = Exception.class)
    public void scrapped(String inventoryDate, String selfNo, String loginId) throws Exception {
        // 1. 查詢資產資料
        ASSET_INVENTORY asset = assetInventoryRepository.findByInventoryDateAndSelfNo(inventoryDate, selfNo);
        if (asset == null) {
            throw new Exception("找不到資產編號：" + selfNo);
        }

        // 2. 插入報廢紀錄 - 修正為使用個別參數版本
        Timestamp now = new Timestamp(System.currentTimeMillis());
        assetInventoryRepository.saveScrapped(
            selfNo,
            asset.getModel_no(),
            asset.getType(),
            asset.getProd_desc(),
            asset.getOwner_id(),
            asset.getOwner_name(),
            asset.getTitle(),
            asset.getDept_name(),
            loginId,
            now
        );

        // 3. 刪除資產
        assetInventoryRepository.deleteByInventoryDateAndSelfNo(inventoryDate, selfNo);
        
        log.info("報廢資產成功，資產編號：{}", selfNo);
    }

    /**
     * 提交資產確認
     */
    @Transactional(rollbackFor = Exception.class)
    public void submit(String code) throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        // 1. 取得最新盤點日期
        ASSET_INVENTORY_HEADER latest = assetInventoryHeaderRepository.findLatestInventoryStatus();
        if (latest == null) {
            throw new Exception("找不到進行中的盤點");
        }
        
        String inventoryDate = latest.getInventory_date();

        // 2. 更新確認狀態
        assetInventoryRepository.confirmAsset(code, code, now, inventoryDate);

        // 3. 檢查是否所有人都已確認
        List<ASSET_INVENTORY> unconfirmed = assetInventoryRepository.findUnconfirmedByInventoryDate(inventoryDate);
        
        if (unconfirmed.isEmpty()) {
            // 關閉盤點
            assetInventoryHeaderRepository.updateHeaderClose("資產盤點已完成", now, inventoryDate);
            log.info("資產盤點已完成，盤點日期：{}", inventoryDate);
        }
        
        log.info("員工 {} 確認資產完成", code);
    }

    // ==================== 私有輔助方法 ====================

    /**
     * 安全剖析 TypeId，防止空白或無空格字串導致 Exception
     */
    private String extractTypeId(String rawType) {
        if (rawType == null || rawType.trim().isEmpty()) {
            return "";
        }
        return rawType.trim().split(" ")[0];
    }

    /**
     * 儲存維護紀錄 - 使用個別參數版本
     */
    private void saveMaintainHist(String selfNo, String maintainHist, String loginId) {
        if (maintainHist == null || maintainHist.trim().isEmpty()) {
            return;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        if (assetInventoryRepository.existsMaintainHist(selfNo)) {
            // 更新已存在的紀錄
            assetInventoryRepository.updateMaintainHist(maintainHist, loginId, now, selfNo);
        } else {
            // 新增紀錄
            assetInventoryRepository.saveMaintainHist(selfNo, maintainHist, loginId, now);
        }
    }

    /**
     * 儲存備註紀錄 - 使用個別參數版本
     */
    private void saveMemoHist(String selfNo, String memoHist, String loginId) {
        if (memoHist == null || memoHist.trim().isEmpty()) {
            return;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        if (assetInventoryRepository.existsMemoHist(selfNo)) {
            // 更新已存在的紀錄
            assetInventoryRepository.updateMemoHist(memoHist, loginId, now, selfNo);
        } else {
            // 新增紀錄
            assetInventoryRepository.saveMemoHist(selfNo, memoHist, loginId, now);
        }
    }
}