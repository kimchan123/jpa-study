package com.example.jpaselfstudy.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(final String name);

    @Query("select m from Member m join fetch m.orders where m.id = :id")
    Member findByIdByFetch(@Param("id") Long id);

    @Query("select m from Member m join fetch m.orders")
    List<Member> findAllByFetch();
}
