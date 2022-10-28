package com.example.jpaselfstudy.presentation;

import com.example.jpaselfstudy.application.MemberService;
import com.example.jpaselfstudy.application.response.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> findMember() {
        final MemberResponse response = memberService.findMember(1L);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember() {
        memberService.insertMember();
        return ResponseEntity.noContent().build();
    }
}
