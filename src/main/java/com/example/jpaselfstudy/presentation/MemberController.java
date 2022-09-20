package com.example.jpaselfstudy.presentation;

import com.example.jpaselfstudy.application.MemberService;
import com.example.jpaselfstudy.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/api/members")
    public List<String> findMembers() {
        List<Member> members = memberService.findAll();
        return members.stream()
                .map(Member::getName)
                .collect(Collectors.toList());
    }
}
