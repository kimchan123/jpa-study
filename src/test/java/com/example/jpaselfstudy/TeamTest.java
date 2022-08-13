package com.example.jpaselfstudy;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.transaction.TestTransaction;

@DataJpaTest
class TeamTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void test() {
        Team team1 = Team.builder()
                .name("team1")
                .build();
        Team saveTeam1 = teamRepository.save(team1);

        Team team2 = Team.builder()
                .name("team2")
                .build();
        Team saveTeam2 = teamRepository.save(team2);

        Member member1 = Member.builder()
                .name("member1")
                .team(team1)
                .build();
        Member saveMember = memberRepository.save(member1);

        em.flush();
        em.clear();

        Team findTeam1 = teamRepository.findById(1L).get();

        List<Member> members = findTeam1.getMembers();
        Member member = members.get(0);
        member.setName("changedMember");
        TestTransaction.flagForCommit(); // need this, otherwise the next line does a rollback
        TestTransaction.end();
    }
}
