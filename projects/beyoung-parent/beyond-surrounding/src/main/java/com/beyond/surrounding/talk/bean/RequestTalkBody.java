package com.beyond.surrounding.talk.bean;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "RequestTalkBody") 
@XmlAccessorType(XmlAccessType.FIELD)      
@Getter
@Setter
@ToString
public class RequestTalkBody {

    @XmlElement(name = "sAccessToken")
    @JsonProperty("sAccessToken") // 確保 JSON 傳入舊名稱 "sAccessToken" 能正確映射到變數 accessToken
    private String accessToken;

    @XmlElement(name = "sMessage")
    @JsonProperty("sMessage")     // 確保 JSON 傳入舊名稱 "sMessage" 能正確映射到變數 message
    private String message;

    @XmlElement(name = "sFileName")
    @JsonProperty("sFileName")
    private String fileName;

    @XmlElement(name = "sFromDate")
    @JsonProperty("sFromDate")
    private String fromDate;

    @XmlElement(name = "sReportType")
    @JsonProperty("sReportType")
    private String reportType;
    
}