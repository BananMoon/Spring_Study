## 20. 스프링의 전통적인 트랜잭션
**스프링 시작**
1. 톰켓 시작 - 서버 작동
2. web.xml 읽어진다.
3. context.xml이 읽어진다 -> DB 연결 테스트
4. 트랜잭션 환경이 세팅된다. (request-> web.xaml - 필터 - 스프링 컨테이너 - 영속성 컨텍스트 - DB)
 
**트랜잭션 발생**  
1. a가 b에게 송금하는 **request 발생**
2. Presentation 계층에서
  1) DB 연결 세션 생성 (JDBC 커넥션) ➡️ 쿼리문 날릴 수 있다.
  2) 트랜잭션 시작
  3) 영속성 컨텍스트 시작
3. 스프링 컨테이너 내에서 
    1) Controller 계층 : 요청을 분기하여 Service에 요청
    2) Service 계층 : 송금() 호출 -> R
    3) Repository 계층 : Select로 DB에서 데이터를 조회하여 객체화시켜 (영속성 컨텍스트에 입력됨) Service에게 반환한다.
    - 만약 영속성 컨텍스트 내(1차 캐시)에 데이터 정보가 있으면 반환 (DB에서 조회 x)
    4) Service 계층 : 값을 변경 (update)
4. Controller에서
    1) DB연결 세션 종료
    2) 트랜잭션 종료 -> `commit` 발생 : (영속성 컨텍스트에서) 영속화된 객체의 변경을 감지하여 DB에 flush하여 DB의 값을 update한다.
    3) 영속성 컨텍스트 종료
    4) Response로 Data(Json) or html 응답
<hr>

## 20-1. 전통적인 방식의 문제점 😧
### BUT 1 ❗❗
1. request 요청 시에 (Presentation 계층에서) 2-1), 2-2)가 진행되었지만 그 시점에 실행될 필요가 없네?<br>
  **Controller 수행 이후에 2,3번을 실행하자.**
2. Controller 수행 시에 4-1), 4-2), 4-3)이 진행되지 않고 **Service가 종료된 이후에 (Service 계층에서) 실행하자.**

➡️ Controller에서 해야되는 작업이 줄어들어 부하가 줄어든다. ➡️ DB 부하가 줄어든다.
<br><br>

### ➕ 추가. fetch 전략에 따른 전개
> `@ManyToOne`의 기본 fetch 전략 "`fetch=FetchType.EAGER`"<br>
- Player와 Team 테이블이 있고, ManyToOne 관계일 때, '이대호'라는 Player를 select한다고 가정하자.
- DB로부터 '이대호'라는 Player 정보와 해당하는 Team 정보(1건- 롯데 ..)가 함께 영속화된다. (영속성 컨텍스트에!)
- 쉽게 말해, `JOIN`을 통해 Team 데이터를 연결해서 두 객체가 Controller에게까지 전달된다.
<br><br>

> `fetch=FetchType.LAZY` 전략(**지연 로딩**)으로 바꾼다면? 
- '이대호'라는 Player를 select할 때 Team 데이터를 함께 가져오지 않는다.
  - 즉, 나중에 Team 정보가 필요하면 그때 호출할게! 라는 소리.
- 이때는 Team 객체가 아닌 프록시 객체(빈 객체)를 JOIN해서 전달한다.
<hr>

## 20-2. 개선 : 스프링 JPA의 OSIV 전략
### BUT 2. Controller 에서 JOIN되어 연결(ManyToOne)된 객체를 조회하길 원한다! (특정 객체에 대해 트랜잭션 이후)
- '이대호'라는 Player 객체에 대해 트랜잭션이 종료된 후에 Controller에서 프록시 객체인 '롯데' 팀 객체를 *조회*하고 싶다면? (프록시 객체에 대한 Lazy Loading)
    - 영속성 컨텍스트를 삭제하면 객체가 사라지므로 불가능해진다. 그러므로 **Presentation 계층에서 영속성 컨텍스트 삭제를 실행하자.**
    - 위와 같이 개선된 방식은 Controller가 종료될 때까지 영속성 컨텍스트가 종료되지 않는다.

> 진행 흐름
1. 다시 '롯데'인 Team 객체를 호출한다.
2. JDBC 커넥션을 열어서 영속성 컨텍스트 내의 1차 캐시에 있는 롯데 Team 객체를 조회하는 select를 실행한다.
3. 프록시(빈) 객체가 롯데 Team 객체로 대체되어 User Interface(Controller) 계층까지 객체가 전달된다.

- 트랜잭션은 Service 계층에서 종료되기 때문에 Insert, Delete, Update는 수행하지 못한다. But, 트랜잭션이 종료된 이후에도 Controller의 세션이 close되지 않았기 때문에 영속 객체는 Persistence 상태를 유지할 수 있어 **프록시 객체에 대한 Lazy Loading을 수행할 수 있다.(SELECT)**

*Persistence(영속성) : 데이터를 생성한 프로그램의 실행이 종료되더라도 사라지지 않는 데이터의 특성

- Spring Boot의 JPA에서 `open-in-view`를 True로 설정해주면 LAZY 로딩 전략으로 설정되어 위의 흐름을 진행할 수 있다.
<br>

> **RESULT**
- Spring은 위의 **But1,2**에 나타난 것처럼 로직을 업데이트해왔고 Spring Boot의 2.0 버전부터는 Open-in-view의 디폴트를 True로 설정하여 위의 로직을 실행할 수 있게 된다.
  - False로 설정할 경우, 영속성 컨텍스트(Persistence Context)가 Service 계층에서 종료되어 **Controller에서 Lazy Load를 할 수 없게 된다.**


## 🔖 정리. 스프링부트의 트랜잭션
- 세션의 시작은 서블릿이 시작되는 시점부터! (세션은 영속성 컨텍스트를 포함)
  - Servlet이란, 자바를 사용하여 웹페이지를 동적으로 생성하는 서버측 프로그램
  - 즉, request 요청 시에 세션이 시작된다고 생각하면 된다.
- 트랜잭션의 시작은 Serive 계층부터! (JDBC 커넥션도 이 시점부터)
- 트랜잭션의 종료는 Service 계층에서 종료! (JDBC 커넥션도 이 시점에 종료)
- 세션은 Controller 영역까지 열려있기 때문에 영속성이 보장되며, SELECT가 가능해지고, lazy-loading이 가능해진다.

![image](https://user-images.githubusercontent.com/66311276/141430895-3ec83a54-f62c-4100-9219-712eac70074b.png)
