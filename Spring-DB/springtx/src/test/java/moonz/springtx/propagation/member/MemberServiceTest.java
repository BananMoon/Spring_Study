package moonz.springtx.propagation.member;

import moonz.springtx.propagation.log.LogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    @Test
    void outerTxOff_success() {
        String username = "outerTxOff_success";
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }
    @Test
    void outerTxOff_fail() {
        String username = "로그예외 발생할 moonz";
        memberService.joinV2(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    // 서비스 계층에서 트랜잭션을 시작하고 레파지토리 계층에서는 생성되지 않는다.
    @DisplayName("기본(REQUIRES)이며 단일 트랜잭션 : 논리 트랜잭션이 정상 커밋되는 경우 물리 트랜잭션도 정상 커밋된다.")
    @Test
    void singleTx() {
        String username = "moonz";
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService On
     * MemberRepository On
     * LogRepository On
     */
    @DisplayName("신규 트랜잭션과 내부 트랜잭션이 생성된다.")
    @Test
    void outerTxOn_success() {
        String username = "outerTxOn_success";
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }
    /**
     * MemberService On
     * MemberRepository On
     * LogRepository On
     *
     회원과 회원 이력 로그를 처리하는 부분을 하나의 트랜잭션으로 묶은 덕분에 문제가 발생했을 때 회원과 회원 이력 로그가 모두 함께 롤백된다.
     따라서 데이터 정합성에 문제가 발생하지 않는다.
     */
    @DisplayName("기본적으로 트랜잭션이 전파되므로 LogRepository에서 예외 발생 시 모두 롤백되어야 한다.")
    @Test
    void outerTxOn_fail() {
        String username = "로그예외 발생할 moonz";

        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(RuntimeException.class);

        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * 요구사항 변경 : 회원가입 시 이력 저장에 문제가 발생했을 때 이력은 남기지 못해도 회원가입은 잘 이루어지도록 해야한다.
     * 즉, 회원 가입을 시도한 로그를 남기는데 실패하더라도 회원 가입은 유지되어야 한다.
     * LogRepository에서 예외발생 시 catch하더라도 전파속성이 'REQUIRES'이므로 회원 저장도 롤백된다.
     * 이럴 때 'REQUIRES_NEW' 속성을 사용해야 한다.
     * 이력 저장 시 신규 트랜잭션으로 생성되어야 하므로 LogRepository.save()의 @Transactional에 전파 속성을 설정하면 된다.
     */
    @DisplayName("REQUIRED_이력 저장 실패했지만 회원 저장에 성공해야 한다.")
    @Test
    void saveLog_failed_but_saveMember_succeed_using_REQUIRED_NEW() {
        String username = "로그예외 발생시킬 moonz";
        memberService.joinV2(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
}