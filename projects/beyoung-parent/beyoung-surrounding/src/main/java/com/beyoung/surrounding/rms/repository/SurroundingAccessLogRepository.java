package com.beyoung.surrounding.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.rms.entity.SurroundingAccessLog;

@Repository
public interface SurroundingAccessLogRepository extends JpaRepository<SurroundingAccessLog, Integer> {

    /**
     *  客製化無痛相容方法
     * 為了讓舊有的呼叫端 surroundingAccessLogDao.save(ip, c_no, url) 
     * 不需要做大範圍程式碼調整，在這裡直接重載原生的 save 行為。
     */
    default void save(String sAccessIP, String c_no, String sUrl) throws Exception {
        SurroundingAccessLog logEntity = new SurroundingAccessLog();
        logEntity.setAccess_ip(sAccessIP);
        logEntity.setC_no(c_no);
        logEntity.setUrl(sUrl);
        
        // 呼叫 JpaRepository 內建的物件導向 save 方法
        // 註：Entity 內的 @PrePersist 會自動幫忙塞入 access_date 時間
        this.save(logEntity); 
    }
}