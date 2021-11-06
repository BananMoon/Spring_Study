package com.moonz.blog.repository;

import com.moonz.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// DAO (Database Access Object)
// 자동으로 bean 등록이 된다. @Repository 생략 가능.
public interface UserRepository extends JpaRepository<User, Long> {
}
