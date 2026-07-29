package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.pos2.bean.POS2Bean;
import com.beyond.surrounding.pos2.entity.TC_PSA_FILE;
import com.beyond.surrounding.pos2.repository.InvoicePsaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranXService {

    private final InvoicePsaRepository invoicePsaRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(List<POS2Bean> requestBody) throws Exception {
        if (requestBody == null || requestBody.isEmpty()) {
            return;
        }

        for (POS2Bean bean : requestBody) {
            TC_PSA_FILE joTcPsaFile = bean.getTcPsaFile();
            if (joTcPsaFile == null) continue;

            // 1. 重複檢查 (對齊原 SQL 邏輯，保留字串比對格式)
            List<String> dupList = invoicePsaRepository.checkDuplicate(
                    joTcPsaFile.getTcPsaPlant(),
                    joTcPsaFile.getTcPsa01(),
                    joTcPsaFile.getTcPsa02(),
                    joTcPsaFile.getTcPsa03(),
                    joTcPsaFile.getTcPsa04()
            );

            // 如果查無資料，才進行處理與塞入
            if (dupList.isEmpty()) {
                
                // 2. 折讓、作廢、退貨關聯欄位處理 (TC_PSA06 == '02' || '03')
                String psa06 = joTcPsaFile.getTcPsa06();
                if ("02".equals(psa06) || "03".equals(psa06)) {
                    List<TC_PSA_FILE> origInvoices = invoicePsaRepository.findOriginalInvoice(
                            joTcPsaFile.getTcPsaPlant(),
                            joTcPsaFile.getTcPsa01(),
                            joTcPsaFile.getTcPsa16(),
                            joTcPsaFile.getTcPsa17()
                    );
                    
                    if (!origInvoices.isEmpty()) {
                        TC_PSA_FILE origBean = origInvoices.get(0);
                        if (origBean.getTcPsa13() != null && !origBean.getTcPsa13().isEmpty()) {
                            joTcPsaFile.setTcPsa13(origBean.getTcPsa13());
                        }
                    }
                }

                // 3. 載具條件判斷處理
                String psa35 = joTcPsaFile.getTcPsa35();
                if (psa35 != null && !psa35.isEmpty()) {
                    if (psa35.startsWith("/")) {
                        joTcPsaFile.setTcPsa34("0"); // 條碼載具
                    } else if (psa35.length() == 16 && psa35.substring(0, 2).matches("[a-zA-Z]+")) {
                        joTcPsaFile.setTcPsa34("2"); // 自然人憑證等其他型態載具
                    }
                    // else 情況維持前端傳進來的原值，不額外處理
                }

                // 4. 固定格式的特殊欄位欄位初始化對齊 (原 SQL 硬代 0)
                joTcPsaFile.setTcPsa19(0);
                joTcPsaFile.setTcPsa20(0);
                
                // 處理原 SQL 設定為 null 的未定義或未傳送欄位 (如果 Entity 內沒對應，JPA 自然不更新)
                joTcPsaFile.setTcPsa15(null);

                // 5. 透過 JPA 直接存檔 (會自動生成完美的多欄位安全 Insert 陳述式)
                invoicePsaRepository.save(joTcPsaFile);
                log.info("發票資料成功存入：{}", joTcPsaFile.getTcPsa17());
            } else {
                log.warn("發票資料重複，略過存檔：{} / {}", joTcPsaFile.getTcPsaPlant(), joTcPsaFile.getTcPsa17());
            }
        }
    }
}
