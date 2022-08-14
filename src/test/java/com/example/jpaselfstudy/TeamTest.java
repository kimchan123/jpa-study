package com.example.jpaselfstudy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
class TeamTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final Member member = Member.builder()
            .id(1L)
            .name("name1")
            .build();

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void test() {
//        Member member = Member.builder()
//                .name("name1")
//                .build();

        String[] beanDefinitionNames = webApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("beanDefinitionName = " + beanDefinitionName);
        }
        memberRepository.save(member);
        em.clear();
        memberRepository.save(member);
    }

    //isNew vs Merge
    //id가 null이면 비영속, 있으면 준영속
    //isNew는 그냥 바로 entity를 persist하고 바로 반환
    //merge는 select query가 한번 나가야함
}


