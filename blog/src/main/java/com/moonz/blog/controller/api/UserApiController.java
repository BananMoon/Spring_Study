package com.moonz.blog.controller.api;

import com.moonz.blog.dto.ResponseDto;
import com.moonz.blog.model.RoleType;
import com.moonz.blog.model.User;
import com.moonz.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserApiController {
    @Autowired      // DI. Spring container가 Bean으로 가지고 있다.
    private UserService userService;


    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user) {
        System.out.println("UserApiController : save 호출됨");
//        return new ResponseDto<Integer>(200, 1);
        // 실제로 DB에 insert하고 아래에서 return.
        user.setRole(RoleType.USER);    // 추가로 강제로 세팅해줘야하는 필드.
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /* 아래는 전통적인 방식
//    @Autowired  // 필요하면 DI에서 받아서 사용할 수 있음.
//    HttpSession session;
    @PostMapping("/api/user/login")
    public ResponseDto<Integer> login(@RequestBody User user) { // HttpSession session을 매개변수로 갖고있을 수도, @Autowired로 DI처리할 수 있음.
        System.out.println("UserApiController : login 호출됨");
        User principal = userService.로그인(user); // 접근 주체 principal

        if (principal != null) {    // null이 아니면
            session.setAttribute("principal", principal);
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    } */
}
