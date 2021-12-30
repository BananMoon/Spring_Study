package moonz.core;

import moonz.core.member.Grade;
import moonz.core.member.Member;
import moonz.core.member.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
        //   수정
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        // 스프링 : @Bean을 모두 관리하는 스프링 컨테이너 를 생성!
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);    // AppConfig 파일에 있는 환경 설정정보를 이용해서 Spring Container에 객체를 넣어서 관리하도록 한다.
        // 스프링 :  직접 찾아오는게아닌 스프링 컨테이너를 통해서 가져온다. @Bean 등록한 메서드 이름과 타입으로 찾아 꺼낸다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);   // Ctrl+ alt + V : 변수 생성
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        System.out.println("new Member= " + member.getName());
        System.out.println("find Member= " + findMember.getName());
    }
}
