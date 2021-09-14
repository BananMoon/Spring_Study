package com.moonz.springPractice01.controller;

import com.moonz.springPractice01.javaPrac.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// 응답에 최적화된 빵틀 생성!
@RestController
public class CourseController {

    @GetMapping("/courses")
    public Course getCourses() {
        Course course = new Course();
        course.setTitle("웹개발의 봄 스프링");
        course.setDays(35);
        course.setTutor("moonz");
        return course;
    }
    // 이렇게 객체를 반환만 하면 JSON으로 변환해서 브라우저에 응답하는 것을 Spring이 다해준다!
}
