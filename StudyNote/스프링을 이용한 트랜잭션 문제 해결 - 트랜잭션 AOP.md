## 프록시의 필요성
V3_2에서는 트랜잭션 템플릿 (클래스)을 이용하여 기존 서비스 단의 트랜잭션 관련 로직을 제거할 수 있도록 하였다.
그래서 현재는 클라이언트에서 서비스 단을 호출하면, 서비스 레이어에서 트랜잭션 시작 - 비즈니스 로직 진행 (이때 레파지토리 이용) - 트랜잭션 종료 순으로 진행되었다.

그러나 아예 서비스 레이어에서 트랜잭션 관련 로직을 제거하기 위해
스프링은 프록시를 제공하여 서비스 레이어에는 비즈니스 로직만 집중 구현하도록 돕는다.

프록시 기술을 도입함으로 인해, 클라이언트는 실제 서비스 레이어가 아닌, 트랜잭션 프록시를 호출하게 된다.
그럼 프록시에서 트랜잭션을 시작하고, 프록시가 **실제 서비스 레이어를 호출**하여 수행한 뒤, 프록시에서 트랜잭션을 종료한다.

이로 인해 트랜잭션 처리 객체와 비즈니스 로직 처리하는 서비스 객체를 명확하게 분리하게 된다.

## 트랜잭션 AOP
그리고 위를 처리하기 위해 스프링은 ***스프링이 제공하는 AOP 기능을 이용해서 프록시 기술을 적용***할 수 있다.

직접 개발자가 AOP 기술을 이용, 즉 `@Aspect` , `@Advice` , `@Pointcut` 를 사용해서 트랜잭션 처리용 AOP를 만들 수도 있다.

그러나 '트랜잭션' 기능은 매우 중요하기도 하고 전 세계 개발자들이 사용하고 있는 기능이기 때문에 이미 brilliant 스프링이 자체적으로 `트랜잭션 AOP`를 위한 기능을 미리 만들어서 제공하고 있다.
스프링 부트를 사용하면, 트랜잭션 AOP를 처리하기 위해 필요한 스프링 빈(bean)들도 자동 등록되어 있다.

결국 개발자는 직접 트랜잭션 처리하는 AOP 기술을 만들 필요 없이 
트랜잭션 처리가 필요한 곳에 `@Transactional` 애노테이션을 붙이면 된다.
스프링의 트랜잭션 AOP는 해당 애노테이션을 인식해서 
애노테이션이 붙은 로직에 트랜잭션 프록시를 적용하는 것이다.

-  `org.springframework.transaction.annotation.Transactional`

>참고<br>
>해당 애노테이션은 클래스 단위에 붙여도 된다. 이때는 `public` 접근 제어인 메서드들이 수행 시 모두 적용된다.



## 적용 후 테스트 코드 작성
> [참고 코드](추후 등록 예정)

#### 주의 점 (에러 발생 : Failed to obtain JDBC Connection)  
- 트랜잭션 AOP도 내부적으로 트랜잭션 매니저를 이용한다. 이때 스프링 컨테이너에 등록된 Bean을 이용하는 것이기 때문에 
  스프링이 자동으로 등록하는 Bean + 추가 필요한 Bean들을 필요로 한다.  
* 스프링이 기본으로 등록하는 Bean을 이용하기 위해 `@SpringBootTest`와 
  추가 빈 등록을 위해 `@TestCongiguration` 내부 클래스 내에 빈을 등록하였다.
  -> 테스트 환경에서 빈을 등록해놓고 스프링 컨테이너를 이용하도록 한다.  
- 테스트에서 `@Autowired` 를 통해 스프링 컨테이너가 관리하는 빈들을 주입하여 사용할 수 있다.


#### 알아둘 점  
- MemberServiceV3_3에는 프록시가 주입되고, 비즈니스 로직 수행 시 실제 메서드를 호출한다.
- 클라이언트가 서비스 요청 시, 프록시는 트랜잭션 AOP(`@Transactional`)이 붙은 메서드를 상속받아서 오버라이딩하여 해당 메서드가 실행된다.  

---

## 트랜잭션 AOP 적용 흐름
1. 클라이언트가 서비스 호출 시, AOP 프록시가 호출된다.
2. AOP 프록시에서는 빈으로 등록되어있는 트랜잭션 매니저를 획득해 트랜잭션을 시작한다.
	1. 데이터 소스를 이용해 커넥션을 생성한다
	2. con.setAutoCommit(false)로 트랜잭션을 시작시킨다.
3. 생성된 커넥션을 (추후 동기화하여 사용하기 위해) 트랜잭션 도기화 매니저에 보관한다.
4. 실제 서비스를 호출한다.
5. 비즈니스 로직에서 레파지토리를 호출한다. 
	1. `DataSourceUtils.getConnection(dataSource)`을 통해 트랜잭션 동기화된 커넥션을 획득한다.
6. 트랜잭션 커밋/롤백 수행한 뒤 클라이언트에게 반환한다.




> 선언적 vs 프로그래밍 방식 트랜잭션 관리

- 선언적 ~ : 애노테이션 하나만 선언해서 편리하게 트랜잭션을 적용하는 것
	- MemberServiceV3_3 : `@Transactional` 사용
- 프로그래밍 방식 ~ : 트랜잭션 매니저나 트랜잭션 템플릿을 사용해서 트랜잭션 관련 코드를 작성하는 것
	- MemberServiceV3_1 : 트랜잭션 매니저를 이용해서 커넥션 획득, 트랜잭션 관리
	- MemberServiceV3_2 : 트랜잭션 템플릿을 사용해서 대신 관리
	- 테스트 시, 스프링 컨테이너나 스프링 AOP 기술 없이도 테스트할 수 있기 때문에 가끔 사용될 때는 있다. 

---

## 스프링의 리소스 자동 등록 - 데이터소스와 트랜잭션 매니저

스프링 부트가 등장하기 전에는 데이터 소스와 트랜잭션 매니저를 개발자가 직접 스프링 빈으로 등록해서 사용해야 했다.
더 오래전에 스프링을 다루었다면 XML로 이 빈을 등록하고 관리했을 것이다. (본 적 있음.. ㅇㄹㄷㅋ)

스프링 부트가 나오면서 많은 부분이 자동화되었다. 

1. DataSource 
- DataSouce의 스프링 빈 이름 : dataSource   (앞글자는 소문자로 바꾼다.)
- 스프링은 `application.properties`에 있는 속성을 사용하여 데이터소스 생성한 뒤 스프링 빈에 등록한다.

2. TransactionManager
- PlatformTransactionManager -> 빈 이름 : transactionManager
- 어떤 트랜잭션 매니저를 선택할 지는 현재 등록된 라이브러리를 보고 판단한다. 
	- JPA와 JDBC 둘 다 사용한다면 `JpaTransactionManager` 구현체를 등록한다. (JpaTxManager가 JDBC의 트랜잭션 매니저 기능도 대부분 갖고 있다.)


---

지금까지 트랜잭션을 적용하면서 생겼던 문제 3개 중 **트랜잭션 문제**(서비스 계층에 누수, 트랜잭션 동기화 문제, 트랜잭션 로직 중복 문제)를 해결했다.

> NEXT> 2. 예외 누수(at Service Layer)와 3. JDBC 반복 문제(at Repository Layer)를 스프링 기술을 통해 해결하는 것을 정리한다.
