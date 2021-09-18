## API
- 클라이언트와 서버 간의 약속
- 클라이언트가 정한대로 서버에게 요청(Request)을 보내면, 서버가 요구사항을 처리하여 응답(Response)을 반환함

## REST
- 주소에 명사, 요청 방식에 동사(CRUD)를 사용하여 의도를 명확히 드러냄
  - 데이터 생성->POST , 조회->GET , 수정->PUT , 삭제->DELETE 요청에 따라 
- 주소는 보통 단수보단 복수!

## API testing tools
- API 생성 뒤 각종 툴로 테스트 및 기능 확인을 하기 위한 툴
- ARC (Advanced REST Client), Postman, Swagger 등이 있음
<br>

> 예시 : JSON을 응답하도록 할 것이므로 RESTController를 사용해보자.
- GET, POST, PUT, Delete는 자동 응답기인 controller를 사용!
- 코드를 간단히 살펴보자. (몇몇은 다른 파일도 수정해줘야했다. 나머지 수정사항은 아래 링크를 참고하자.)<br>

<br>

## Code

> POST
```java
   @PostMapping("/api/courses")// 해당 주소로 POST 요청(데이터 생성)
    public Course createCourse(@RequestBody CourseRequestDto requestDto) {  // 생성한 Course를 반환
        // @RequestBody를 통해 요청한 정보를 CourseRequestDto requestDto가 받을 수 있음.
        //requestDto는 생성 요청 : 강의 정보(강의명과 튜터명)를 가져오는 역할
        Course course = new Course(requestDto); // 데이터를 생성할 때 Course로 직접적으로 생성하는게 아닌 requestDto로 생성하기로 했으니 관련 생성자를 만들어야함! ㄱㄱ

        //JPA를 이용해서 db에 저장 및 반환
        return courseRepository.save(course);
    }
```

> GET
```java
    @GetMapping("/api/courses")
    public List<Course> getCourses() {
        return courseRepository.findAll();  //SQL로 데이터 조회. 형태는 List<Course> 형태로!
    }
```

> PUT
```java
    // @PathVariable을 통해 {id}에서 받은 값을 메서드에 넣어줌.
    @PutMapping("/api/courses/{id}") // 변경할 id값
    public Long updateCourse(@PathVariable Long id, @RequestBody CourseRequestDto requestDto) {
        return courseService.update(id, requestDto); //update는 CourseService 이용!
    }
```

> DELETE
```java
    @DeleteMapping("/api/courses/{id}")
    public Long deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return id;
    }
```

## API TEST
- 이제 API 테스트 툴인 Postman을 이용해서 잘 동작하는지 **테스트**해보자.
- **GET 요청**할 때는 그냥 'GET'으로 맞추고 테스트하고자하는 api를 url 뒤에 붙여서 (http://localhost:8080/api/courses) 작성한다. 
1. send 누르면 테스트 완료! 200OK가 떠야 성공이다.

- **POST 요청** 시, 약간의 설정이 필요하다.
1. 'POST'로 맞추고 테스트하고자하는 api를 url 뒤에 붙여서(http://localhost:8080/api/courses) 작성한다.
2. Headers 항목에서 json 세팅을 해줘야아한다. 다음과 같이 입력한다.
  - key     : Content-Type
  - value   : application/json

3. Body 항목에서 raw 클릭, JSON 클릭한 후, JSON 형식에 맞게 작성한다.
 ```java
 {
  "title": "앱개발 종합반",
  "tutor": "문윤지"
 }
 ```
 
 - **PUT 요청** 시에는 url을 달리 줘야한다.
 1. 'PUT'으로 맞추고 값을 수정할 id를 url뒤에 붙여준다. (http://localhost:8080/api/courses/1)
 2. POST 방식과 똑같이 JSON을 써주되, 바꾸고 싶은 정보를 입력한다.
 3. Send 클릭 시, 수정된 해당 데이터의 id를 반환한다.

- **DELETE 요청** 시에도 id값을 이용해서 제거한다.
1. 'DELETE'로 맞추고 값을 수정할 id를 url뒤에 붙여준다. (http://localhost:8080/api/courses/1)
2. Send 클릭 시, 제거된 해당 데이터의 id를 반환한다.

<br>
 
> [수정 및 추가 코드 참조](https://github.com/BananMoon/Spring_Study/pull/2/files)

<hr>

# 😙 StudyNote 1~4 간단한 중간 정리 😃
- **Controller & Service & Repository**
  - Controller : 자동 응답기
  - Service : Update할 때 
  - Repository : 쿼리문 수행할 때
- **DTO** : 계층 간 소통을 위해 사용
  - Controller에서 Service/Repository로, Serivice에서 Repository로.. 요청을 받거나 정보를 생성해서 전달할 때! 사용됨
- **Lombok** : 코드를 절약하는 역할
- **REST API** 개념과 CRUD에 해당하는 API를 구축!!!
