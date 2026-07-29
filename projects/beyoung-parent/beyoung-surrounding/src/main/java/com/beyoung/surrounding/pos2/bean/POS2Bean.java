package com.beyoung.surrounding.pos2.bean;

import java.util.List;
import com.beyoung.surrounding.pos2.entity.TC_PSA_FILE;
import com.beyoung.surrounding.pos2.entity.TC_PSB_FILE;
import com.beyoung.surrounding.pos2.entity.TC_PSC_FILE;
import com.fasterxml.jackson.annotation.JsonProperty;

public class POS2Bean {

    private TC_PSA_FILE tcPsaFile = null;
    private List<TC_PSB_FILE> tcPsbFile = null;
    private List<TC_PSC_FILE> tcPscFile = null;

    // --- Getter & Setter ---

    // 透過 @JsonProperty 確保 JSON 的 "TC_PSA_FILE" 能與此物件正確轉換
    @JsonProperty("TC_PSA_FILE")
    public TC_PSA_FILE getTcPsaFile() {
        return tcPsaFile;
    }

    public void setTcPsaFile(TC_PSA_FILE tcPsaFile) {
        this.tcPsaFile = tcPsaFile;
    }

    @JsonProperty("TC_PSB_FILE")
    public List<TC_PSB_FILE> getTcPsbFile() {
        return tcPsbFile;
    }

    public void setTcPsbFile(List<TC_PSB_FILE> tcPsbFile) {
        this.tcPsbFile = tcPsbFile;
    }

    @JsonProperty("TC_PSC_FILE")
    public List<TC_PSC_FILE> getTcPscFile() {
        return tcPscFile;
    }

    public void setTcPscFile(List<TC_PSC_FILE> tcPscFile) {
        this.tcPscFile = tcPscFile;
    }
}