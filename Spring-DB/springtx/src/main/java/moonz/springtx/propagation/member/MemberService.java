package moonz.springtx.propagation.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moonz.springtx.propagation.log.LogService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final LogService logService;

    public Member joinV1(String username) {
        Member member = new Member(username);
        log.info("== MemberRepository 호출 시작 ==");
        Member savedMember = memberRepository.save(member); // 트랜잭션이 자동으로 생기나?
        log.info("== MemberRepository 호출 종료 ==");

        // LogService.save()가 아닌 Log 객체 생성해서 바로 LogRepository 호출
        logService.save(savedMember.getUsername());

        return savedMember;
    }

    /**
     * 로그 저장 실패해도 회원가입을 완료시키고 싶은 경우
     */
    public Member joinV2(String username) {
        Member member = new Member(username);
        log.info("== MemberRepository 호출 시작 ==");
        Member savedMember = memberRepository.save(member); // 트랜잭션이 자동으로 생기나?
        log.info("== MemberRepository 호출 종료 ==");

        // LogService.save()가 아닌 Log 객체 생성해서 바로 LogRepository 호출
        try {
            logService.save(savedMember.getUsername());
        } catch (Exception e) {
            log.error("== Log 저장에 실패했습니다. : {} ==", savedMember.getUsername());
            log.info("정상 흐름 진행");
        }

        return savedMember;
    }
}