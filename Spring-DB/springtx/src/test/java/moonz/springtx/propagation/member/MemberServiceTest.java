package moonz.springtx.propagation.member;

import moonz.springtx.propagation.log.LogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    @Test
    void outerTxOff_success() {
        String username = "moonz";
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.findByMessage(username).isPresent());
    }
    @Test
    void outerTxOff_fail() {
        String username = "로그예외 발생할 moonz";
        memberService.joinV2(username);

        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.findByMessage(username).isEmpty());
    }

// 서비스 계층에서 트랜잭션을 시작하지 않고 레파지토리 계층에서 생성된다.
    @Test
    void singleTx() {
        String username = "moonz";
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.findByMessage(username).isPresent());
    }
}