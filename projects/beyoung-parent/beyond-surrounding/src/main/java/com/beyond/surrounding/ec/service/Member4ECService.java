package com.beyond.surrounding.ec.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.app.client.ErpPointFeignClient;
import com.beyond.surrounding.app.entity.LPJ_FILE;
import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.app.entity.LSM_FILE;
import com.beyond.surrounding.app.entity.LPL_FILE;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.bonus.service.BonusService;
import com.beyond.surrounding.ec.bean.RequestBody;
import com.beyond.surrounding.ec.entity.TC_LRJ_FILE;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.ec.repository.Member4ECRepository;
import com.beyond.surrounding.ec.repository.RedeemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.util.ParseUtil; 

@Slf4j
@Service
@RequiredArgsConstructor
public class Member4ECService {
	
	private final Member4ECRepository memberRepository;
	private final BonusService bonusService;
    private final RedeemRepository redeemRepository; 
    private final ErpPointFeignClient erpPointFeignClient;
    private final ParseUtil parseUtil;
	/**
	 * 1. 根據 ID 獲取會員資料
	 */
	@Transactional(readOnly = true)
	public LPK_FILE getMemberById(String id) {
		return memberRepository.findMemberById(id).orElse(new LPK_FILE());
	}

	/**
	 * 2. 註冊暫存會員 (同時新增 LPK_FILE 與 LPJ_FILE)
	 */
	@Transactional
	public ResponseBean addTempMember(RequestBody requestBody) {
		ResponseBean responseBean = new ResponseBean();
		try {
			log.info("EC：addTempMember : center -> {}, cardNo -> {}", requestBody.getCenter(), requestBody.getCardNo());
			
			// 檢查卡號是否重複
			if (memberRepository.isExistLPK(requestBody.getCardNo())) {
				responseBean.setCode(ErrCodeConst.duplicate);
				responseBean.setMessage(requestBody.getCardNo() + " " + ErrCodeConst.duplicate_message);
				return responseBean;
			}
			
			Date now = new Date();
			
			// 寫入暫存會員基本檔 (LPK)
			memberRepository.insertTempMember(requestBody.getCardNo(), now);
			
			// 寫入暫存點數主檔 (LPJ)
			memberRepository.insertTempPoint(requestBody.getCardNo(), now);

			responseBean.setCode(ErrCodeConst.finished);
			responseBean.setMessage(ErrCodeConst.finished_message);
		} catch (Exception e) {
			log.error("addTempMember 發生異常: ", e);
			throw new RuntimeException(e.getMessage());
		}
		return responseBean;
	}

	/**
	 * 3. 修改會員通訊資料
	 */
	@Transactional
	public ResponseBean updateMemberById(RequestBody requestBody) {
		ResponseBean responseBean = new ResponseBean();
		if (!memberRepository.isExistID(requestBody.getId())) {
			responseBean.setCode(ErrCodeConst.not_found);
			responseBean.setMessage(requestBody.getId() + " " + ErrCodeConst.not_found_message);
		} else {
			memberRepository.updateMemberById(requestBody.getId(), requestBody.getMobile(), requestBody.getEmail(), requestBody.getAddr(), new Date());
			responseBean.setCode(ErrCodeConst.finished);
			responseBean.setMessage(ErrCodeConst.finished_message);
		}
		return responseBean;
	}

	/**
	 * 4. 獲取點數主檔資訊
	 */
	@Transactional(readOnly = true)
	public LPJ_FILE getPointById(String id) {
		return memberRepository.findPointById(id).orElse(new LPJ_FILE());
	}

	/**
	 * 5. 查詢點數歷史明細歷程 (利用 Java 15 Text Block 處理複雜 Native Query)
	 */
	@Transactional(readOnly = true)
    public List<LSM_FILE> getPointHistById(String id, String startDate, String endDate) {
        List<LSM_FILE> list = memberRepository.getPointHistById(id, startDate, endDate);
        
        // 將動態計算出的 extendTqa02 商店名稱，塞回舊系統對外輸出的 tqa02 屬性中
        if (list != null) {
            for (LSM_FILE lsm : list) {
                if (lsm.getExtendTqa02() != null) {
                    lsm.setTqa02(lsm.getExtendTqa02());
                }
            }
        }
        return list;
    }

