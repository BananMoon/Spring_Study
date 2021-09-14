package com.moonz.springPractice01;

import com.moonz.springPractice01.domain.Course;
import com.moonz.springPractice01.domain.CourseRepository;
import com.moonz.springPractice01.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@EnableJpaAuditing  // 수정일자가 spring에 반영될 수 있도록 함
@SpringBootApplication
public class SpringPractice01Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringPractice01Application.class, args);
    }

    // Week02Application.java 의 main 함수 아래에 붙여주세요.
    // 임시로, jpa 사용법을 아는 것에 집중!
    @Bean
    public CommandLineRunner demo(CourseRepository courseRepository, CourseService courseService) {
        return (args) -> {
        // 데이터 저장하기
            courseRepository.save(new Course("프론트엔드의 꽃, 리액트", "임민영"));
// 아래 코드를 한 줄로!
//			Course course1 = new Course("웹개발의 봄 Spring", "문윤지");
//			repository.save(course1);

        // 데이터 전부(List) 조회하기
            System.out.println("=======전체 데이터 조회 및 인쇄=======");
            List<Course> courseList = courseRepository.findAll();

            //Course course는 courseList의 객체들에 대해서 모두 반복하면서 getId(), getTitle(), getTutor() 실행
            for (Course course : courseList) {
                System.out.println(course.getId()); // 1
                System.out.println(course.getTitle());  // 임민영
                System.out.println(course.getTutor());  // 프론트엔드의 꽃, 리액트
            }

//			for (int i=0; i<courseList.size(); i++) {
//				Course course = courseList.get(i);
//				System.out.println(course.getId());
//				System.out.println(course.getTitle());
//				System.out.println(course.getTutor());
//			}

        // 데이터 하나 조회하기
            System.out.println("=======데이터 1개 조회 및 인쇄=======");
            Course one_course = courseRepository.findById(1L).orElseThrow(    //-> Optional 에러: id가 없을 때 어떻게 할거니?
                //id(Pointer)가 없을 때(Null) Exception을 한개 만들어(new)
                    () -> new NullPointerException("아이디가 존재하지 않습니다.")
            );

        // 데이터 한개 수정하기
            Course new_course = new Course("웹개발의 봄, Spring", "임민영");
            courseService.update(1L, new_course);   //id와 course 정보 전달
            courseList = courseRepository.findAll();

            for (Course course: courseList) {
                System.out.println(course.getId());
                System.out.println(course.getTitle());
                System.out.println(course.getTutor());
            }

            courseRepository.deleteAll();
        };
    }
}

