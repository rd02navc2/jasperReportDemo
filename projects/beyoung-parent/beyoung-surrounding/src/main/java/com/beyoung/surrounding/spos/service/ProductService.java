package com.beyoung.surrounding.spos.service;

import com.beyoung.surrounding.bean.ProductBean;
import com.beyoung.surrounding.spos.repository.IMAFILERepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service("sPosProductService")
@RequiredArgsConstructor
public class ProductService {

	private final IMAFILERepository imaFileRepository;

    @Transactional(readOnly = true)
    public ProductBean getProductByPNO(String sPNO) throws Exception {
        log.info("Executing getProductByPNO for PNO: {}", sPNO);
        
        ProductBean bean = new ProductBean();
        // 預設設為未找到，等真正查到資料再覆蓋
        bean.setCode("not_found");
        bean.setMessage("not_found_message");

        try {
            List<Object[]> results = imaFileRepository.findProductByPNORaw(sPNO);
            
            // 模擬舊專案迴圈覆蓋邏輯（取最後一筆）
            for (Object[] row : results) {
                if (row != null && row.length >= 5) {
                    bean.setCode("finished");
                    bean.setMessage("finished_message");
                    
                    String ima01 = row[0] != null ? row[0].toString() : null;
                    String ima02 = row[1] != null ? row[1].toString() : null;
                    String ima15 = row[2] != null ? row[2].toString() : null;
                    
                    // 轉型價格為 Double (防呆預設 0.0)
                    double ima127 = row[3] != null ? Double.parseDouble(row[3].toString()) : 0.0;
                    double ima128 = row[4] != null ? Double.parseDouble(row[4].toString()) : 0.0;
                    
                    bean.setP_no(ima01);
                    bean.setP_name(ima02);
                    
                    //  核心舊商業邏輯轉移：判斷 IMA15 決定帶出哪一個價格
                    if (ima15 != null && ima15.equalsIgnoreCase("Y")) {
                        bean.setPrice(ima127);
                    } else {
                        bean.setPrice(ima128);
                    }
                }
            }
            
            // 如果查出來連第一欄 IMA01 都是 null，改回未找到
            if (bean.getP_no() == null) {
                bean.setCode("not_found");
                bean.setMessage("not_found_message");
            }
            
        } catch (Exception e) {
            log.error("Error in getProductByPNO: {}", e.getMessage(), e);
            throw e;
        }
        
        return bean;
    }
    
}