package jpabook.jpashop.service;

import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor    // final 키워드 붙은 필드에 대해 적용
@Transactional(readOnly = true)// JPA의 모든 데이터 변경은 트랜잭션 내에서 처리되어야함. (from Spring)
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional  // 재 설정
    public long join(Member member) {
        // 중복 회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();  // 영속성 컨텍스트에 member 객체를 넣으면, key는 객체의 pk가 되기 때문에 자동으로 객체의 id를 생성하고, 그래서 여기서 id를 접근할 수 있다.
    }

    // 여기서 체크해주어도 동시에 두 사용자가 체크하면서 통과할 수 있기 때문에
    // 한번더 제약을 걸어주고자, db의 name에 대해 unique 제약조건으로 잡아줘야한다.
    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}
