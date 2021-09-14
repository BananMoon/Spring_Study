package com.moonz.springPractice01.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// SQL역할
// JPA Repository를 상속받아서 기능 사용
public interface CourseRepository extends JpaRepository<Course, Long> {
//delteAll(), findAll(), findById() 등을 상속받음
}

