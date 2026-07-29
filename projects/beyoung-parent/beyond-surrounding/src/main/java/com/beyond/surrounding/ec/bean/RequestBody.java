package com.beyond.surrounding.ec.bean;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JacksonXmlRootElement(localName = "RequestBody") // 支援 XML 格式的根節點名稱
public class RequestBody {

    // 1. 會員 ID
    @JsonProperty("id")                       // JSON 解析時對應欄位
    @JacksonXmlProperty(localName = "id")     // XML 解析時對應標籤
    private String id;

    // 2. 生日
    @JsonProperty("birthday")
    @JacksonXmlProperty(localName = "birthday")
    private String birthday;

    // 3. 手機號碼
    @JsonProperty("mobile")
    @JacksonXmlProperty(localName = "mobile")
    private String mobile;

    // 4. 電子郵件
    @JsonProperty("email")
    @JacksonXmlProperty(localName = "email")
    private String email;

    // 5. 地址
    @JsonProperty("addr")
    @JacksonXmlProperty(localName = "addr")
    private String addr;

    // 6. 中心/廠區 (對應原本的 sCenter)
    @JsonProperty("center")
    @JacksonXmlProperty(localName = "center")
    private String center;

    // 7. 登入 ID (對應原本的 sLoginID)
    @JsonProperty("loginId")
    @JacksonXmlProperty(localName = "loginId")
    private String loginId;

    // 8. 專櫃 ID (對應原本的 sCounterID)
    @JsonProperty("counterId")
    @JacksonXmlProperty(localName = "counterId")
    private String counterId;

    // 9. 用戶 ID (對應原本的 sUserID)
    @JsonProperty("userId")
    @JacksonXmlProperty(localName = "userId")
    private String userId;

    // 10. 用戶名稱 (對應原本的 sUserName)
    @JsonProperty("userName")
    @JacksonXmlProperty(localName = "userName")
    private String userName;

    // 11. 卡號 (對應原本的 sCardNO)
    @JsonProperty("cardNo")
    @JacksonXmlProperty(localName = "cardNo")
    private String cardNo;

    // 12. 點數 (對應原本的 iPoint)
    @JsonProperty("point")
    @JacksonXmlProperty(localName = "point")
    private Integer point;

    // 13. 金額 (對應原本的 iAmt)
    @JsonProperty("amt")
    @JacksonXmlProperty(localName = "amt")
    private Integer amt;
    
}