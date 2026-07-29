package com.beyoung.surrounding.pos2.service;

import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.app.entity.LPK_FILE;
import com.beyoung.surrounding.member.repository.MemberRepository;
import com.beyoung.surrounding.pos2.entity.READING_SPACE_LOG;
import com.beyoung.surrounding.pos2.entity.READING_SPACE_PARAM;
import com.beyoung.surrounding.pos2.repository.ReadingSpaceLogPos2Repository;
import com.beyoung.surrounding.pos2.repository.ReadingSpaceParamPos2Repository;
import com.beyoung.surrounding.pos2.repository.ReadingSpaceUnlimitPos2Repository;
import com.beyoung.surrounding.util.ERPWebService;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.beyoung.surrounding.util.GetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingSpacePos2Service {

	private final MemberRepository memberRepository;
    private final ReadingSpaceLogPos2Repository logPos2Repository;
    private final ReadingSpaceUnlimitPos2Repository unlimitPos2Repository;
    private final ReadingSpaceParamPos2Repository paramPos2Repository;
    private final Environment env; // 用於取得 ERP_WS_URL 配置
    
    @Transactional
    public ResponseBean purchase(
            String center,
            String counterID,
            String posID,
            String cardNO,
            Integer price) {

        ResponseBean bean = new ResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        // 1. 查會員
        LPK_FILE entity = memberRepository.getMemberByCardId(cardNO);

        if (entity.getLpk01() == null) {
            bean.setCode("404");
            bean.setMessage("會員不存在");
            return bean;
        }

        // 2. 查今日是否已有交易
        List<READING_SPACE_LOG> logs =
        		logPos2Repository.findTodayLog(center, entity.getLpk01());

        if (logs != null && !logs.isEmpty()) {

            READING_SPACE_LOG record = logs.get(0);

            // 已交易未退款 → 擋掉
            if (record.getRefundDate() == null) {
                bean.setCode(ErrCodeConst.pos_rs_repeat);
                bean.setMessage(ErrCodeConst.pos_rs_repeat_message);
                return bean;
            }

            // 已退款 → update 回復交易
            record.setTransactionTime(GetDateTime.getTime(":"));
            record.setCardNo(cardNO);
            record.setPrice(price);
            record.setEnterDate(null);
            record.setExitDate(null);
            record.setRefundDate(null);
            record.setInvoiceNo(null);
            record.setVip(null);
            record.setInRoom(null);

            logPos2Repository.save(record);
            return bean;
        }

        // 3. 無紀錄 → insert
        READING_SPACE_LOG newLog = new READING_SPACE_LOG();
        newLog.setCenter(center);
        newLog.setTransactionDate(new Date());
        newLog.setTransactionTime(GetDateTime.getTime(":"));
        newLog.setCounterId(counterID);
        newLog.setPosId(posID);
        newLog.setUserId(entity.getLpk01());
        newLog.setUserName(entity.getLpk04());
        newLog.setCardNo(cardNO);
        newLog.setPrice(price);

        logPos2Repository.save(newLog);

        return bean;
    }

    @Transactional
    public ResponseBean enter(String center, String cardNO, LPK_FILE entity) throws Exception {
        
        ResponseBean bean = new ResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        // 1. 檢查姓名欄位 (舊碼 lpk04)
        if (entity.getLpk02() == null || "".equals(entity.getLpk02().trim())) {
            bean.setCode(ErrCodeConst.pos_rs_name_not_found);
            bean.setMessage(ErrCodeConst.pos_rs_name_not_found_message);
            log.info("ReadingSpace enter : cardNO -> {} {}", cardNO, ErrCodeConst.pos_rs_name_not_found_message);
            return bean;
        }

        // 2. 姓名去識別化處理 (遮罩第二個字)
        StringBuilder myName = new StringBuilder(entity.getLpk02().trim());
        if (entity.getLpk02().length() >= 2 && !"臨時會員".equals(entity.getLpk02())) {
            myName.replace(1, 2, "*");
        }
        bean.setUser_name(myName.toString());

        // 3. 檢查是否為員工卡 (免點數免限制白名單)
        boolean isEmployee = unlimitPos2Repository.existsByUserId(entity.getLpk01());

        if (!isEmployee) {
            // ==================== 非員工：進入常規控管邏輯 ====================
            
            int iLimit = 25;       // 預設人數上限
            int iMinusPoint = 9999; // 預設扣抵點數 (9999代表不啟用)

            // 載入系統參數
            List<READING_SPACE_PARAM> params = paramPos2Repository.findByParamNameIn(Arrays.asList("member_limit", "minus_point"));
            for (READING_SPACE_PARAM param : params) {
                if ("member_limit".equals(param.getParamName())) {
                    iLimit = Integer.parseInt(param.getParamValue());
                }
                if ("minus_point".equals(param.getParamName())) {
                    iMinusPoint = Integer.parseInt(param.getParamValue());
                }
            }

            // 判斷現場人數是否超額
            int currentPeopleCount = logPos2Repository.countTodayInRoomPeople(center);
            if (currentPeopleCount >= iLimit) {
                bean.setCode(ErrCodeConst.pos_rs_over);
                bean.setMessage(ErrCodeConst.pos_rs_over_message);
                log.info("ReadingSpace enter : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_over_message);
                return bean;
            }

            // 查詢今日是否有未退款的交易紀錄或入場紀錄
            List<READING_SPACE_LOG> todayLogs = logPos2Repository.findTodayActiveLog(center, entity.getLpk01());

            if (!todayLogs.isEmpty()) {
                // 情境 1：今日已有紀錄
                for (READING_SPACE_LOG record : todayLogs) {
                    // 若人已經在房間內，重入報錯
                    if ("Y".equals(record.getInRoom())) {
                        bean.setCode(ErrCodeConst.pos_rs_not_exit);
                        bean.setMessage(ErrCodeConst.pos_rs_not_exit_message);
                        log.info("ReadingSpace enter : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_not_exit_message);
                        return bean;
                    }
                    
                    // 已付費未進場：更新進場時間與狀態
                    record.setEnterDate(new Date());
                    record.setInRoom("Y");
                    logPos2Repository.save(record);
                }
            } else {
                // 情境 2：今日無交易紀錄，判定是否可直接免簽或走點數扣抵
                
                // 檢查是否為 VIP 會員 (假設 vip_level 對應您實體中的特定欄位，這裡用示意欄位，請改為您實際對應的欄位名如 taLpk01 等)
                if ("1".equals(entity.getVipLevel()) || "2".equals(entity.getVipLevel())) {
                    
                    READING_SPACE_LOG vipLog = READING_SPACE_LOG.builder()
                            .center(center)
                            .transactionDate(new Date())
                            .transactionTime(GetDateTime.getTime(":"))
                            .userId(entity.getLpk01())
                            .userName(entity.getLpk02())
                            .cardNo(cardNO)
                            .enterDate(new Date())
                            .vip("Y")
                            .inRoom("Y")
                            .build();
                    
                    logPos2Repository.save(vipLog);
                    log.info("ReadingSpace enter : cardNO -> {}, Name -> {} VIP 會員直接進場", cardNO, entity.getLpk02());
                    
                } else {
                    // 一般會員未先購買，確認系統是否有開啟「點數扣抵入場」
                    if (iMinusPoint != 9999) {
                        // 假設 lpj12 為點數餘額欄位
                        if (entity.getLpj12() != null && entity.getLpj12() >= iMinusPoint) {
                            
                            // 呼叫 ERP WebService扣點
                            JSONObject joResult = ERPWebService.useMemberPoint(
                                    env.getProperty("ERP_WS_URL"), "BY001", "70050", cardNO, iMinusPoint, "", "", "RS", GetDateTime.getTimeMilli("")
                            );
                            
                            if (!"0".equals(joResult.getString("code"))) {
                                bean.setCode(ErrCodeConst.pos_rs_erp_ws);
                                bean.setMessage(ErrCodeConst.pos_rs_erp_ws_message);
                                log.error("{} 卡號：{}，{}:{}", bean.getUser_name(), cardNO, joResult.getString("code"), joResult.getString("message"));
                                return bean;
                            }

                            // 扣點成功，直接寫入一筆扣點進場紀錄
                            READING_SPACE_LOG pointLog = READING_SPACE_LOG.builder()
                                    .center(center)
                                    .transactionDate(new Date())
                                    .transactionTime(GetDateTime.getTime(":"))
                                    .counterId("70050")
                                    .userId(entity.getLpk01())
                                    .userName(entity.getLpk02())
                                    .cardNo(cardNO)
                                    .enterDate(new Date())
                                    .inRoom("Y")
                                    .point(iMinusPoint)
                                    .build();
                            
                            logPos2Repository.save(pointLog);
                            log.info("ReadingSpace enter : cardNO -> {}, Name -> {} 使用會員點數 {} 點入場", cardNO, entity.getLpk02(), iMinusPoint);
                            return bean;
                            
                        } else {
                            bean.setCode(ErrCodeConst.pos_rs_point_not_enough);
                            bean.setMessage(ErrCodeConst.pos_rs_point_not_enough_message);
                            log.info("ReadingSpace enter : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_point_not_enough_message);
                            return bean;
                        }
                    }
                    
                    // 未交易、非VIP、且未開啟扣點機制 -> 拒絕入場
                    bean.setCode(ErrCodeConst.pos_rs_no_pay);
                    bean.setMessage(ErrCodeConst.pos_rs_no_pay_message);
                    log.info("ReadingSpace enter : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_no_pay_message);
                    return bean;
                }
            }
        } else {
            // ==================== 是員工：直接放行 ====================
            log.info("ReadingSpace enter : cardNO -> {}, Name -> {} 使用員工卡進場", cardNO, entity.getLpk02());
        }

        return bean;
    }

    @Transactional
	public ResponseBean exit(String center, String cardNO, LPK_FILE entity) {
		
		ResponseBean bean = new ResponseBean();
		bean.setCode(ErrCodeConst.finished);
		bean.setMessage(ErrCodeConst.finished_message);	
		
		// 1. 姓名去識別化處理 (遮罩第二個字)
		StringBuilder myName = new StringBuilder(entity.getLpk02().trim());
		if (entity.getLpk02().length() >= 2 && !"臨時會員".equals(entity.getLpk02())) {
			myName.replace(1, 2, "*");
		}
		bean.setUser_name(myName.toString());

		// 2. 檢查是否為員工卡 (免扣點白名單)
		boolean isEmployee = unlimitPos2Repository.existsByUserId(entity.getLpk01());
		
		// 3. 非員工身分，處理出場邏輯
		if (!isEmployee) {
			// 查詢今天該會員在該場館的交易日誌 (複用先前在進場寫好的 findTodayActiveLog 或設計對應方法)
			// 注意：舊碼此處並未限定 refund_date is null，但安全起見維持原本對應的今日資料查詢
			List<READING_SPACE_LOG> todayLogs = logPos2Repository.findTodayActiveLog(center, entity.getLpk01());
			
			if (!todayLogs.isEmpty()) {
				for (READING_SPACE_LOG record : todayLogs) {
					// 更新出場時間，並將在室狀態標記為 N
					record.setExitDate(new Date());
					record.setInRoom("N");
					
					// 使用 Spring Data JPA 的 save 觸發舊資料的 Update 動作
					logPos2Repository.save(record);
				}
			}
		} else {
			log.info("ReadingSpace exit : cardNO -> {}, Name -> {} 使用員工卡出場", cardNO, entity.getLpk02());
		}
		
		return bean;
	}

    @Transactional
	public ResponseBean refund(String center, String invoiceNO, String cardNO, String refundDate, LPK_FILE entity) {
		
		ResponseBean bean = new ResponseBean();
		bean.setCode(ErrCodeConst.finished);
		bean.setMessage(ErrCodeConst.finished_message);	
		
		// 1. 查詢該筆指定的交易日誌 (此處的 refundDate 其實是前端傳入的「原交易日期」)
		List<READING_SPACE_LOG> logs = logPos2Repository.findLogForRefund(center, refundDate, entity.getLpk01());
		
		// 2. 判斷是否有交易資料
		if (!logs.isEmpty()) {
			for (READING_SPACE_LOG record : logs) {
				
				// 攔截點 A：已交易且已進場使用過 -> 阻擋退款
				if (record.getEnterDate() != null) {
					bean.setCode(ErrCodeConst.pos_rs_used);
					bean.setMessage(ErrCodeConst.pos_rs_used_message);
					log.info("ReadingSpace refund : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_used_message);
					return bean;
				} 
				
				// 攔截點 B：此筆交易先前已辦理過退款 -> 阻擋重複退款
				else if (record.getRefundDate() != null) {
					bean.setCode(ErrCodeConst.pos_rs_refunded);
					bean.setMessage(ErrCodeConst.pos_rs_refunded_message);
					log.info("ReadingSpace refund : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_refunded_message);
					return bean;
				} 
				
				// 允許退款情境：已付費未使用 -> 寫入退款日期與發票號碼
				else {
					record.setRefundDate(new java.util.Date()); // 設為當前退款時間 now()
					record.setInvoiceNo(invoiceNO);           // 寫入發票號碼
					
					// 透過 JPA 自動觸發 Update 動作
					logPos2Repository.save(record);
					log.info("ReadingSpace refund success : cardNO -> {}, Name -> {}, Invoice -> {}", cardNO, entity.getLpk02(), invoiceNO);
				}
			}
		} else {
			// 找不到任何交易日誌 -> 提示未交易
			bean.setCode(ErrCodeConst.pos_rs_no_pay);
			bean.setMessage(ErrCodeConst.pos_rs_no_pay_message);
			log.info("ReadingSpace refund : cardNO -> {}, Name -> {} {}", cardNO, entity.getLpk02(), ErrCodeConst.pos_rs_no_pay_message);
			return bean;				
		}
		
		return bean;
	}
   
    
}