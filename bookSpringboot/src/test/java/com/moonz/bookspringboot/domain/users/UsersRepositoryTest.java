package com.moonz.bookspringboot.domain.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersRepositoryTest {
    @Autowired
    UsersRepository usersRepository;

    @Test
    public void 사용자_불러오기() {
        Long user_id = 11L;
        Optional<Users> result = usersRepository.findById(user_id);

        System.out.println("=============================");
        if(result.isPresent()) {
            Users user = result.get();
            System.out.println(user.getUserId());
            System.out.println(user.getUpdatedAt());
            System.out.println(user.getNowEmotion());
            System.out.println(user.getAdId());
        }
    }
    //1. users 테이블- 특정 userID의 모든 광고 id를 조회 후,
    // advertisement 테이블에서 광고데이터 조회

    // 2. users 테이블- now_emotion 칼럼을 하루 날짜 기준으로 조회

    // 3. users 테이블- 가장 최근의 userID의 now_emotion을 조회
}
