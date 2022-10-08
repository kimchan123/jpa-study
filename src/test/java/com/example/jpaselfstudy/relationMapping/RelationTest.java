package com.example.jpaselfstudy.relationMapping;

import static org.assertj.core.api.Assertions.assertThat;

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
public class RelationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * ## 연관관계 주의할 점
     * 1. 연관관계 주인에서 연관관계를 맺으면 관계가 설정된다. 그 반대는 성립 x (하지만, 연관관계 편의 메서드에서 양쪽다 연관을 설정해주는 것이 좋다.)
     * 2. XToOne은 즉시 로딩이 Default, XToMany는 지연 로딩이 Default 이다.
     * 3. 즉시 로딩은 findById 에만 N+1이 발생하지 않는다. 그 외의 쿼리에선 N+1이 발생하는 것으로 보임.(정확히 말하면 PK로 조회)
     * // 4, 5의 경우 JoinColumn의 nullable을 말하는 것임. 즉, 외래키의 nullable 여부
     * 4. 즉시 로딩의 경우 JoinColumn(nullable = false) 설정을 하면 내부 조인 사용, 반대는 외부 조인 사용(N 쪽에서 조인할 경우)
     * 5. 즉시 로딩의 경우 XToMany에선 optional과 상관없이 항상 외부 조인 사용(1 쪽에 해당하는 외래키를 N 쪽에서 안가지고 있을 수도 있어서 조회가 안되는 경우도 있음)
     * 6. 양방향에서 ManyToOne, OneToMany 둘다 즉시로딩을 사용하면 정상적으로 작동 안함(아마, 순환참조 형식이라 그런 것 같음)
     */
    @Test
    void 연관관계_주인에서_연관관계를_맺으면_관계가_설정된다() {
        final Team team1 = teamRepository.save(Team.builder()
                .name("team1")
                .build());

        final Member member = memberRepository.save(Member.builder()
                .name("member1")
                .team(team1)
                .build());
        memberRepository.save(Member.builder()
                .name("member2")
                .team(team1)
                .build());
        em.clear();

        final Team team = teamRepository.findById(team1.getId()).get();
        team.getMembers().get(0);
//        final Member resultMember = memberRepository.findById(member.getId()).get();
    }

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
    void N_쪽에서_1_쪽을_즉시로딩으로_불러올_때_nullable_false이면_inner_join() {
        final Team team1 = teamRepository.save(Team.builder()
                .name("team1")
                .build());

        final Member member = memberRepository.save(Member.builder()
                .name("member1")
                .team(team1)
                .build());
        em.clear();

        final Member result = memberRepository.findById(member.getId()).get();
        final Team team = result.getTeam();
        System.out.println(team);
        assertThat(team.getName()).isNotNull();
    }

    @Test
    void One_쪽에서_N_쪽을_즉시로딩으로_불러올_때_nullable에_상관없이_항상_left_outer_join() {
        final Team team1 = teamRepository.save(Team.builder()
                .name("team1")
                .build());

        final Member member = memberRepository.save(Member.builder()
                .name("member1")
                .team(team1)
                .build());
        em.clear();

        final Optional<Member> byId = memberRepository.findById(member.getId());
    }
}
