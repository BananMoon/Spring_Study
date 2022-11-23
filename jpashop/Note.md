# 목차
- Spring MVC
- JPA

---

## Spring MVC
### @ModelAttribute
- 기본적으로 controller 단에서 argument에 어노테이션이 생략되어있을 경우,
  - String, int, Integer 같은 **단순 타입**은 `@RequestParam`을 붙인 것처럼 동작하고 
  - 나머지(예를 들면 클래스 객체)는 `@ModelAttribute`를 붙인 것처럼 동작합니다.

### Form과 Entity의 구분
- (복잡하면 복잡할수록) 기존에 생성한 Entity와 웹에서 전달되는 데이터가 완벽히 매칭될 수 없는 경우가 대부분이다.
  - 즉, 순수 Entity의 필드들에, 화면에 필요한 데이터들이 추가로 존재한다. 
- 그렇다고 필요한 데이터들을 Entity에 모두 구현하게 되면, Entity는 웹에 종속되는 문제가 발생한다.
- Entity는 순수(핵심) 비즈니스 로직만을 포함하는 객체로 두어야하고, 따로 화면에 필요한 데이터를 전달할 객체를 사용해야한다. 
- 그렇기 때문에 Form 객체를 따로 생성하는 것을 권고한다.

`Form과 DTO의 구분`은 Form보다 넓은 범위가 DTO이다.
  - Form은 웹에서 컨트롤러에 전달될 때 사용되는 것으로 한정된 의미의 객체이고, DTO는 모든 Layer에서 객체를 전달하는 용도로 사용될 수 있다.
  - 네이밍 차이에 따라 다른 곳에서 쓰이는 것이지, 역할은 동일하다. 
> Setter를 열어줘도 되는가?<br>
> Form은 View단과 연결되어 쓰이기 때문에 getter, setter를 이용해서 접근하는 경우가 많다. 그리고 Entity 자체가 아니기 때문에 Setter를 열어줘도 괜찮다. 
 
---

## JPA
#### 모든 연관관계는 지연 로딩으로 설정하자! 💛
- 즉시로딩(`EAGER`)은 예측이 어렵고 어떤 SQL이 실행될지 추적하기 어렵다. 특히 `JPQL`을 실행할 때 `N+1` 문제가 자주 발생한다.
- 연관된 엔티티를 함께 DB에서 조회해야 하면, 즉시로딩(`EAGER`)로 설정하기 보다 **fetch join** 또는 **엔티티 그래프 기능**을 사용하자.
- `@ManyToOne`, `@OneToOne`은 즉시로딩이 default.
---

#### Entity Manager Factory와 EntityManager 💓
- EntityManagerFactory는 EntityManager를 만드는 역할을 한다.
- EntityManagerFactory를 만드는 비용은 상당히 크다.
- 여러 스레드가 동시에 접근해도 안전하다.
- EntityManager는 엔티티를 수정/삭제/조회 등 일을 처리하는데, 말 그대로 엔티티 관리자 이다.
- EntityManager를 생성하는 비용은 거의 들지 않지만, 스레드가 동시 접근하면 동시성 문제가 발생하여 스레드 간 절대 공유해서는 안된다.


- EntityManager는 트랜잭션이 시작할 때와 같이 db 연결이 꼭 필요한 시점에 데이터베이스 커넥션을 획득한다.
- EntityManagerFactory를 생성하는 시점에 데이터베이스 커넥션풀도 생성한다.
---

#### 영속성 컨텍스트 (Persistence Context) 🤍
- 엔티티를 '영구 저장'하는 환경이라는 뜻으로, 엔티티 매니저로 엔티티를 저장하거나 조회 시, 영속성 컨텍스트에 엔티티를 보관하고 관리한다.
- 아래 코드는 엔티티 매니저가 엔티티를 영속성 컨텍스트에 저장하는 코드이다.
`EntityManager객체.persist(엔티티)`
- 여러 엔티티 매니저가 같은 영속성 컨텍스트에 접근할 수도, 하나의 엔티티 매니저에 하나의 영속성 먼텍스트가 생성될 수도 있다.

