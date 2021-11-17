package com.moonz.blog.repository;

import com.moonz.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// DAO (Database Access Object)
// 자동으로 bean 등록이 된다. @Repository 생략 가능.
public interface UserRepository extends JpaRepository<User, Long> {
    // JPA Naming 쿼리 : SELECT * FROM USER WHERE username= ? AND password = ?;
    // 로그인을 위한 함수
    User findByUsernameAndPassword(String username, String password);
    // 더 복잡할 경우 Native Query도 가능.
    // @Query(value="SELECT * FROM USER WHERE username= ? AND password = ?")
    // User login (String username, String password);
}
