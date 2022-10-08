package com.example.jpaselfstudy.relationMapping;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jpaselfstudy.domain.Member;
import com.example.jpaselfstudy.domain.MemberRepository;
import com.example.jpaselfstudy.domain.Team;
import com.example.jpaselfstudy.domain.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CascadeTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    /**
     * ## 영속성 전이 주의할점
     * 1. 부모에서만 추가해줘도 자식이 같이 저장은 된다. 하지만, 자식의 FK는 null로 설정됨. 따라서, 자식도 연관관계를 설정해줘야 함
     * 2. OrphanRemoval = true 이면 참조가 제거된 자식 엔티티는 자동으로 삭제된다.
     * 3. CascadeType.ALL + orphanRemoval = true를 사용하면 부모 엔티티를 통해 자식의 생명주기를 관리할 수 있다.
     * -> 보통 DDD의 aggregate root에서 사용하는 pattern 인 것 같다.
     */

    @Test
    void 영속성_전이_시_자식도_연관관계를_맺어줘야_한다() {
        final Team team = Team.builder()
                .name("team1")
                .build();

        final Member member = Member.builder()
                .name("member1")
                .build();

        team.addMember(member);

        teamRepository.save(team);
        assertThat(member.getTeam()).isNull();
    }

    @Test
    @DisplayName("orphanRemoval = true이면 참조가 제거된 자식 엔티티는 자동으로 삭제된다.")
    void orphanRemoval_true() {
        final Team team = Team.builder()
                .name("team1")
                .build();

        final Member member = Member.builder()
                .name("member1")
                .team(team)
                .build();

        team.addMember(member);
        final Team savedTeam = teamRepository.save(team);
        savedTeam.getMembers().remove(0);

        assertThat(memberRepository.findAll()).isEmpty();
    }
}
