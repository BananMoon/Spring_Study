package moon.firstspring.controller;

import moon.firstspring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Spring이 시작될 때 Spring Container가 생성이 되는데 이 @Controller를 보고 해당 MemberController 객체(Bean)를 생성해서 Spring Container에 저장하고 관리한다.
// 이를 Spring Bean이 관리된다고 함.
@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }



}
