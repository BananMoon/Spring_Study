package moonz.core.member;

public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }   // 인터페이스만 보고 개발하면 된다!

    @Override
    public Member findMember(Long memberId) { // 관례: 구현체가 1개인 경우 "인터페이스명+Impl"
        return memberRepository.findById(memberId);
    }
}