- 특징
1. 영속성 컨텍스트는 엔티티를 식별자값으로 구분하기 때문에 식별자 값이 반드시 있어야 한다. (안그러면 예외 발생)
2. 영속성 컨텍스트에 새로 저장된 엔티티는 트랜잭션을 커밋하는 순간 DB에 flush된다.
- 장점
1. 1차 캐시
- 영속성 컨텍스트 내부에 (식별자,엔티티 인스턴스) 형태로 Map 자료구조로 존재하는 캐시 공간
- 1차 캐시에 존재하지 않으면, DB에서 가져와서 1차 캐시에 저장한 후 반환한다.
2. 동일성 보장
- 조회 시 1차 캐시에 저장된 엔티티들을 조회하기 때문에 같은 엔티티가 조회되므로 동일성이 보장된다.
3. 트랜잭션을 지원하는 쓰기 지연(Lazy)
4. 변경 감지
- 엔티티가 처음 1차 캐시에 저장될 때 JPA는 최초 상태를 복사해서 저장해둔다. (스냅샷)
- (플러시 시점) 트랜잭션이 커밋될 때마다 그 스냅샷과 엔티티를 비교해서 변경된 엔티티를 찾는다.  (영속 상태의 엔티티에만 적용)
- 있다면 update sql문을 생성해서 **쓰기 지연 SQL 저장소**에 보낸다. 이 저장소의 SQL을 DB에 보낸다.
- DB 트랜잭션을 커밋(플러시)한다. 이때 모든 필드가 다시 update된다. (BUT, 일부 필드만 업데이트되는 게 더 장점인 상황이라면 전략을 수정하면 된다.)
- 일부 필드만 변경되기 원하면 `@org.hibernate.annotations.DynamicUpdate`
5. 지연 로딩
---

#### 플러시(flush()) 💜
- 영속성 컨텍스트의 변경 내용을 DB에 반영한다.
- 영속성 컨텍스트를 플러시하는 방법 3가지
1. 직접 호출 : `em.flush()`
- 거의 사용 X
2. 트랜잭션 커밋 시 플러시가 자동 호출
-  플러시를 통해 영속성 컨텍스트의 변경 내용을 SQL로 전달하고 트랜잭션을 커밋해야 데이터베이스에 반영이 된다.
-  그래서 트랜잭션 커밋 시 자동으로 플러시를 호출한다.
3. JPQL 쿼리 실행 시 플러시가 자동 호출
- 만약 memeberA, memeberB, memeberC를 영속성 컨텍스트에 저장(`em.persist(멤버)`)했는데, 플러시 없이 바로 JPQL문을 날리면 어떻게 될까?
```java
em.creqteQuery('select m from Member m', Member.class);
```
- 세 엔티티에 대한 정보는 아직 DB에 반영되지 않았으므로 조회되지 않을 것이다.
- 그래서 JPQL 실행 시 플러시가 자동으로 호출된다.
- 단, 식별자를 기준으로 조회하는 `find()` 메서드 실행 시에는 플러시가 호출되지 않는다.    **영속성 컨텍스트에서 조회해서 그런가?**
---

#### 준영속 상태(detached)의 엔티티 🧡
-영속성 컨텍스트가 관리하던 엔티티가 영속성 컨텍스트에서 분리되는 것을 준영속 상태라고 한다.
- 3가지 방법으로 준영속 상태로 만들 수 있다.
1. 특정 엔티티만 준영속 상태로 전환 : `em.detach(엔티티)`  -> 1차 캐시와 SQL 쓰기 저장소에 있는 정보 모두 삭제
2. 영속성 컨텍스트를 완전히 초기화 : `em.clear()`
3. 영속성 컨텍스트를 종료 : `em.close()`
- 특징
1. 영속성 컨텍스트가 제공하는 기능을 하나도 지원받지 못한다. (1차 캐시, 쓰기 지연, 변경 감지, 지연 로딩)
2. (비영속 상태와 달리) 식별자 값은 가지고 있다. (이미 한번 영속 상태였으므로)
3. 지연 로딩은 실제 객체 대신 프록시 객체를 로딩해두고 해당 객체를 실제로 사용할 때 영속성 컨텍스트를 통해 데이터를 불러오는 방법인데,
준 영속 상태는 영속성 컨텍스트가 더이상 관리하지 않으므로 지연 로딩 시 문제가 발생한다.
---

#### 어노테이션을 이용한 객체와 테이블 매핑 💜
- JPA는 엔티티 객체를 생성할 때 기본 생성자를 사용하기 때문에, 만약 생성자를 하나 이상 만드는 경우 기본 생성자를 직접 만들어 놔야한다. (`public` 또는 `protected`)
- **DB 스키마 자동 생성** 속성은 운영환경보다, 테스트 환경이나 엔티티와 테이블을 어떻게 매핑해야하는지 학습하는 용도로만 사용하자. (훌륭한 학습 도구!)
  - create, create-drop, update, validate, none 
