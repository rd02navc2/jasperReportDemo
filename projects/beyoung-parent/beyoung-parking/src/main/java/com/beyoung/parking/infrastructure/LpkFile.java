package com.beyoung.parking.infrastructure;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lpk_file") // 會員基本資料擴充 : 這張表通常用來存放會員的附加資訊（例如個人資料、聯絡管道等）
public class LpkFile {

    @Id
    @Column(name = "lpk01") // String  會員系統內部 ID   與 lpj01 對應，用來做資料表 Inner Join。
    private String lpk01; // 會員系統內部 ID (主鍵)

    @Column(name = "lpk04") // String  會員姓名 / 會員等級名稱   雖然 SQL 有查詢出來，但在本次加點邏輯中僅作備用
    private String lpk04; // 會員姓名 / 等級名稱
}

