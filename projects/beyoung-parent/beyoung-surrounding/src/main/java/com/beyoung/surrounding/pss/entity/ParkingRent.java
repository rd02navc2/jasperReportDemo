package com.beyoung.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;

/**
 * ParkingRent
 * 車位租用主檔實體類別（現代化重構版）
 * 已升級至 Jakarta Persistence 規範、導入 Lombok 簡化，並規範化欄位命名與映射
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PARKING_RENT")
@DynamicInsert
public class ParkingRent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CAR_NO", length = 50)
    private String carNo;

    @Column(name = "USER_NAME", length = 100)
    private String userName;

    @Column(name = "IS_UNLIMITED_DATE", length = 10)
    private String isUnlimitedDate;

    @Temporal(TemporalType.DATE) // 租約啟用日期通常精確到日即可
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.DATE) // 租約結束日期通常精確到日即可
    @Column(name = "END_DATE")
    private Date endDate;

    @Temporal(TemporalType.TIMESTAMP) // 系統寫入時間戳記，保留時分秒精度
    @Column(name = "ACCESS_DATE")
    private Date accessDate;

    @Column(name = "ACCESS_ID", length = 50)
    private String accessId;

	public Object getPNo() {
		// TODO Auto-generated method stub
		return null;
	}
}