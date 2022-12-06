package springdb1.jdbc.service;

import lombok.RequiredArgsConstructor;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepositoryV1;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepositoryV1;

    /**
     * 계좌 이체
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV1.findById(fromId);
        Member toMember = memberRepositoryV1.findById(toId);
        memberRepositoryV1.update(fromId, fromMember.getMoney() - money);

        validation(toMember);
        memberRepositoryV1.update(toId, toMember.getMoney() + money);

    }

    /**
     * 검증: 수신자가 "ex"일 경우 예외 상황 발생
     */
    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }

}
