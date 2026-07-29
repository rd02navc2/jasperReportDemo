package com.beyoung.surrounding.pos.service;

import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.pos.client.GiftServiceFeignClient;
import com.beyoung.surrounding.pos.dto.GiftResponseDTO;
import com.beyoung.surrounding.pos.entity.TD;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicePosService {

    // private final InvoicePosRepository invoicePosRepository;
    private final PosDetailService posDetailService; 
    private final GiftServiceFeignClient giftServiceFeignClient;
    private Environment env;
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean doRecoveryInvoice(String invoiceNO, String cashierID) throws Exception {

        TD entity = posDetailService.getTDByInvoiceNO(env, "BY001", invoiceNO);

        if (entity == null) {
            throw new RuntimeException("查無發票：" + invoiceNO);
        }

        String time = LocalDateTime.now().toString();

        Map<String, Object> trans = new HashMap<>();

        trans.put("T0100", "0200");
        trans.put("T0200", entity.getVipNo() == null ? "" : entity.getVipNo());
        trans.put("T0300", "727090");
        trans.put("T0400", entity.getTotSales() == null ? 0 : entity.getTotSales());
        trans.put("T0405", entity.getInvAmt() == null ? 0 : entity.getInvAmt());
        trans.put("T1200", time);
        trans.put("T1300", LocalDate.now().toString());
        trans.put("T4100", entity.getPosNo() == null ? "BYSERVER" : entity.getPosNo());
        trans.put("T4200", "BY001");
        trans.put("T5503", entity.getTentNo() == null ? "BYSERVER" : entity.getTentNo());
        trans.put("T5505", time);
        trans.put("T5507", invoiceNO);
        trans.put("T5509", "0");
        trans.put("T5581", entity.getPosNo() == null ? "BYSERVER" : entity.getPosNo());
        trans.put("T5582", entity.getSeqNo() == null ? time : entity.getSeqNo());
        trans.put("T5583", entity.getSalDate());

        Map<String, Object> request = new HashMap<>();
        request.put("Trans", trans);

        GiftResponseDTO response = giftServiceFeignClient.recoveryInvoice(request);

        if (!"00".equals(response.getTrans().getT3900())) {
            throw new RuntimeException(
                    "系統執行錯誤：" + response.getTrans().getT3900()
            );
        }

        ResponseBean bean = new ResponseBean();
        bean.setCode("0000");
        bean.setMessage("SUCCESS");

        return bean;
    }
    
}