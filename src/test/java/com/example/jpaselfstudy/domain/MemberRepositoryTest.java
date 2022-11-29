package com.example.jpaselfstudy.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void test() {
        final Member member1 = memberRepository.save(new Member("member1"));
        final Member member2 = memberRepository.save(new Member("member2"));
        for (int i=0; i<5; i++) {
            orderRepository.save(new OrderItem(member1));
            orderRepository.save(new OrderItem(member2));
        }

        entityManager.flush();
        entityManager.clear();

        final List<Member> members = memberRepository.findAllByFetch();


    }
}
