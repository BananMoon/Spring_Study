package com.moonz.securitypractice.v1.repository;

import com.moonz.securitypractice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByUsername(String username);
    boolean existsByUsername(String username);
}
