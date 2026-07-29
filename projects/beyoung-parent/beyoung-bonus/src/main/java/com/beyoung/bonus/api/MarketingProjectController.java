package com.beyoung.bonus.api;

import com.beyoung.bonus.infrastructure.LrqFile;
import com.beyoung.bonus.infrastructure.LrqFileId;
import com.beyoung.bonus.application.MarketingProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/Surrounding/api/bonus/Marketing") 
@RequiredArgsConstructor
public class MarketingProjectController {

    private final MarketingProjectService marketingProjectService;
    
    /**
     * 營運人員動態新增或調整活動專案截止日期
     */
    @PostMapping("/addExpiryDate")
    public ResponseEntity<LrqFile> addExpiryDate(@RequestBody LrqFile project) {
        log.info("後台動態變更收到行銷專案效期調整請求 -> 專案:[{}-{}], 據點:[{}], 動態過期日(lrq11):[{}]", 
                project.getLrq01(), project.getLrq02(), project.getLrqplant(), project.getLrq11());
        
        LrqFile updatedProject = marketingProjectService.updateProject(project);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * 新增專案規則 (POST)
     */
    @PostMapping("/addProject")
    public ResponseEntity<LrqFile> addProject(@RequestBody LrqFile project) {
        log.info("API 接收收到後台 [新增] 活動規則請求: {}-{}-{}", 
                project.getLrq01(), project.getLrq02(), project.getLrqplant());
         
        LrqFile savedProject = marketingProjectService.addProject(project);
        return ResponseEntity.ok(savedProject);
    }
    
    /**
     * 更新專案規則 (PUT)
     */
    @PutMapping("/updateProject")
    public ResponseEntity<LrqFile> updateProject(@RequestBody LrqFile project) {
        log.info("[API 接收]收到後台更新活動規則請求: {}-{}-{}", 
                project.getLrq01(), project.getLrq02(), project.getLrqplant());
        
        LrqFile updatedProject = marketingProjectService.updateProject(project);
        return ResponseEntity.ok(updatedProject);
    }

    // =========================================================================
    // member 新增整合：動態客製化滿額贈點門檻 (dynamicThreshold) 管理 API
    // =========================================================================

    /**
     * 營運後台動態變更大額客製化贈點門檻 (PUT)
     * 請求範例 JSON: { "threshold": "50000" }
     */
    @PutMapping("/updateThreshold")
    public ResponseEntity<Map<String, String>> updateThreshold(@RequestBody Map<String, String> payload) {
        String newThreshold = payload.get("threshold");
        log.info("[API 接收] 收到營運後台變更 VIP 客製化加贈門檻請求，目標值: {} 元", newThreshold);
        
        marketingProjectService.updateDynamicThreshold(newThreshold);
        
        return ResponseEntity.ok(Map.of(
                "code", "0",
                "message", "大額客製化點數門檻動態變更成功",
                "currentThreshold", newThreshold
        ));
    }

    /**
     * 查詢當前全域生效的大額贈點門檻值 (GET)
     * 路由：GET /Surrounding/api/bonus/Marketing/getThreshold
     */
    @GetMapping("/getThreshold")
    public ResponseEntity<Map<String, String>> getThreshold() {
        String currentThreshold = marketingProjectService.getDynamicThreshold();
        return ResponseEntity.ok(Map.of("dynamicThreshold", currentThreshold));
    }
}