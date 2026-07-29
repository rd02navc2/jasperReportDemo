package com.beyoung.member.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beyoung.member.application.MemberService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/Surrounding/api/app/Member/internal/members")
@RequiredArgsConstructor
public class InternalMemberController {

    private final MemberService memberService;

    // 將路徑修正為與 FeignClient 完全一致
    @GetMapping("/{memberId}/card-numbers")
    public List<String> getCardNumbers(@PathVariable("memberId") String memberID) {
        return memberService.getCardNumbersByMemberID(memberID);
    }
}