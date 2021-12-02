package moon.firstspring.repository;


import moon.firstspring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository{
    @Override
    public Optional<Member> findByName(String name);
}
