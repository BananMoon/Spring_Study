package moon.firstspring;

import moon.firstspring.repository.MemberRepository;
import moon.firstspring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {
    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {    // 의존성 주입
        this.memberRepository = memberRepository;   // 의존관계 세팅
    }


    //  @persistenceContext 를 안붙여줘도 Spring에서 DI해줌
//    private EntityManager em;
//
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository); // memberService는 memberRepository가 필요!
        // 아래에서 등록된 스프링 빈을 여기에 넣어준다.
    }

//    @Bean
//    public MemberRepository memberRepository() {
////        return new MemoryMemberRepository();
//        return new JpaMemberRepository(em); // 스프링데이터 JPA가 자동으로 bean등록해주므로
//    }
}
