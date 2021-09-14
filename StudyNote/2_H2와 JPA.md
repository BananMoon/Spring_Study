<center><img width="60%" src="https://user-images.githubusercontent.com/66311276/132437032-3fcab7f1-4599-4b1f-bee6-501ccfb99fdf.png"/></center>

사진에서 오른쪽부터 왼쪽으로 가면서 설명하겠다.
<br>

### RDBMS
- Relational DataBase Management System
- 관계형 데이터베이스로 정보를 저장하는 공간이다.
- RDB라고도 한다.
- 종류 : MySQL, PostgreSQL, Oracle

### H2
- RDBMS의 한 종류
- 서버가 켜져있는 동안에만 작동하고, 서버가 작동을 멈추면 데이터가 모두 삭제되는 RDB (In-memory DB의 대표)
- SQL로 데이터를 조회, 삭제, 생성, 업데이트가 가능하다.
- 연습용으로 좋다.
<center><img width="40%" src="https://user-images.githubusercontent.com/66311276/132974452-b8cfd5b5-c239-4125-b721-d82cef126d6f.JPG"/></center> 
> 위와같이 SQL문으로 매번 작성해줘야할까?<br>
> 아니다. Java명령어를 SQL로 번역해주는 Spring Data JPA를 사용하면 된다.

## JPA
- Java로 코드를 작성(데이터 생성, 조회, 수정, 삭제) 하면 SQL로 번역해주는 번역기
- JPA는 Repository를 통해서만 사용할 수 있다.
> - JPA가 없다면?
> ```java
> String query = "SELECT * FROM EMPLOYEE WHERE id=?";
> Employee employee = jdbcTemplate.queryForObject(
>   query, new Object[] { id }, ew EmployeeRowMapper());
> ```

>- JPA가 있다면?
>```java
>//설정
>implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
>//save a few customers
>repository.save(new Customer("Jack", "Bauer"));
>respository.save(new Customer("CHloe", "O'Brain"));
>```

## Domain과 Repository
- Domain: **Table**과 1대1로 대응되는 역할
- Repository : **SQL** 역할
  - Repository는 interface로 만든다.
  - interface : 클래스에서 멤버가 빠진, 메소드 모음집

<br>

## JPA 심화
- JPA로 CRUD 기능을 할 수 있다.
1. Creat와 Read는 Repository의 `save()`와 `findAll()` 을 이용한다. 
```java
// 데이터 저장하기
repository.save(new Course("프론트엔드의 꽃, 리액트", "임민영"));

// 데이터 전부 조회하기
List<Course> courseList = repository.findAll();
for (int i=0; i<courseList.size(); i++) {
    Course course = courseList.get(i);
    System.out.println(course.getId());
    System.out.println(course.getTitle());
    System.out.println(course.getTutor());
}

// 데이터 하나 조회하기
Course course = repository.findById(1L).orElseThrow(
        () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
);
```

> Spring의 구조
> 1. Controller : 가장 바깥 부분, 요청/응답을 처리
> 2. Service : 중간 부분, 실제 중요한 작동이 많이 발생함
> 3. Repository : 가장 안쪽 부분, DB와 맞닿아 관리(생성, 삭제 등)하는 역할

2. Update
> Update는 Service를 이용해야하기 때문에 Service를 먼저 만든 후 Upddate 메서드를 만든다.
- Course 클래스에 `update` 메소드 추가
- service 패키지 생성 후, `CourseService.java` 생성
- main 클래스에서 update 메서드 호출문 추가

3. Delete
- 데이터 삭제는 `deleteAll()` 메서드로 손쉽게 가능하다.

**[최종 main 클래스 내부 코드]**
```java
@Bean
public CommandLineRunner demo(CourseRepository courseRepository, CourseService courseService) {
    return (args) -> {

// 데이터 생성
        courseRepository.save(new Course("프론트엔드의 꽃, 리액트", "임민영"));

// 데이터 조회
        System.out.println("데이터 인쇄");
        List<Course> courseList = courseRepository.findAll();
        for (int i=0; i<courseList.size(); i++) {
            Course course = courseList.get(i);
            System.out.println(course.getId());
            System.out.println(course.getTitle());
            System.out.println(course.getTutor());
        }

// 데이터 업데이트
        Course new_course = new Course("웹개발의 봄, Spring", "임민영");
        courseService.update(1L, new_course);
        courseList = courseRepository.findAll();
        for (int i=0; i<courseList.size(); i++) {
            Course course = courseList.get(i);
            System.out.println(course.getId());
            System.out.println(course.getTitle());
            System.out.println(course.getTutor());
        }

// 데이터 삭제
        courseRepository.deleteAll();
    };
}
```
