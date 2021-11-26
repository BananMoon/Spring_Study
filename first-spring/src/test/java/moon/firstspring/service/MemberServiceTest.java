package moon.firstspring.service;

import moon.firstspring.domain.Member;
import moon.firstspring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memoryMemberRepository;
//    MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    // MemberService에서 생성한 memberRepository와 다른 인스턴스이므로
    // 테스트가 동일 환경에서 이루어지지 않을 수 있음.

    @BeforeEach
    void beforeEach() { // 테스트가 시작하기전에 실행되는 메서드
        memoryMemberRepository = new MemoryMemberRepository();
        memberService =new MemberService(memoryMemberRepository);
    }

    @AfterEach
    void afterEach() {
        memoryMemberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring1");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        Member member2 = new Member();
        member2.setName("spring1");

        //when. 2번째에서 예외가 발생할 테니 try catch로 예외를 잡아본다.
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> { memberService.join(member2);
        }); // 2번째인자인 람다함수 로직이 실행될 때 1번째 인자인 Illegal 에러가 발생하길 기대한다.
        // 만약 NullPointerExException.class를 입력할 경우 테스트 실패!
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");    //메시지 검증은 assertThrows에서 반환하는 에러를 이용!

        /* 번거로운 try catch 대신
        try {
            Long saveId2 = memberService.join(member2);
            fail(); // 예외 못잡으면 실행
        } catch (IllegalArgumentException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
            // 예외 잡았으면 에러메시지가 똑같은지 확인
        }
        */
    }

    @Test
    void 전체조회() {

    }

    @Test
    void 회원조회() {

    }
}