package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createMemberForm(Model model) {
        //Controller에서 View로 넘어갈 때 해당 데이터를 함께 넘긴다.
        model.addAttribute("memberForm", new MemberForm());
         return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String createMember (@Valid MemberForm memberForm, BindingResult result) { // @Valid: MemberForm의 valid 애노테이션을 통해 검증 진행
        // @Valid 인자 뒤에 BindingResult 인자를 넣으면, (에러로 튕기지않고) 에러가 해당 객체에 담겨서 메서드가 실행이 됨.
        if (result.hasErrors()) {
            return "members/createMemberForm";  // Spring이 memberForm과 BindingResult까지 함께 Form에 전달하도록 함.
        }
        Address address = memberForm.createAddress();
        Member member = Member.builder()
                .name(memberForm.getName())
                .address(address)
                .build();
        memberService.join(member);
        return "redirect:/";  //재로딩되기보단 폼에게 1번째 페이지로 리다이렉트
    }

    /**
     * 모든 회원을 조회하는 요청에 응답한다.
     * 해당 메서드에서도 Member를 그대로 전달하기 보다, DTO를 사용하는 것을 권장한다.
     * 특히, ServerSide에서 끝나는게 아닌, 단지 API 서버로서 사용될 때는 Entity를 외부로 반환하면 절대 안된다. 필수로 DTO를 사용하자.
     */
    @GetMapping("/members")
    public String findMember (Model model) {   // 화면에 데이터를 전달하기위한 객체
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
