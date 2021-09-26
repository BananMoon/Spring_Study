프로젝트 만들고 API(클라이언트와 서버 간 약속) 설계하기

- Controller - Service - Repository 3계층 존재!
- Repository, Service, Controller 방향 즉, 바깥 -> 안쪽 방향으로 들어가면서 만들어보겠다.

#### 0. 프로젝트 Setting
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

#### 1. 설계

> 타임라인 API <br>

| 기능 | Method | URL | Return |
| :-: | :-: | :-: | :-: | 
| 메모 생성 | POST | /api/memos | Memo |
| 메모 조회 | GET | /api/memos | List<Memo> |
| 메모 변경 | PUT | /api/memos/{id} | Long |
| 메모 삭제 | DELETE | /api/memos/{id} | Long |  
  
<br>
> 필요한 클래스
1) 익명의 작성자 이름 (username)
2) 메모 내용 (contents)
