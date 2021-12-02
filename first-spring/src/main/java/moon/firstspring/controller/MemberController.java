package moon.firstspring.controller;

import moon.firstspring.domain.Member;
import moon.firstspring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// Spring이 시작될 때 Spring Container가 생성이 되는데 이 @Controller를 보고 해당 MemberController 객체(Bean)를 생성해서 Spring Container에 저장하고 관리한다.
// 이를 Spring Bean이 관리된다고 함.
@Controller
public class MemberController {
    // memberService를 final로 하지않으면
    private final MemberService memberService;

    @Autowired  // 생성자 주입
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);
        return "redirect:/";    // 회원가입 완료했으니 홈으로 리다이렉트할때는 이렇게 return
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members); // 템플릿을 로딩하면서 값을 전달하고자 할 때 Model 객체 이용해서 addAttribute로 값을 준다.
        // 템플릿에서는 members.XX 로 데이터를 사용할 수 있다.
        return "members/memberList";
    }
}
// 스프링 프레임워크에서 의존성을 주입하는 방법 : 생성자 주입 vs 필드 주입 vs 수정자 주입(setter injection)
// 생성자 주입
// 단일 생성자의 경우 @Autowired 어노테이션을 붙이지 않아도 된다. 생성자가 2개이상인 경우에는 붙여야함.
// 필드를 final로 선언할 수 있다. 즉, 런타임에 객체 불변성을 보장한다. (final로 선언하지 않으면 나중에 변경될 수 있다는 뜻 _ 필드 주입과 수정자 주입)

// 빈을 주입하는 순서가 다르다.
//1. 생성자의 인자에 사용되는 빈을 찾거나 없으면 빈 팩토리에서 만든다.
//2. 그후 찾은 인자 빈으로 주입하려는 빈의 생성자를 호출한다.
//즉, 먼저 빈을 생성하지 않고 주입하려는 빈을 먼저 찾는다.

// 테스트 코드 작성이 용이하다.
// 원하는 객체를 생성하여 생성자에 넣어주면 된다.. (Mockito를 이용해 목킹한 후 테스트를 진행할 필요 없이_ 필드 주입&수정자 주입)