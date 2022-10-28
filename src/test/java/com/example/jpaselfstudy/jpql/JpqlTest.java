package com.example.jpaselfstudy.jpql;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jpaselfstudy.domain.Member;
import com.example.jpaselfstudy.domain.MemberRepository;
import com.example.jpaselfstudy.domain.Team;
import com.example.jpaselfstudy.domain.TeamRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class JpqlTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void inner_join() {
        final List<Object[]> result = em.createQuery("select m, t from Member m join m.team t").getResultList();

        for (Object[] row : result) {
            Member member = (Member) row[0];
            Team team = (Team) row[1];
            System.out.println(team);
        }
    }

    @Test
    void left_join() {
        final List<Member> members = em.createQuery("select m from Team t left join t.members m", Member.class)
                .getResultList();

        assertThat(members).hasSize(2);
    }

    /**
     * Fetch Join
     * 1. fetch join은 연관된 엔티티나 컬렉션을 한 번에 같이 조회하는 JPQL에서 성능 최적화를 위해 제공하는 기능
     * 2. 별칭 사용 가능 (Hibernate에서는)
     * 3. N:1 or 1:1 관계에선 조인 시 결과가 증가하지 않는다. 반면, 1:N 관계에서 조인 시 결과가 중복되어 증가하게 된다.
     * ex) Member : Team = N : 1 -> Team에서 Members를 fetch join하면 team에 해당하는 N개의 member가 존재할 것임.
     *     이 때, team은 하나만 생기면 되지만, 해당되는 Member가 N개 이므로 중복된 N개가 조회된다.
     *     -> distinct를 사용하면 중복 제거 가능
     * 4. 둘 이상의 컬렉션을 Fetch join 할 수 없다. Cartesian 곱이 만들어지므로 구현체에서 자체적으로 차단하는 것 같다.
     *    MultipleBagFetchException 발생
     * 5. Collection을 fetch join하면 paging API 사용 불가능.
     */
    @Test
    void fetch_join_when_N_to_one() {
        final List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                .getResultList();

        //lazy loading 발생 안함
        for (Member member : members) {
            System.out.println("username = " + member.getName() + ", " + "teamname = " + member.getTeam().getName());
        }
    }

    @Test
    void fetch_join_when_one_to_N() {
        final Team team = em.createQuery("select t from Team t join fetch t.members", Team.class)
                .getSingleResult();

        final List<Member> members = team.getMembers();
        assertThat(members).hasSize(2);
    }
}
