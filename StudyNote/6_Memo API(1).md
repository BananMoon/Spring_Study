프로젝트 만들고 API(클라이언트와 서버 간 약속) 설계하기

- Controller - Service - Repository 3계층 존재!
- Repository, Service, Controller 방향 즉, 바깥 -> 안쪽 방향으로 들어가면서 만들어보겠다.

### 0. 프로젝트 Setting
> 이전과 똑같이 [SpringBoot 페이지](https://start.spring.io/)를 이용한다.<br>

**:bus: 참고**
- Group: com.moonz
- Artifact: memoProject
- Spring Boot : 2.5.5
- Packaging : Jar
- **Type: Gradle**
- **Language: Java**
- **Java Version: 8**
- Dependencies :
  - Lombok
  - Spring Web
  - Spring Data JPA
  - H2 Database
  - MySQL Driver
<hr>

### 1. 설계
> 타임라인 API <br>

| 기능 | Method | URL | Return |
| :-: | :-: | :-: | :-: | 
| 메모 생성 | POST | /api/memos | Memo |
| 메모 조회 | GET | /api/memos | List<Memo> |
| 메모 변경 | PUT | /api/memos/{id} | Long |
| 메모 삭제 | DELETE | /api/memos/{id} | Long |  
<br>
  
> 필요한 변수<br>
1) 익명의 작성자 이름 (username)
2) 메모 내용 (contents)
<hr>

### 2. Domain 디렉토리
#### :one: Memo 클래스 (Memo.java)
- 멤버변수 id, username, contents
- `@Getter`, `@NoargsConstructor`, `@Entity` 어노테이션
- 매개변수있는 생성자
  
#### 2️⃣ Memo 레파지토리 (MemoRepository.java)
**# SQL 역할로, DB를 관리하는 클래스**
- `JpaRepository<Memo, Long>` 상속
- JpaRepository 인터페이스가 갖고있는 메서드 외에 내가 원하는 식으로 쿼리문을 생성하고 싶다면?
  - [여기](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)에서 원하는 쿼리문을 찾아서 생성!
  - 본인은 전체를 찾는데, ModifiedAt 칼럼 기준으로 내림차순해서 정렬해줘! => `findAllByOrderedByModifiedAtDesc()`
  
#### :three: Memo Dto (MemoRequestDto.java)
**# update 등을 목적으로 객체를 전달할 때 사용되는 Data Transfer Object**
- Memo 클래스와 유사하게, 멤버변수와 매개변수 있는 생성자 선언
- `@Getter`, `@NoArgsConstrutor`
  
#### 4️⃣ Timestamped (Timestamped.java)
**# 데이터 생성&수정 시 자동으로 날짜를 입력시키는 역할**
- LocalDateTime 자료형 이용
<hr>
  
### 3. Service 디렉토리
**# 데이터 수정 시에 사용됨**
- **update**를 위해서는 `id`와 `MemoRequestDto` 인자가 필요
- MemoRepository로 `findById()`로 id에 해당하는 객체를 받아와서 (`NullPointerException` 예외처리)<br>
  RequestDto로 전달된 객체정보로 UPDATE 시킨다. (Memo 클래스의 `update()` 호출)
<hr>  
  
### 4. Controller 디렉토리
**# REST API 호출에 응답하는 역할**
- 이전에 Person 클래스로 REST API를 다룬 것과 동일하게 작성해주면 되기 때문에 생략
- **중요**
  - POST나 PUT 요청의 경우, json 형태 데이터를 body에 담아서 보내기 때문에, 해당 메서드의 매개변수에서 `@RequestBody`를 붙여줘야한다.
  - memoRepository와 memoService는 필수적으로 필요한 멤버변수 이므로 <br>
  `private final` 키워드를 붙여 선언해줘야하고, 이에 대한 생성자를 생성해주는 `@RequiredArgsConstructor` 를 달아준다.
