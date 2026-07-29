package com.beyoung.surrounding.member.controller.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.beyoung.surrounding.member.dto.MemberStatsDTO;

@FeignClient(
	    name = "member-service", 
	    url = "http://localhost:8095", 
	    path = "/Surrounding/rest/bonus/Point"
	)
public interface BonusServiceClient {

    /**
     * 遠端呼叫：/Surrounding/rest/bonus/point-history
     */
    @PostMapping("/point-history")
    List<Map<String, Object>> getPointHistByMemberID(@RequestBody Map<String, String> params);
    
    @GetMapping("/stats/{memberId}")
    MemberStatsDTO getMemberStats(@PathVariable String memberId, 
                                  @RequestParam String start, 
                                  @RequestParam String end);

    
    @PostMapping("/lsm/updateCardId")
    void updateLsmCardId(@RequestParam String oldCardId, 
                         @RequestParam String newCardId);
}