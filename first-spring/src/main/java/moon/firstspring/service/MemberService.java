package moon.firstspring.service;

import moon.firstspring.domain.Member;
import moon.firstspring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository; // 매번 생성되도록 하지말고

    @Autowired  // memberRepository에 DI
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;   // 직접 new하지 않고, 외부에서 넣어주는 DI! (Dependency Injection)
    }

    /**
     * 회원가입
     **/
    public Long join (Member member) {
        // 중복회원 있는지 확인
        validateDuplicateMember(member);

        // 없으면 회원가입
        member.setName(member.getName());
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }
    //   Optional로 감쌌기 때문에 아래 ifPresent처럼 Optional의 여러 메서드를 쓸 수 있음.
    // Optional 객체니까 따로 저장하지 말고 ifPresent를 사용할 수있음.
        /*
        memberRepository.findByName(member.getName());
            ifPresent(m -> {
                throw new IllegalArgumentException("이미 존재하는 회운입니다.");
            });
         */

    /**
    * 전체 조회
     **/
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne (Long memberId) {
        return memberRepository.findById(memberId);
    }
}
