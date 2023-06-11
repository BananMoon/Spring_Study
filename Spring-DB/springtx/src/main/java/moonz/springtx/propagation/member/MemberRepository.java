package moonz.springtx.propagation.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Transactional
    Optional<Member> findByUsername(String username);
}
