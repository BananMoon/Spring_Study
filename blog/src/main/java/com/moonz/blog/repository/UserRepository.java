package com.moonz.blog.repository;

import com.moonz.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// DAO (Database Access Object)
// 자동으로 bean 등록이 된다. @Repository 생략 가능.
public interface UserRepository extends JpaRepository<User, Long> {
    // SELECT * FROM user WHERE username=?;
    Optional<User> findByUsername(String username);
}


// JPA Naming 쿼리 : SELECT * FROM USER WHERE username= ? AND password = ?;
// 로그인을 위한 함수 : seurity 사용했으므로 로그인을 직접 해줄 필요없어!
//    User findByUsernameAndPassword(String username, String password);
// 더 복잡할 경우 Native Query도 가능.
// @Query(value="SELECT * FROM USER WHERE username= ? AND password = ?")
// User login (String username, String password);