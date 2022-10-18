package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
// JUnit4
@RunWith(SpringRunner.class)    // Spring과 함께 실행하겠다.
@SpringBootTest // Spring context와 함께 동작하도록.
@Transactional  // test case에 있으면 작업 후 롤백! (영속성 컨텍스트에서 flush X)
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false)    // commit이 되서 insert 실행됨.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member("moon", new Address("화성시", "동탄대로 2길", "23242"));
        //when
        Long saveId = memberRepository.save(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class )
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member("kim", new Address("화성시", "동탄대로 2길", "23242"));
        Member member2 = new Member("kim", new Address("화성시", "동탄대로 2길", "23242"));

        //when
        memberService.join(member1);
        memberService.join(member2);   // 예외 발생해야함!!
/*
        try {
        } catch (IllegalArgumentException e) {
            return;
        };
*/
        //then
        fail("예외가 발생해야하는데 발생하지 않았다!!");
    }
}