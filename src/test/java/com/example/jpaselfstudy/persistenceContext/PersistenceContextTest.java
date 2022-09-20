package com.example.jpaselfstudy.persistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jpaselfstudy.domain.Member;
import com.example.jpaselfstudy.domain.MemberRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PersistenceContextTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 준영속_상태의_Entity를_추가하면_select_쿼리가_추가로_발생한다_또한_아이디가_자동_생성된다() {
        Member detachMember = Member.builder()
                .id(1L)
                .name("member")
                .build();

        final Member actual = memberRepository.save(detachMember);

        assertThat(actual.getName()).isEqualTo("member");
    }

    @Test
    @DisplayName("준영속 상태의 Entity의 id와 동일한 엔티티가 영속성 컨텍스트에 있는 상태에서 삽입하면 select 쿼리가 발생하지 않고 "
            + "준영속 상태의 Entity를 반환한다")
    void insertDetachEntityWhenEntityWithSameIdExistsInPersistenceContext() {
        Member member = Member.builder()
                .name("member")
                .build();
        final Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.getId());

        Member detachMember = Member.builder()
                .id(1L)
                .name("detachMember")
                .build();
        final Member actual = memberRepository.save(detachMember);

        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("detachMember");
    }

    @Test
    void 삭제() {
        Member member = Member.builder()
                .name("member")
                .build();
        final Member savedMember = memberRepository.save(member);
        entityManager.clear();

        //영속성 컨텍스트에 없으므로 select후 쓰기 지연 저장소에 delete 쿼리 저장 & 영속성 컨텍스트에서 제거
        memberRepository.deleteById(savedMember.getId());

        //member가 조회되지 않는다. Why? 내 Session에만 삭제가 적용되었고 아직 Commit은 일어나지 않은 상태.
        //다른 Session에선 savedMember의 Id로 조회가 가능하다.
        final Optional<Member> deleteMember = memberRepository.findById(savedMember.getId());
        assertThat(deleteMember).isEmpty();
        memberRepository.flush();
    }

    /**
     flush가 일어나는 3가지 경우
     1. em.flush()
     2. 트랜잭션 커밋 시
     3. JPQL 쿼리 실행 시
     */
}
