package com.moonz.springPractice01.service;

import com.moonz.springPractice01.domain.Course;
import com.moonz.springPractice01.domain.CourseRepository;
import com.moonz.springPractice01.domain.CourseRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor    //courseRepository처럼 필요한 생성자를 대신 생성해주는 lombok
@Service // 스프링에게 이 클래스는 '서비스'임을 명시. Update될 수 있음을 알려줌
public class CourseService {

    // 조회나 업데이트할 때, CourseRepository가 필요함
    // final: 서비스에게 꼭 필요한 녀석임을 명시 & 값 변형 X
    private final CourseRepository courseRepository;

    // 생성자를 통해, Service 클래스를 만들 때 꼭 Repository를 넣어주도록
    // 스프링에게 알려줌 -> CourseService 생성하면 자동으로 Repository를 넘겨주니까 쓸 수 있음.
//    public CourseService(CourseRepository courseRepository) {
//        this.courseRepository = courseRepository;
//    } -> courseRepository 생성자대신 lombok으로!

    @Transactional // SQL 쿼리가 일어나야 함을 스프링에게 알려줌
    public Long update(Long id, CourseRequestDto requestDto) {    // UPDATE할 때 필요한 정보
        Course course1 = courseRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );      // ID를 찾으면
        course1.update(requestDto);     //course 정보가 Course.java의 update 메서드(로 넘어감
        return course1.getId();     //Update하면 id값을 return해서 알려줌
    }
}