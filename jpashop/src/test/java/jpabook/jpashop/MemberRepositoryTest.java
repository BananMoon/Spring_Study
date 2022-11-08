package jpabook.jpashop;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryV2;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)  // Spring 관련된 것을 테스트하겠다고 JUnit에 알림
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepositoryV2 memberRepository;
    
    @Test
    @Transactional      /* EntityManager를 통해 데이터 변경 시 트랜잭션 내에서 이뤄져야함. */
    @Rollback(false)    /* test에 Transactional이 붙으면 Rollback되므로 false하면 Rollback안되고 commit됨. */
    public void testMember() {
        //given
        Member member = Member.builder()
                .name("moonz")
                .address(new Address("화성시","동탄대로 2길", "19"))
                .build();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Assertions.assertThat(savedMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(savedMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(savedMember).isEqualTo(member);    /* 같은 영속성 컨텍스트 내 1차 캐시에서 식별자로 가져옴 */
    }
}

// 영속성 컨텍스트는 엔티티를 연구 저장하는 환경이다.
// 애플리케이션과 DB 사이에서 객체를 보관하는 가상의 DB 역할을 하는데,
// Entity Manager를 통해 entity를 저장하거나 조회하면, 이를 영속성 컨텍스트에 보관하고 관리합니다.
// em.persist(엔티티);
// entity manager 생성할 때마다 1개의 영속성 컨텍스트가 생성됩니다.
// entity manager를 통해 영속성 컨텍스트에 접근합니다.