package com.beyoung.member.infrastructure;

import jakarta.persistence.*;
import lombok.*;

/**
 * 點數歷程 Entity
 * 對應資料表：LPL_FILE
 * 用於 doHouseHold 合併點數歷程
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LPL_FILE")
public class LplFile {

    @Id
    @Column(name = "LPL01", length = 50)
    private String lpl01;

    @Column(name = "LPL02")
    private java.sql.Date lpl02;

    @Column(name = "LPL09")
    private Integer lpl09;
}