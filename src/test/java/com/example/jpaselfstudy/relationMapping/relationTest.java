package com.example.jpaselfstudy.relationMapping;

import com.example.jpaselfstudy.domain.Member;
import com.example.jpaselfstudy.domain.MemberRepository;
import com.example.jpaselfstudy.domain.OrderItem;
import com.example.jpaselfstudy.domain.OrderRepository;
import com.example.jpaselfstudy.domain.Team;
import com.example.jpaselfstudy.domain.TeamRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class relationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void pk가_아닌_column으로_조회하면_즉시로딩이_안된다() {
        final Team team1 = teamRepository.save(Team.builder()
                .name("team1")
                .build());

        final Member member = memberRepository.save(Member.builder()
                .name("member1")
                .team(team1)
                .build());

        final OrderItem order = orderRepository.save(OrderItem.builder()
                .member(member)
                .build());
        em.clear();

        //team 조회하는 쿼리가 따로 나감
        final Optional<Member> result = memberRepository.findById(member.getId());
    }

    @Test
    void 연관관계_주인에서_연관관계를_맺으면_관계가_설정된다() {
        final Team team1 = teamRepository.save(Team.builder()
                .name("team1")
                .build());
        final Team team2 = teamRepository.save(Team.builder()
                .name("team2")
                .build());

        memberRepository.save(Member.builder()
                .name("member1")
                .team(team1)
                .build());
        memberRepository.save(Member.builder()
                .name("member2")
                .team(team1)
                .build());

        memberRepository.save(Member.builder()
                .name("member1")
                .team(team2)
                .build());
        memberRepository.save(Member.builder()
                .name("member2")
                .team(team2)
                .build());
        em.clear();

        final Optional<Team> team = teamRepository.findById(team1.getId());
//        final List<Team> teams = em.createQuery("select t from Team t", Team.class)
//                .getResultList();
//        assertThat(result.getMembers()).hasSize(1);
    }
}

