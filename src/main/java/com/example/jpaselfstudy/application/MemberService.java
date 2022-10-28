package com.example.jpaselfstudy.application;

import com.example.jpaselfstudy.application.response.MemberResponse;
import com.example.jpaselfstudy.domain.Member;
import com.example.jpaselfstudy.domain.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findMember(final Long id) {
        final Optional<Member> member = memberRepository.findById(id);
        return new MemberResponse(member.get().getName());
    }

    @Transactional
    public void insertMember() {
        memberRepository.save(Member.builder()
                .name("member")
                .build()
        );
    }
}
