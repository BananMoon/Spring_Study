package com.moonz.blog.service;

import com.moonz.blog.model.User;
import com.moonz.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌. IoC(의존성 역전. 메모리에 대신 띄워준다)
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional      // 전체에서 실패하면 Rollback됨. 따로 로직을 짜줘야함. 현재는 1개의 트랜잭션이므로 생략.
    public void 회원가입(User user) {
        userRepository.save(user);  // 에러 발생 시 자동으로 GlobalExcptionHandler 호출
    }

    // 원래는 transaction이 필요없지만.. select가 여러번 조회되더라도 같은 데이터가 찾아지도록 !
    @Transactional(readOnly = true) //  select 시 transaction 시작, 서비스 종료 시에 트랜잭션 종료 (정합성 유지 가능)
    public User 로그인(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
