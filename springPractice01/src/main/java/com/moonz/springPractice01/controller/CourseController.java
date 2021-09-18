package com.moonz.springPractice01.controller;

import com.moonz.springPractice01.domain.Course;
import com.moonz.springPractice01.domain.CourseRepository;
import com.moonz.springPractice01.domain.CourseRequestDto;
import com.moonz.springPractice01.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 응답에 최적화된 빵틀 생성!
@RequiredArgsConstructor
@RestController //json으로 반환이니까
public class CourseController {
    private final CourseRepository courseRepository;    // 꼭 필요하니까 final! lombok까지!
    private final CourseService courseService;

    @PostMapping("/api/courses")// 해당 주소로 POST 요청(데이터 생성)
    public Course createCourse(@RequestBody CourseRequestDto requestDto) {  // 생성한 Course를 반환
        // @RequestBody를 통해 요청한 정보를 CourseRequestDto requestDto가 받을 수 있음.
        //requestDto는 생성 요청 : 강의 정보(강의명과 튜터명)를 가져오는 역할
        //Course를 저장하므로
        Course course = new Course(requestDto); // 데이터를 생성할 때 Course가 아닌 requestDto로 생성하기로 했으니 관련 생성자를 만들어야함!

        //JPA를 이용해서 db에 저장 및 반환
        return courseRepository.save(course);
    }

    // 해당 주소로 GET 요청(데이터 조회)이 오면 해당 메서드 실행!
    @GetMapping("/api/courses")
    public List<Course> getCourses() {
        return courseRepository.findAll();  //SQL로 데이터 조회. 형태는 List<Course> 형태로!
    }
    // 이렇게 객체를 반환만 하면 JSON으로 변환해서 브라우저에 응답하는 것을 Spring이 다해준다!

    // @PathVariable을 통해 {id}에서 받은 값을 메서드에 넣어줌.
    @PutMapping("/api/courses/{id}") // 변경할 id값
    public Long updateCourse(@PathVariable Long id, @RequestBody CourseRequestDto requestDto) {
        return courseService.update(id, requestDto); //update는 CourseService 이용!
    }

    @DeleteMapping("/api/courses/{id}")
    public Long deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return id;
    }
}

