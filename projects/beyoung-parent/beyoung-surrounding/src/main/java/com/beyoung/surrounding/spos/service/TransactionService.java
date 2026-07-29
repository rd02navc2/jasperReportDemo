package com.beyoung.surrounding.spos.service;

import com.beyoung.surrounding.spos.bean.SPOSBean;
import com.beyoung.surrounding.spos.repository.TCPSAFILERepository;
import com.beyoung.surrounding.spos.repository.TCPSBFILERepository;
import com.beyoung.surrounding.spos.repository.TCPSCFILERepository;
import com.beyoung.surrounding.spos.service.TransactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TCPSAFILERepository tcpsaFileRepository;
    private final TCPSBFILERepository tcpsbFileRepository;
    private final TCPSCFILERepository tcpscFileRepository;

    @Transactional(rollbackFor = Exception.class) // 確保單頭、單身寫入時，若任一處出錯會自動全盤 Rollback
    public void save(List<SPOSBean> requestBody) throws Exception {
        if (requestBody == null || requestBody.isEmpty()) {
            return;
        }

        for (SPOSBean bean : requestBody) {
            // 1. 儲存單頭 (TC_PSA_FILE)
            if (bean.getTC_PSA_FILE() != null) {
                tcpsaFileRepository.save(bean.getTC_PSA_FILE());
            }

            // 2. 儲存單身商品明細 (TC_PSB_FILE)
            if (bean.getTC_PSB_FILE() != null && !bean.getTC_PSB_FILE().isEmpty()) {
                tcpsbFileRepository.saveAll(bean.getTC_PSB_FILE());
            }

            // 3. 儲存付款/促銷明細 (TC_PSC_FILE)
            if (bean.getTC_PSC_FILE() != null && !bean.getTC_PSC_FILE().isEmpty()) {
                tcpscFileRepository.saveAll(bean.getTC_PSC_FILE());
            }
        }
    }
}