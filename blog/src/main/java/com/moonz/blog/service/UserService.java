package com.moonz.blog.service;

import com.moonz.blog.model.User;
import com.moonz.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌. IoC(의존성 역전. 메모리에 대신 띄워준다)
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Long 회원가입(User user) {
        try {
            User user1 = userRepository.save(user);
            return user1.getId();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("UserService : 회원가입() : " + e.getMessage());
        }
        return -1L; //  에러발생하면 여기로 !
    }
}
