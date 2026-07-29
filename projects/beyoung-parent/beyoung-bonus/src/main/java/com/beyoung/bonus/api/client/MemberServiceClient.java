package com.beyoung.bonus.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// name 填入 member 微服務在註冊中心註冊的服務名稱
// @FeignClient(name = "member-service", path = "/api/internal/members")
@FeignClient(
    name = "member-service", 
    url = "http://localhost:8096", 
    path = "/Surrounding/api/app/Member/internal/members"
)
public interface MemberServiceClient {

    @GetMapping("/{memberId}/card-numbers")
    List<String> getCardNumbersByMemberId(@PathVariable("memberId") String memberId);
}