- 유니크 제약조건을 걸어주는 `uniqueConstraints` 속성
  - DDL 생성 시 유니크 제약조건이 추가된다.
```java
@Table(name="MEMBER", uniqueConstraints = {@UniqueConstraint(
  name = "NAME_AGE_UNIQUE",
  columnNames = {"NAME", "AGE"} )})
```
생성된 DDL
```sql
ALTER TABLE MEMBER ADD CONSTRAINT NAME_AGE_UNIQUE UNIQUE (NAME, AGE)
```
---

#### 기본키 직접 할당 전략 💙
`@Id` 로 매핑하여 기본 키를 직접 할당할 수 있다.
적용 가능한 타입은 아래와 같다.
- 자바 기본형
- 자바 Wrapper 형
- String
- java.util.Date
- java.sql.Date
- java.math.BigDecimal
- java.math.BigInteger
---

### IDENTITY 전략과 SEQUENCE 전략 🤎
```
IDENTITY 전략은 데이터를 DB에 INSERT한 후 기본 키 값을 조회할 수 있기 때문에, 추가로 DB를 조회해서 엔티티에 식별자 값을 할당할 수 있었다. 
하지만 JDBC3에 추가된 `Statement.getGeneratedKeys()`를 사용하면 데이터 저장과 동시에 생성된 기본키 값도 얻어올 수 있어 한번만 통신할 수 있게 된다.
```
엔티티가 영속 상태가 되려면 식별자가 반드시 필요하므로 em.persist()`를 호출하는 즉시 INSERT SQL이 db에 전달되어 식별자를 얻는다. 즉, 쓰기 지연이 동작하지 않는 전략이다.

---
### 변경감지와 병합(merge) 💚
> JPA에서 권장하는 best practice는 **변경감지**이다.
1. 변경감지를 통한 JPA의 DB 수정
```java
@Autowired
EntityManager em;

