package moon.firstspring;

import moon.firstspring.repository.MemberRepository;
import moon.firstspring.repository.MemoryMemberRepository;
import moon.firstspring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepostiroy()); // memberService는 memberRepository가 필요!
        // 아래에서 등록된 스프링 빈을 여기에 넣어준다.
    }

    @Bean
    public MemberRepository memberRepostiroy() {
        return new MemoryMemberRepository();
    }
}
