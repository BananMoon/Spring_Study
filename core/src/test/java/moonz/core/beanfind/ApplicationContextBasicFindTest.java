package moonz.core.beanfind;

import moonz.core.AppConfig;
import moonz.core.member.MemberService;
import moonz.core.member.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;    // static으로 함. 온디맨드
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByFind() {
        MemberService memberService = ac.getBean(MemberService.class);  // 같은 타입일 경우 곤란
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class); // 구체(AppConfig의 반환) 타입에 의존하기 때문에 유연성 저하
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    // 실패 테스트도 무조건 만들어줘야함
    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX() {
    // org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'xxxx' available
        //여기선 junit.jupiter.api.Assertions 사용
        assertThrows(NoSuchBeanDefinitionException.class, // 해당 예외가 터져야 테스트 성공
                () -> ac.getBean("xxxxx", MemberService.class) );
    }
}