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

    private final MemberService memberService;

    @Autowired
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
