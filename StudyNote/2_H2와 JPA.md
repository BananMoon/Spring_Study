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
