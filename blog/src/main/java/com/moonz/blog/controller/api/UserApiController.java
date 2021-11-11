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

@RestController
public class UserApiController {
    @Autowired      // DI
    private UserService userService;

    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user) {
        System.out.println("회원가입이 호출됨");
//        return new ResponseDto<Integer>(200, 1);
        // 실제로 DB에 insert하고 아래에서 return.
        user.setRole(RoleType.USER);    // 추가로 강제로 세팅해줘야하는 필드.
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