Book book = em.find(Book.class, 1L);
book.setName("이름 바꿔치기");
// 트랜잭션 커밋 시점에 변경 감지(dirty checking)가 발생하여 db에 update 쿼리를 날리고 엔티티를 변경한다.
```

> 준영속 엔티티일 때는 어떻게 될까?
- 엔티티 컨텍스트가 더이상 관리하지 않는 엔티티
- 준영속?<br>
  update 메서드에서 Book이라는 새로운 객체를 생성하지만 id, name, 등을 세팅한다. 이때 식별자(id)를 세팅한다는 것은 이미 db에 한번 저장이 되었던 엔티티라는 의미.
- 결국 새 엔티티를 생성한 것이기 때문에 setXXX만 한다고 해서 JPA가 변경을 감지하지 않는다.

> 준영속 엔티티를 수정하는 방법 2가지
1. 변경 감지(dirty checking) 기능 사용
- `find()` persist로 영속성 상태의 엔티티를 찾아서 setXXX하고 transaction이 커밋하는 경우, JPA는 변경을 감지하여 flush를 날린다.
- flush를 날리면, 바뀐 엔티티를 조회하여 db에 업데이트를 보내는 것.
2. 병합(merge) 사용
- 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능
- 영속 엔티티의 값을 준영속 엔티티의 값으로 **모두** 교체한다.
  - 병합은 모든 속성이 변경되기 때문에, 병합 시 값이 없으면 `null`로 업데이트 된다.   -> 사용하지 말자
```java
@Transactional
void update(Item param) {
    Item mergeItem = em.merge(param);
}
```


## 중요 포인트 짚고가자 ‼
### 1. 엔티티를 수정하는 작업은 엔티티 클래스 내에 하나의 메서드를 생성해 진행한다.
> 예제
> ```java
> class Entity {
> private int stockCnt;
> 
>    public void addStockCnt() {
>      stockCnt++;
>    }
> }
> ```

### 2. 서비스 계층 역할에 따른 패턴: 도메일 모델 패턴 vs 트랜잭션 스크립트 패턴
> 도메인 모델(Domain Model) 패턴 
- 도메인이 행동(behavior)과 데이터(data)를 통합하여 갖고 있다. (http://martinfowler.com/eaaCatalog/domainModel.html)
- 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할만 한다. 
- 엔티티가 비즈니스 로직을 가진다.
- 객체 지향의 특성을 적극 활용하는 방식
> 트랜잭션 스크립트(Transaction Script) 패턴 (http://martinfowler.com/eaaCatalog/transactionScript.html)
- 프렌제테이션으로부터 오는 단일 요청을 다루는 각각의 프로시저를 기준으로 비즈니스 로직을 조직화한다.
  - 각 트랜잭션이 자신의 스크립트를 갖고 있고, 하위 작업이 있을 경우, 하위 프로시저로 나눌 수도 있다.
- 도메인 모델 패턴과 반대로, 엔티티에는 비즈니스 로직이 거의 없다.
- 서비스 계층에서 대부분의 비즈니스 로직을 처리한다.

### 3. 동적쿼리 생성 방법 3가지
1. JPQL : String으로 동적쿼리 생성하는 방법. 컴파일 체크가 되지 않아 버그가 발생하기 쉽다.
2. JPA Criteria : JPA 표준스펙. Java코드로 JPQL을 생성하도록 만들어주는 클래스 모음. 유지보수성이 현저히 떨어져 실무에서 사용하지 않는다.
- 딱 Criteria를 이용한 코드를 봤을 때 JPQL이 어떻게 만들어지는지 파악하기 어렵다.
3. QueryDSL(💫권고) : 동적 뿐만아니라 정적쿼리도 생성해줄 수 있음. 실무에서 접하는 복잡한 쿼리 또한 가능하다.

### 4. 실무에서는 JPA만으로 해결되지 않고, JPA + JPQL을 직접 이용해서 쿼리를 짜야하는 경우가 많다.
영한님: 실무에서는 Spring + JPA + Spring JPA + QueryDSL 묶음 기본으로 사용
또한 Spring Data Jpa에도 제약이 있기 때문에, 한계를 알고 사용해야 한다.
실무에서는 사용하면 매우 단순해지는 좋은 기능이어도 위 같은 문제로 사용하지 못하는 경우가 있다.

### 5. 관계형 데이터베이스에서 1:N 관계의 테이블끼리 join하면 N인 테이블의 데이터 갯수에 맞춰서 데이터가 조회된다.
주문(Order)을 한 번 할 때 두개의 아이템(OrderItem)을 구매했다. 두 테이블을 조인해서 조회하면?
order_id=2인 Order는 1개의 데이터이지만 OrderItem은 2개(order_item_id=2,3)이기 때문에 order_id=2인 Order 데이터 1개가 2개로 중복 조회된다.
결과 예시)
```
order_id  order_item_id
   2            2
   2            3