	/**
	 * 6. 暫存卡歸戶至正式會員主卡 (核心複雜邏輯)
	 */
	@Transactional
	public synchronized LPJ_FILE doHouseHold(RequestBody requestBody) {
		try {
			log.info("EC：doHouseHold : tempMemberId -> {}, id -> {}", requestBody.getCardNo(), requestBody.getId());
			
			// 驗證暫存卡點數狀態是否為 '000'
			LPJ_FILE tempPoint = memberRepository.findPointByMemberId(requestBody.getCardNo())
					.orElseThrow(() -> new Exception("EC：找不到暫存卡資料 -> " + requestBody.getCardNo()));
			
			if (!"000".equals(tempPoint.getLpj02())) {
				throw new Exception("EC：無法歸戶，卡片狀態非 000, TempMemberID -> " + requestBody.getCardNo() + ", ID -> " + requestBody.getId());
			}

			// 驗證正式會員主卡是否存在
			LPJ_FILE mainPoint = memberRepository.findPointById(requestBody.getId())
					.orElseThrow(() -> new Exception("EC：找不到主卡會員資料 -> " + requestBody.getId()));
			
			if (mainPoint.getLpj03() == null) {
				throw new Exception("EC：無法歸戶，該用戶無有效主卡, sID -> " + requestBody.getId());
			}

			// 驗證卡號前綴
			String mainCardNo = mainPoint.getLpj03();
			if (!mainCardNo.startsWith("7708") && !mainCardNo.startsWith("EC") && !mainCardNo.startsWith("APP") && !mainCardNo.startsWith("TS")) {
				throw new Exception("EC：無法歸戶，主卡卡號不合法, Card ID -> " + mainCardNo + ", ID -> " + requestBody.getId());
			}

			if (tempPoint.getLpj01() != null && mainPoint.getLpj01() != null) {
				log.info("EC doHouseHold 開始進行合併 : tempMemberId -> {}, memberId -> {}", requestBody.getCardNo(), mainPoint.getLpj01());
				
				// A. 點數合併累加到主卡
				memberRepository.mergePointToMainCard(
				        mainPoint.getLpj01(),        // @Param("memberId") 主卡會員內部ID
				        tempPoint.getLpj07(),        // @Param("lpj07")
				        tempPoint.getLpj12(),        // @Param("lpj12") 修正大小寫
				        tempPoint.getLpj14(),        // @Param("lpj14") 修正大小寫
				        tempPoint.getLpj15(),        // @Param("lpj15") 修正大小寫
				        tempPoint.getTaLpj02(),      // @Param("taLpj02") 修正大小寫與駝峰
				        tempPoint.getTaLpj03()       // @Param("taLpj03") 修正！原誤傳成 getLpj03() 卡號
				);
				
				// B. 將被合併的暫存卡點數歸屬人改為主卡，並將 ta_lpj04 設為 'N' (失效)
				memberRepository.disableTempPointCard(mainPoint.getLpj01(), requestBody.getCardNo());

				// C. 移轉 LPL_FILE 歷史卡紀錄 (搬移舊 DAO 的逐筆 Max + 1 邏輯)
				List<LPL_FILE> tempLplList = memberRepository.findLplByMemberId(requestBody.getCardNo());
				for (LPL_FILE lplRecord : tempLplList) {
					Integer nextSeq = memberRepository.getNextLplSeq(mainPoint.getLpj01());
					memberRepository.updateLplHistory(
							mainPoint.getLpj01(), 
							nextSeq, 
							requestBody.getCardNo(), 
							lplRecord.getLpl02(), 
							lplRecord.getLpl09()
					);
				}

				// D. 將舊點數歷程明細 (LSM_FILE) 轉到正式主卡上
				memberRepository.transferPointHistory(mainCardNo, requestBody.getCardNo());
				
				// E. 刪除原暫存卡的會員基本資料 (LPK_FILE)
				memberRepository.deleteById(requestBody.getCardNo());

				// 回傳合併後最新主卡的點數狀態
				return memberRepository.findPointById(requestBody.getId()).orElse(new LPJ_FILE());
			} else {
				throw new Exception("EC：無法歸戶，會員系統內部 ID 遺失 -> " + requestBody.getId());
			}
		} catch (Exception e) {
			log.error("doHouseHold 發生異常: ", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 7. 暫存會員升級正式會員 (自動分析身份證字號判斷性別)
	 */
	@Transactional
	public ResponseBean doFormal(RequestBody requestBody) {
		ResponseBean responseBean = new ResponseBean();
		try {
			log.info("EC：doFormal : tempMemberId -> {}, userName -> {}, id -> {}", requestBody.getCardNo(), requestBody.getUserName(), requestBody.getId());
			
			LPJ_FILE tempPoint = memberRepository.findPointByMemberId(requestBody.getCardNo())
					.orElseThrow(() -> new Exception("EC：升級正式會員失敗，找不到該暫存卡。"));

			if (tempPoint.getLpj02() == null || !"000".equals(tempPoint.getLpj02())) {
				throw new Exception("EC：該用戶無法轉正式會員，卡片狀態非 000 ， TempMemberID -> " + requestBody.getCardNo() + ", ID -> " + requestBody.getId());
			}

			String id = requestBody.getId();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date birthday = (requestBody.getBirthday() == null) ? null : sdf.parse(requestBody.getBirthday());

			// 身份證第 2 碼性別識別 (1=男 lpk06='1', 2=女 lpk06='0')
			if (id != null && id.length() >= 2) {
				String genderCode = id.substring(1, 2);
				if ("1".equals(genderCode)) {
					memberRepository.formalizeMemberWithGender(requestBody.getCardNo(), id.toUpperCase(), requestBody.getUserName(), birthday, requestBody.getAddr(), requestBody.getMobile(), requestBody.getEmail(), "1");
				} else if ("2".equals(genderCode)) {
					memberRepository.formalizeMemberWithGender(requestBody.getCardNo(), id.toUpperCase(), requestBody.getUserName(), birthday, requestBody.getAddr(), requestBody.getMobile(), requestBody.getEmail(), "0");
				} else {
					memberRepository.formalizeMemberWithoutGender(requestBody.getCardNo(), id.toUpperCase(), requestBody.getUserName(), birthday, requestBody.getAddr(), requestBody.getMobile(), requestBody.getEmail());
				}
			} else {
				memberRepository.formalizeMemberWithoutGender(requestBody.getCardNo(), id != null ? id.toUpperCase() : null, requestBody.getUserName(), birthday, requestBody.getAddr(), requestBody.getMobile(), requestBody.getEmail());
			}

			// 更新點數主檔卡片狀態為 'EC'
			memberRepository.formalizePointCard(requestBody.getCardNo());

			responseBean.setCode(ErrCodeConst.finished);
			responseBean.setMessage(ErrCodeConst.finished_message);
		} catch (Exception e) {
			log.error("doFormal 發生異常: ", e);
			throw new RuntimeException(e.getMessage());
		}
		return responseBean;
	}

	/**
	 * 8. 扣點與點數處理交易 (殼保留)
	 */
	@Transactional(rollbackFor = Exception.class)
    public ResponseBean processPoint(RequestBody requestBody) throws Exception {
        ResponseBean bean = new ResponseBean();

        // 1. 取得扣點規則設定
        TC_LRJ_FILE entity = redeemRepository.getRule();

        // 2. 組裝舊有 TIPTOP ERP 的 SOAP XML 內容
        String todayStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String milliStr = String.valueOf(System.currentTimeMillis() % 100000);
        String serialNum = "EC" + todayStr + milliStr;
        int invertedPoint = requestBody.getPoint(); // 扣點反轉為負數

        String soapEnvelope = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
            + "   <soapenv:Header/>"
            + "   <soapenv:Body>"
            + "      <tip:GetConsumerPointsRequest>"
            + "         <tip:request>"
            + "             &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>"
            + "&lt;Field name='type' value='1'/>"
            + "&lt;Field name='condition' value='" + requestBody.getCardNo() + "'/>"
            + "&lt;Field name='shop' value='" + requestBody.getCenter() + "'/>"
            + "&lt;Field name='saleno' value='" + serialNum + "'/>"
            + "&lt;Field name='date' value='" + todayStr + "'/>"
            + "&lt;Field name='amt' value='" + requestBody.getAmt() + "'/>"
            + "&lt;Field name='Reduce_Points' value='" + invertedPoint + "'/>"
            + "&lt;Field name='Rent_Booth' value='" + requestBody.getCounterId() + "'/>"
            + "&lt;Field name='invoice_b' value=''/>"
            + "&lt;Field name='invoice_e' value=''/>"
            + "&lt;Field name='rule' value='" + entity.getTC_LRJ01() + "'/>"
            + "&lt;Field name='Reduce_Points2' value='0'/>"
            + "&lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>"
            + "         </tip:request>"
            + "      </tip:GetConsumerPointsRequest>"
            + "   </soapenv:Body>"
            + "</soapenv:Envelope>";

        // 3. 透過 Feign Client 發送 SOAP 請求並取得 XML 字串回應
        String xmlResponse;
        try {
            xmlResponse = erpPointFeignClient.sendSoapRequest(soapEnvelope);
        } catch (Exception e) {
            log.error("Feign 調用 ERP WebService 發生通訊異常", e);
            throw e;
        }

        // 4. 解析 ERP 回傳的 XML（沿用舊系統的 ParseUtil 轉換為 JSONObject）
        JSONObject joResult = parseUtil.parserERPPoint(xmlResponse);

        // 5. 判斷 ERP 回傳狀態碼
        if (!"0".equals(joResult.getString("code"))) {
            bean.setCode(ErrCodeConst.pos_rs_erp_ws);
            bean.setMessage(ErrCodeConst.pos_rs_erp_ws_message);
            log.error("卡號：{}，{}:{}", requestBody.getCardNo(), joResult.getString("code"), joResult.getString("message"));
            return bean;
        }

        // 6. 寫入 BONUS_LOG 點數交易日誌
        bonusService.saveLog(
            requestBody.getCenter(),
            requestBody.getCounterId(),
            requestBody.getUserId(),
            requestBody.getUserName(),
            requestBody.getCardNo(),
            requestBody.getPoint(),
            requestBody.getLoginId()
        );

        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);
        return bean;
    }
	

	
}