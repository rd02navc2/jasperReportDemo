package com.beyond.surrounding.dc.service;

import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.dc.entity.VIP_ROOM_LOG;
import com.beyond.surrounding.dc.repositiry.VipRoomLogRepository;
import com.beyond.surrounding.dc.repositiry.VipRoomUnlimitRepository;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VipRoomControlService {
	
	private final VipRoomUnlimitRepository unlimitRepository;
	private final VipRoomLogRepository vipRoomLogRepository;

	@Transactional
    public ResponseBean purchase(String center, String loginId, String cardId, int totalQty, LPK_FILE memberEntity) throws Exception {
        
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(ErrCodeConst.finished);
        responseBean.setMessage(ErrCodeConst.finished_message);    
        
        // 1. 查詢今日該會員的 VIP 房紀錄
        List<VIP_ROOM_LOG> logList = vipRoomLogRepository.findTodayLogs(center, memberEntity.getLpk01());
        
        // 2. 判斷是否有紀錄
        if (!logList.isEmpty()) {
            for (VIP_ROOM_LOG record : logList) {
                
                // 如果 refund_date 為空，代表今日已經進去過且未退款 -> 判定為重複刷卡
                if (record.getRefundDate() == null) {
                    responseBean.setCode(ErrCodeConst.vip_room_rs_repeat);
                    responseBean.setMessage(ErrCodeConst.vip_room_rs_repeat_message);
                    log.info("VipRoom purchase : cardId -> {}, Name -> {} {}", cardId, memberEntity.getLpk04(), ErrCodeConst.vip_room_rs_repeat_message);
                    return responseBean;
                } else {
                    // 如果有退款紀錄，則更新該筆資料狀態，允許重新啟用
                    String currentTime = GetDateTime.getTime(":");
                    vipRoomLogRepository.updateRefundedLog(currentTime, cardId, center, memberEntity.getLpk01());
                }
            }
        } else {
            // 3. 沒有任何紀錄，直接新增一筆 (利用內建的 save，完全不需手動綁定 INSERT 欄位)
            VIP_ROOM_LOG newLog = VIP_ROOM_LOG.builder()
                    .center(center)
                    .transactionDate(new java.util.Date()) // 統一收斂使用 java.util.Date
                    .transactionTime(GetDateTime.getTime(":"))
                    .userId(memberEntity.getLpk01())
                    .userName(memberEntity.getLpk04())
                    .cardNo(cardId)
                    .totalQty(totalQty)
                    .accessId(loginId)
                    .build();
            
            vipRoomLogRepository.save(newLog);
        }
        
        return responseBean;
    }

    @Transactional
    public ResponseBean enter(String center, String cardNO, LPK_FILE entity) throws Exception {
        ResponseBean bean = new ResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        // 處理會員名字遮蔽
        String rawName = entity.getLpk04() != null ? entity.getLpk04().trim() : "";
        StringBuilder myName = new StringBuilder(rawName);
        if (rawName.length() >= 2 && !rawName.equals("臨時會員")) {
            myName.replace(1, 2, "*");
        }
        bean.setUser_name(myName.toString());

        // 1. 檢查是否為無限制員工白名單
        boolean isUnlimit = unlimitRepository.existsByUserId(entity.getLpk01());

        if (!isUnlimit) {
            // 2. 檢查今日是否有扣點/付費主檔紀錄
            List<VIP_ROOM_LOG> activeLogs = vipRoomLogRepository.findTodayActiveLogs(center, entity.getLpk01());

            if (!activeLogs.isEmpty()) {
                // 有點數，更新進入狀態
                vipRoomLogRepository.updateEnterStatus(center, entity.getLpk01());
            } else {
                // 沒點數/未付費
                bean.setCode(ErrCodeConst.vip_room_rs_no_pay);
                bean.setMessage(ErrCodeConst.vip_room_rs_no_pay_message);
                log.info("VipRoom enter : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk04(), ErrCodeConst.vip_room_rs_no_pay_message);
                return bean;
            }
        } else {
            log.info("VipRoom enter : cardNO -> {}, Name -> {} 員工進入VIP室", cardNO, entity.getLpk04());
        }
        return bean;
    }

    @Transactional
    public ResponseBean exit(String sCenter, String sCardNO, LPK_FILE entity) throws Exception {
        ResponseBean bean = new ResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        // 1. 檢查是否為員工
        boolean isUnlimit = unlimitRepository.existsByUserId(entity.getLpk01());

        if (!isUnlimit) {
            // 2. 檢查今日是否有紀錄
            List<VIP_ROOM_LOG> todayLogs = vipRoomLogRepository.findTodayLogs(sCenter, entity.getLpk01());

            if (!todayLogs.isEmpty()) {
                vipRoomLogRepository.updateExitStatus(sCenter, entity.getLpk01());
            }
        } else {
            log.info("VipRoom exit : sCardID -> {}, Name -> {} 使用員工卡離開VIP室", sCardNO, entity.getLpk04());
        }
        return bean;
    }

    @Transactional
    public ResponseBean refund(String center, String loginId, String cardID, LPK_FILE entity) throws Exception {
        ResponseBean bean = new ResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        // 1. 檢查今日所有紀錄
        List<VIP_ROOM_LOG> todayLogs = vipRoomLogRepository.findTodayLogs(center, entity.getLpk01());
        
        if (!todayLogs.isEmpty()) {
            for (VIP_ROOM_LOG record : todayLogs) {
                // 狀態防呆 A：如果已經開門進入過了，不允許退款
                if (record.getEnterDate() != null) {
                    bean.setCode(ErrCodeConst.vip_room_rs_used);
                    bean.setMessage(ErrCodeConst.vip_room_rs_used_message);
                    log.info("VipRoom refund : sCardID -> {}, Name -> {} {}", cardID, entity.getLpk04(), ErrCodeConst.vip_room_rs_used_message);
                    return bean;
                } 
                // 狀態防呆 B：如果先前已經取消過，不重複處理
                else if (record.getRefundDate() != null) {
                    bean.setCode(ErrCodeConst.vip_room_rs_refunded);
                    bean.setMessage(ErrCodeConst.vip_room_rs_refunded_message);
                    log.info("VipRoom refund : sCardID -> {}, Name -> {} {}", cardID, entity.getLpk04(), ErrCodeConst.vip_room_rs_refunded_message);
                    return bean;
                } 
                // 狀態正常：尚未進房且尚未退款，執行退款
                else {
                    vipRoomLogRepository.updateRefundStatus(center, entity.getLpk01());
                }
            }
        } else {
            // 根本沒有購買紀錄
            bean.setCode(ErrCodeConst.vip_room_rs_no_pay);
            bean.setMessage(ErrCodeConst.vip_room_rs_no_pay_message);
            log.info("ReadingSpac refund : cardID -> {}, Name -> {} {}", cardID, entity.getLpk04(), ErrCodeConst.vip_room_rs_no_pay_message);
            return bean;
        }
        return bean;
    }

}