```

### 6. 일대다 관계에서 컬렉션 패치 조인은 1개만 사용할 수 있다. 즉, 1:N:M 관계로 2개 이상의 패치조인이 불가능하다.
데이터가 중복되기도 하지만, 정합성이 안 맞는 문제가 발생한다.

### 7.옵션 `hibernate.default_batch_fetch_size`까지 설정하면, 일대다 관계로 인한 지연 로딩 조회 시 지정한 사이즈만큼 pk 기준 in 쿼리를 날려서조회할 수 있다.

만약 데이터가 1000개 있고, `default_batch_fetch_size=100`으로 설정했다면 총 10번의 in절 쿼리로 조회할 수 있다. (yml 설정 파일에 옵션 설정한다.)
현재 예제에서는 데이터 갯수가 모두 100개 이하이기 때문에 일대다 관계인 객체 조회 시 1:1:1로 쿼리가 전송된 것을 확인할 수 있다.
결국 일대다 관계에서 1:N:M -> 1:1:1로 성능을 최적화한 것이다.
```sql
-- order_item 조회 시 pk(order_id)로 in 쿼리 조회 
select orderitems0_.order_id as order_id5_5_1_, orderitems0_.order_item_id as order_it1_5_1_, orderitems0_.order_item_id as order_it1_5_0_, orderitems0_.count as count2_5_0_, orderitems0_.item_id as item_id4_5_0_, orderitems0_.order_id as order_id5_5_0_, orderitems0_.order_price as order_pr3_5_0_ from order_item orderitems0_ where orderitems0_.order_id in (4, 11);
-- item 조회 시 pk(item_id)로 in 쿼리 조회 
select item0_.item_id as item_id2_3_0_, item0_.name as name3_3_0_, item0_.price as price4_3_0_, item0_.stock_quantity as stock_qu5_3_0_, item0_.artist as artist6_3_0_, item0_.etc as etc7_3_0_, item0_.author as author8_3_0_, item0_.isbn as isbn9_3_0_, item0_.actor as actor10_3_0_, item0_.director as directo11_3_0_, item0_.dtype as dtype1_3_0_ from item item0_ where item0_.item_id in (2, 3, 9, 10);
```

추가) 디테일하게 배치 사이즈 설정하려면, `@BatchSize` 애노테이션 사용하자.
```java
    @BatchSize(size = 1000)
    @OneToMany(mappedBy="order", cascade= CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
```   

> `default_batch_fetch_size` 설정 시 주의할 점!!!<br>
적당한 사이즈로 지정해야 하는데, 100~1000 사이를 권장한다.<br>
어떤 db는 in절 파라미터 수를 최대 1000까지 제한하는 경우도 있기 때문이다.<br>
> - size가 클수록, 쿼리 수는 줄지만 한번에 1000개를 db에서 애플리케이션으로 전달하므로 db에 순간 부하가 문제될 수 있고<br>
> - size가 작을수록, 쿼리 수가 많아진다는 문제가 있다.<br>
> 
> 어떻게 하든 전체 데이터를 모두 가져와야 하므로 메모리의 사용량은 동일하다. 결국 db나 애플리케이션이 부하를 어느정도까지 견딜 수 있냐에 따라 조정해야 한다.

---

### 8. API 개발 고급 - 버전 V1~V5에 대한 설명 및 권장 순서
### (1) 각 버전 V1~V5에 대한 설명
### 1. 엔티티 조회
> V1은 엔티티로 반환, V2~3.1은 엔티티로 조회 후 애플리케이션에서 DTO로 반환

**V1: 엔티티 조회해서 그대로 반환**<br>
**V2: 엔티티 조회해서 DTO로 변환 및 반환**
- 루트 객체 조회 시 Lazy로딩으로 설정된 연관관계에 있는 엔티티들은 null값으로 채워지고, 연관 엔티티가 쓰일 때 쿼리를 추가로 날려서 조회한다. 이는 패치 조인을 사용해 처음부터 연관객체들도 같이 조회되도록 할 수 있다.<br>

**V3: 패치 조인을 사용해서 쿼리 수 최적화**
- Lazy로딩 설정된 연관 관계 객체들도 처음부터 함께 조회되도록 하여 쿼리 수를 대폭 줄였다. 
- toMany 관계인 연관관계들 조회 시 컬렉션으로 조회되는데, 이때 'Many' 엔티티를 기준으로 데이터가 조회되어 중복 데이터가 생긴다. <br>
   즉, 다(N)인 객체를 기준으로 데이터를 읽어서 메모리에서 페이징을 시도하기 때문에 제대로 된 페이징이 불가능해진다.

**V3.1: 컬렉션 페이징 불가 문제 해결**
- toOne과 toMany 관계인 객체를 따로 조회한다. 
- toOne 관계: (기존 방식처럼) 패치 조인으로 쿼리 수 최적화
- toMany 관계: (컬렉션 + 페이징을 위해) 지연로딩 유지한 상태에서 배치 사이즈를 지정하여 지연 로딩 성능을 최적화한다. (IN 쿼리를 통해 컬렉션이나 프록시 객체를 설정한 갯수만큼 조회할 수 있다.)
  - `hiberrnate.default_batch_fetch_size` : 글로벌 설정
  - `@BatchSize` : 개별 최적화

### 2. DTO 직접 조회
**V4: JPA로부터 DTO로 조회**
- 단건 조회에서 많이 사용하는 방식 
  - 조회한 Order 데이터가 1000건이라면, V4는 1+1000개 쿼리가 발생하므로 이때는 V5를 사용하면 엄청난 성능 개선이 이루어진다.
1. toOne 관계인 객체들을 한번에 조회하는 쿼리문을 먼저 따로 수행 
2. 애플리케이션 상에서 조회된 루트 객체들을 순회하면서 id를 인자로 주어 1:N 관계의 객체들(컬렉션)을 추가 조회 
  - 결국 N만큼 쿼리문 수행하므로 N+1 문제 발생

**V5: 컬렉션 조회 - 1+1 쿼리 최적화**
- 데이터를 한꺼번에 처리할 때 많이 사용하는 방식
1. toOne 관계인 객체를 DTO로 먼저 조회하고, toMany 관계 객체도 DTO로 가져오는데 식별자 id 기준 in절 쿼리로 1번에 가져와서 Map<id, 같은 id인 DTO 리스트)으로 반환 
2. 루트 객체 기준 순회하면서 Map의 value를 루트의 필드에 세팅한다.

**V6: 플랫 데이터 최적화**
1. 모두 join으로 조회하여 루트 객체의 필드와 1:N 객체의 필드를 갖는 하나의 DTO로 반환하도록 한다. 
  - 중복 데이터 존재 & 페이징 불가
  - 한방 쿼리 가능
2.애플리케이션 상에서 원하는 DTO로 직접 변환 및 반환 
  - toOne 객체의 DTO를 기준으로 Map<toOne DTO, toManyDTO 리스트> 로 매핑한다.  *이때 toOne DTO의 equals는 객체가 아닌 orderId로 지정한다.*
  - Map 기반으로 기존 반환 방식인 DTO로 만든다. (Map의 value가 반환 DTO의 리스트 타입 필드로 들어간다.)


### (2) 권장 순서
1. 엔티티 조회 방식  (강추🌟)
  1. 패치 조인으로 쿼리 수 최적화
  2. 컬렉션(toMany 관계) 최적화
     1. 페이징이 필요하다면, `hibernate.default_batch_fetch_size`, `@BatchSize`로 최적화
     2. 페이징이 필요 없다면, 패치 조인 사용 가능
2. (엔티티 조회 방식으로 도무지 해결 안되면) DTO 조회 방식
3. (DTO도 안되면) `NativeSQL` or `스프링 JdbcTemplate`

---
### 9. OSIV (Open-Session-In-View)
JPA에서의 `EntityManager`가 Hibernate에서의 `Session`이라고 생각하면 된다.

> OSIV가 `true`인 경우?<br>
영속성 컨텍스트와 db connection이
API: 사용자에게 응답 완료할 때까지 / view: html 랜더링해서 사용자에게 응답하기까지 살아있다.<br>
필요성 및 장점: 쿼리 이후에도, 비즈니스 로직이나 컨트롤러 단에서 지연 로딩 및 db connection이나 컨텍스트 연속성이 필요한 작업을 할 수 있다.<br>
단점: 너무 오랜 시간동안 db connection 리소스를 사용하기 때문에, 실시간 트래픽이 중요한 애플리케이션에서는 db connection이 모자랄 수 있다.

- 즉, 현재까지 지연 로딩은 연속성 컨텍스트가 살아 있기 때문에 가능한 것이었다. 그리고 연속성 컨텍스트는 db connection을 유지한다.

> OSIV가 `false`인 경우?<br>
장점: db connection을 짧은 시간동안 유지하므로 커넥션 리소스를 낭비하지 않을 수 있다.<br>
단점: 트랜잭션 밖에서 지연 로딩을 할 수 없기 때문에, 미리 지연로딩을 초기화해놓거나 패치 조인을 이용해야 한다.

- 언제 OSIV를 false로 할까?<br>
모든 상황에서 꺼야하는 것은 아니다. 켜놓아야 중복 코드를 제거하는 등 더 이점이 있지만, 더 복잡한 애플리케이션을 개발하고, db connection을 실시간으로 많이 사용한다면, 꺼놓을 필요가 있다.


- OSIV를 껐다면 복잡해지는 것을 어떻게 관리할까?<br>
Command와 Query를 분리한다.<br>
비즈니스 로직은 등록 및 수정하는 것으로 크게 성능 문제가 되지 않지만, 복잡한 화면을 출력하는 쿼리는 화면에 맞춰 성능을 최적화하는 것이 중요하다.<br>
하지만 그 복잡성에 비해 핵심 비즈니스에 큰 영향을 주지는 않는다.<br>
그래서 이 둘을 명확하게 분리하는 것이 유지보수 관점에서 충분히 의미가 있다. 
- 예를 들면, OrderService를 아래와 같이 구분한다.
  - OrderService : 핵심 비즈니스 로직
  - OrderQueryService : 화면 / API에 맞춘 서비스 (주로 읽기 전용 트랜잭션 사용) 
    - 프로젝트에서 도메인으로 패키지를 구분했다면, 도메인 내에 별개의 패키지를 생성하여 관리하는 것을 추천한다. 