package springdb1.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import springdb1.jdbc.domain.Member;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
@Slf4j
class MemberRepositoryV0Test {
    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    /**
     * MemberRepositoryV0을 이용해서 쿼리를 수행할 때마다 커넥션이 새로 생성되서 수행된다. 총 6번의 커넥션이 생성되는 것을 확인.
     */
    @Test
    void crud() throws SQLException {
        /* save */
        Member member = new Member("memberV4", 10000);
        repositoryV0.save(member);

        /* findById */
        Member findMember = repositoryV0.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        log.info("findMember == member : {} ", findMember == member);   /* 참조값 비교 */
        assertThat(findMember).isEqualTo(member);    /* 참조값 비교가 아닌 객체 필드 값 비교 */

        /* update */
        repositoryV0.update(member.getMemberId(), 20000);
        Member updatedMember = repositoryV0.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        /* delete */
        repositoryV0.delete(member.getMemberId());
        assertThatThrownBy(() -> repositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}