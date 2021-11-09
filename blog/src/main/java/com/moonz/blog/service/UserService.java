package com.moonz.blog.service;

import com.moonz.blog.model.User;
import com.moonz.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌. IoC(의존성 역전. 메모리에 대신 띄워준다)
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional      // 전체에서 실패하면 Rollback됨. 따로 로직을 짜줘야함. 현재는 1개의 트랜잭션이므로 생략.
    public int 회원가입(User user) {
        try {
            User user1 = userRepository.save(user);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("UserService : 회원가입() : " + e.getMessage());
        }
        return -1; //  에러발생하면 여기로 !
    }
}
