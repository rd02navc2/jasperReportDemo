package com.beyoung.member.api;

import com.beyoung.member.application.MemberService;
import com.beyoung.member.domain.dto.MemberContactDTO;
import com.beyoung.member.domain.dto.MemberDTO;
import com.beyoung.member.domain.dto.MemberDTO.CardDetailResponse;
import com.beyoung.member.domain.dto.MemberDTO.MemberContactResponse;
import com.beyoung.member.domain.dto.MemberDTO.PointResponse;
import com.beyoung.member.domain.dto.MemberDTO.Response;
import com.beyoung.member.domain.dto.MemberDTO.TempMemberRequest;
import com.beyoung.member.domain.dto.MemberDTO.UpdateContactRequest;
import com.beyoung.member.infrastructure.LpjFile;
import com.beyoung.member.infrastructure.LpjFileRepository;
import com.beyoung.member.infrastructure.LpkFile;
import com.beyoung.member.infrastructure.LplFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 會員 APP 相關 API 控制器
 * 已整合 Spring Validation 參數校驗功能，升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@RestController
@RequestMapping("/Surrounding/api/app/Member")
@RequiredArgsConstructor
@Validated // 必須加上此註解，@PathVariable 與 @RequestParam 的單一參數校驗才會生效
public class MemberController {

    private final MemberService memberService;
    private final LpjFileRepository lpjFileRepository;
    
