package com.example.jpaselfstudy.application;

import com.example.jpaselfstudy.domain.Member;
import com.example.jpaselfstudy.domain.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
