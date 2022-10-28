package com.example.jpaselfstudy.replication;

import com.example.jpaselfstudy.application.MemberService;
import com.example.jpaselfstudy.config.replication.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({DatabaseConfig.class})
public class ReplicationTest {

    @Autowired
    private MemberService memberService;

    @Test
    void transactional_readonly_true() {
        memberService.findMember(1L);
    }

    @Test
    void transactional_readonly_false() {
        memberService.insertMember();
    }
}