    @GetMapping(value = "/isExistLPK/{sMemberID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<MemberDTO.ExistResponse> isExistLPK(
            @PathVariable("sMemberID") @NotBlank(message = "會員ID不能為空") String sMemberID) {
        log.info("檢查卡片是否存在：sMemberID -> {}", sMemberID);
        try {
            MemberDTO.ExistResponse response = memberService.checkExistLPK(sMemberID);
            return MemberDTO.Response.success(response);
        } catch (Exception e) {
            log.error("isExistLPK 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 新增一般臨時會員 
     * 修正：由 GET 改為 POST，並改用 Request Body 接收參數以維護安全
     */
    @PostMapping(value = "/addTempMember", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<String> addTempMember(@Valid @RequestBody TempMemberRequest request) {
        log.info("新增臨時會員：sCenter -> {}, sMemberID -> {}", request.getSCenter(), request.getSMemberID());
        try {
            memberService.addTempMember(request.getSCenter(), request.getSMemberID());
            return MemberDTO.Response.success("暫時會員新增成功");
        } catch (Exception e) {
            log.error("addTempMember 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

/*
    @PostMapping(value = "/addTempMember", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String testAddTempMember(@RequestBody String rawJson) {
        log.info("【偵錯】收到的原始 JSON 內容: {}", rawJson);
        return "Received: " + rawJson;
    }
*/
    /**
     * 新增 RS 臨時會員
     * 修正：由 GET 改為 POST，並改用 Request Body 接收參數以維護安全
     */
    @PostMapping(value = "/addRSTempMember", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<String> addRSTempMember(@Valid @RequestBody TempMemberRequest request) {
        log.info("新增RS臨時會員：sCenter -> {}, sMemberID -> {}", request.getSCenter(), request.getSMemberID());
        try {
            memberService.addRSTempMember(request.getSCenter(), request.getSMemberID());
            return MemberDTO.Response.success("RS暫時會員新增成功");
        } catch (Exception e) {
            log.error("addRSTempMember 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 更新會員聯絡資料
     * 修正：符合狀態更新語意改為 PUT，敏感個資全面改由 Request Body 傳遞，嚴禁暴露於 URL
     */
    @PutMapping(value = "/updMemberContact", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<String> updMemberContact(@Valid @RequestBody UpdateContactRequest request) {
        log.info("更新會員聯絡資料：sMemberID -> {}", request.getSMemberID());
        try {
            memberService.updateMemberContact(
                    request.getSMemberID(), 
                    request.getSMobile(), 
                    request.getSEmail(), 
                    request.getSAddr()
            );
            return MemberDTO.Response.success("更新聯絡資料成功");
        } catch (Exception e) {
            log.error("updMemberContact 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }
    
    @GetMapping(value = "/getAllCardByMemberID/{sMemberID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<CardDetailResponse>> getAllCardByMemberID(
            @PathVariable("sMemberID") @NotBlank(message = "會員ID不能為空") String sMemberID) {
        log.info("依會員ID查詢所有卡片：sMemberID -> {}", sMemberID);
        try {
            List<CardDetailResponse> list = memberService.getAllCardByMemberID(sMemberID);
            return MemberDTO.Response.success(list);
        } catch (Exception e) {
            log.error("getAllCardByMemberID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getAllCardByID/{sID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<CardDetailResponse>> getAllCardByID(
            @PathVariable("sID") @NotBlank(message = "身分證ID不能為空") String sID) {
        log.info("依身分證ID查詢所有卡片：sID -> {}", sID);
        try {
            List<CardDetailResponse> list = memberService.getAllCardByID(sID);
            return MemberDTO.Response.success(list);
        } catch (Exception e) {
            log.error("getAllCardByID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getPointByMemberID/{sMemberID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PointResponse> getPointByMemberID(
            @PathVariable("sMemberID") @NotBlank(message = "會員ID不能為空") String sMemberID) {
        log.info("依會員ID查詢點數資訊：sMemberID -> {}", sMemberID);
        try {
            PointResponse result = memberService.getPointByMemberID(sMemberID);
            return MemberDTO.Response.success(result);
        } catch (Exception e) {
            log.error("getPointByMemberID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getPointByID/{sID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PointResponse> getPointByID(
            @PathVariable("sID") @NotBlank(message = "身分證ID不能為空") String sID) {
        log.info("依身分證ID查詢點數資訊：sID -> {}", sID);
        try {
            PointResponse result = memberService.getPointByID(sID);
            return MemberDTO.Response.success(result);
        } catch (Exception e) {
            log.error("getPointByID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 補全缺少的路由：依據會員 ID 查詢點數歷程明細
     * 路由對應：/Surrounding/api/app/Member/getPointHistByMemberID/{sMemberID}
     */
    @GetMapping(value = "/getPointHistByMemberID/{sMemberID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<List<LplFile>> getPointHistByMemberID(
            @PathVariable("sMemberID") @NotBlank(message = "會員ID不能為空") String sMemberID) {
        log.info("依會員ID查詢點數歷程明細：sMemberID -> {}", sMemberID);
        try {
            List<LplFile> list = memberService.getPointHistByMemberID(sMemberID);
            return MemberDTO.Response.success(list);
        } catch (Exception e) {
            log.error("getPointHistByMemberID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 臨時會員與正式會員眷屬綁定
     * 修正：由 GET 改為 POST，身分證 ID 與臨時會員 ID 改由 Request Body 傳遞，嚴禁暴露於 URL 路由中
     */
    @PostMapping(value = "/doHouseHold", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<LpjFile> doHouseHold(@Valid @RequestBody MemberDTO.HouseHoldRequest request) {
        log.info("執行眷屬綁定(HouseHold)：sTempMemberID -> {}, sID -> {}", 
                request.getSTempMemberID(), request.getSID());
        try {
            LpjFile result = memberService.doHouseHold(request.getSTempMemberID(), request.getSID());
            return MemberDTO.Response.success(result);
        } catch (Exception e) {
            log.error("doHouseHold 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 臨時會員升級為正式會員
     * 修正：由 GET 改為 POST，敏感個資（身分證、生日、手機等）全面改由 Request Body 傳遞，嚴禁暴露於 URL
     */
    @PostMapping(value = "/doFormal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MemberDTO.Response<String> doFormal(@Valid @RequestBody MemberDTO.FormalMemberRequest request) {
        log.info("轉正式會員：sTempMemberID -> {}, sName -> {}, sID -> {}", 
                request.getSTempMemberID(), request.getSName(), request.getSID());
        try {
            memberService.doFormal(
                    request.getSTempMemberID(),
                    request.getSName(),
                    request.getSID(),
                    request.getSBirthday(),
                    request.getSMobile(),
                    request.getSAddress(),
                    request.getSEmail()
            );
            return MemberDTO.Response.success("轉正式會員成功");
        } catch (Exception e) {
            log.error("doFormal 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 供內部微服務 RPC 呼叫：依會員 ID 取得卡號清單
     * 路由對應：/Surrounding/api/app/Member/getCardNumbersByMemberId/{memberId}
     */
    @GetMapping(value = "/getCardNumbersByMemberId/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getCardNumbersByMemberId(
            @PathVariable("memberId") @NotBlank(message = "會員ID不能為空") String memberId) {
        log.info("Member服務收到內部呼叫，查詢會員 ID: {} 的卡號清單", memberId);
        return lpjFileRepository.findCardNosByLpj01(memberId);
    }
    
    @GetMapping(value = "/getMemberContact/{sMemberID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<MemberContactDTO> getMemberContact(
            @PathVariable("sMemberID") @NotBlank(message = "會員ID不能為空") String sMemberID) {
        log.info("查詢會員聯絡資訊：sMemberID -> {}", sMemberID);
        try {
            MemberContactDTO result = memberService.getMemberContact(sMemberID);
            return MemberDTO.Response.success(result);
        } catch (Exception e) {
            log.error("getMemberContact 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getMemberContactByID/{sID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<MemberContactDTO> getMemberContactByID(
            @PathVariable("sID") @NotBlank(message = "身分證ID不能為空") String sID) {
        log.info("依身分證ID查詢聯絡資訊：sID -> {}", sID);
        try {
            MemberContactDTO result = memberService.getMemberContactByID(sID);
            return MemberDTO.Response.success(result);
        } catch (Exception e) {
            log.error("getMemberContactByID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/getMemberContactByCardID/{sCardID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<MemberContactResponse> getMemberContactByCardID(
            @PathVariable("sCardID") @NotBlank(message = "卡號不能為空") String sCardID) {
        log.info("依卡號查詢聯絡資訊：sCardID -> {}", sCardID);
        try {
            MemberContactResponse result = memberService.getMemberContactByCardID(sCardID);
            if (result == null) {
                log.error("Service 回傳了 null，請檢查是否在組裝過程中發生異常");
                return Response.fail("找不到相關聯絡資訊，請檢查卡號是否正確");
            }
            
            return MemberDTO.Response.success(result);
        } catch (Exception e) {
            log.error("getMemberContactByCardID 處理異常: ", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @GetMapping("/getMainCard/{sCardID}")
    public MemberDTO.Response<MemberDTO.MainCardResponse> getMainCard(@PathVariable String sCardID) {
        MemberDTO.MainCardResponse data = memberService.getMainCard(sCardID);
        return (data != null) 
               ? MemberDTO.Response.success(data) 
               : MemberDTO.Response.fail("查無此卡片資料");
    }        
}