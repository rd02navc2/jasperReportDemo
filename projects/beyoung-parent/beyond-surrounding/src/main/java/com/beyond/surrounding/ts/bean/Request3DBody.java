package com.beyond.surrounding.ts.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // 自動生成 Getter/Setter/toString
@NoArgsConstructor // 自動生成無參構造函數（JAXB必備）
@AllArgsConstructor // 自動生成全參構造函數
@XmlRootElement(name = "Request3DBody")
public class Request3DBody {

    @XmlElement(name = "member_id")
    @JsonProperty("member_id")
    private String memberId; // 建議遵循 Java 駝峰命名，並用 name 指定 XML 標籤名

    @XmlElement(name = "barcode")
    @JsonProperty("barcode")
    private String barcode;

}