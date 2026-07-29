package com.beyoung.surrounding.pss.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * TcPsaFileId
 * 複合主鍵類別 (原 TC_PSA_FILE_ComposeKey)
 * 使用安全的 Objects.equals 與 Objects.hash 重新實作，修正原舊式代碼 String 與 Date 比對失效漏洞
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TcPsaFileId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tcPsaplant;
    private String tcPsa01;
    private String tcPsa02;
    private String tcPsa03;
    private Date tcPsa04;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcPsaFileId that = (TcPsaFileId) o;
        return Objects.equals(tcPsaplant, that.tcPsaplant) &&
               Objects.equals(tcPsa01, that.tcPsa01) &&
               Objects.equals(tcPsa02, that.tcPsa02) &&
               Objects.equals(tcPsa03, that.tcPsa03) &&
               Objects.equals(tcPsa04, that.tcPsa04);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcPsaplant, tcPsa01, tcPsa02, tcPsa03, tcPsa04);
    }
}