package com.beyoung.member.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.beyoung.member.domain.dto.MemberStatsDTO;

@FeignClient(
	    name = "member-service", 
	    url = "http://localhost:8095", 
	    path = "/Surrounding/api/bonus/Point"
	)
public interface BonusServiceClient {

    /**
     * 遠端呼叫：/Surrounding/api/bonus/point-history
     */
    @PostMapping("/point-history")
    List<Map<String, Object>> getPointHistByMemberID(@RequestBody Map<String, String> params);
    
    @GetMapping("/stats/{memberId}")
    MemberStatsDTO getMemberStats(@PathVariable("memberId") String memberId, 
                                  @RequestParam("start") String start, 
                                  @RequestParam("end") String end);

    
    @PostMapping("/lsm/updateCardId")
    void updateLsmCardId(@RequestParam("oldCardId") String oldCardId, 
                         @RequestParam("newCardId") String newCardId);
}