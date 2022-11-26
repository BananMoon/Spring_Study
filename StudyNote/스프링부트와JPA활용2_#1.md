## 1. 하나의 프로젝트에 API 스펙을 위한 컨트롤러와 렌더링된 화면을 전달하는 컨트롤러가 존재하는 경우, 패키지를 구분해서 생성하는 것을 추천.
- 공통 처리를 할 때 일반적으로 패키지 구성 단위로 구분해서 적용해야 한다. 
- 전자의 컨트롤러는 JSON 스펙을 주고받고, 후자의 컨트롤러는 공통 에러 화면을 전달하는 등 미묘한 차이가 존재하기 때문.


## 2. 엔티티를 API 응답으로 사용하면 안된다.
> 왜 그럴까?
- 엔티티가 변경되면 API 스펙이 변한다.
- 기본적으로 엔티티의 모든 값이 노출된다.
- 응답 스펙을 맞추기 위해 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
  - 실무에서는 동일 엔티티에 대해서도 API 용도에 따라 다양하게 만들어지므로 한 엔티티에 각각의 API를 위한 프레젠테이션 응답 로직을 담기 어렵다.
- 배열 형태로 데이터를 반환하는 것은, 추후 응답 스펙 확장에 유연성이 현저히 떨어진다.
  - 응답을 위한 클래스를 따로 생성해주는 것을 추천.


## 3. Object를 JSON으로 변환하거나 JSON을 Object로 변환할 때
- Object -> JSON 변환 시, ObjectMapper 클래스는 Getter 메서드를 이용해서 변환한다.
- JSON -> OBject 변환 시, 기본 생성자(빈 인자)를 사용한다. 
  - 기본 생성자를 생성해놓지 않으면, 아래와 같이 문제 발생

```markdown
(although at least one Creator exists): cannot deserialize from Object value (no delegate- or property-based Creator); nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException:)
```

 [참고](https://stackoverflow.com/questions/51403962/jackson-deserialization-fails-after-serializing-an-object-using-writevalueasstri)

<br>

---

## 4. X to One 의 성능 최적화
xToOne에는 Many To One, One To One이 있다.

Ex) Order에서 Member를 참조하고,
Member에서 리스트 형태로 Order들을 갖고 있게 되면 다시 리스트 내 Order들에 접근한다.

이와 같이 양방향으로 참조하고 있는 엔티티 간에는 무한 참조가 발생한다.
Jackson 라이브러리 입장에서는 무한으로 객체를 JSON으로 변환하려는 문제가 발생한다.

> 이를 해결하는 방법
양방향 연관관계를 맺는 객체들 중 한 곳에 `@JsonIgnore` 애노테이션을 붙여서 더 이상 순환하지 않도록 한다.

<br>

---

## 5. Entity를 API에 노출하면서 프록시 객체에 대한 문제 발생 
4번을 해결하고 나면 다음 응답과 함께 문제가 발생한다.
```
{
    "timestamp": "2022-10-25T11:20:42.754+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/v1/simple-orders"
}
```

찍힌 로그를 확인해보면 다음과 같다.
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.ArrayList[0]->jpabook.jpashop.domain.Order["member"]->jpabook.jpashop.domain.Member$HibernateProxy$O6y294ir["hibernateLazyInitializer"])
```

Order 필드에서 확인할 수 있는 Member 필드는 지연(LAZY)로딩으로 되어있기 때문에
Order를 조회할 때 Member는 실제로 DB에서 조회하지 않는다.
프록시(Proxy) 라이브러리는 Member 필드에 실제 데이터 대신 Member를 상속받는 프록시 Member 객체(ByteBuddyInterceptor 클래스)를 생성해서 넣어 놓는다.

이때 Jackson 라이브러리가 객체를 뽑아서 JSON으로 변환하는 과정에서 Member 필드에 들어있는 `ByteBuddyInterceptor`에 대해 JSON으로 변환하지 못하는 문제가 발생한다.

*1대다(OneToMany)는 기본 전략이 Lazy 전략이다.

만약 프록시 객체인 Member를 실제로 가져오려면 어떻게 해주면 될까?
`Order.getMember()`까지는 실제 Member 데이터가 조회되지 않지만
`Order.getMember().getName()`을 진행하면 Member 데이터를 조회하는 쿼리가 발생해서 Lazy 강제 초기화를 진행한다.

> <b>알아둘 점</b><br>
em.find(~)를 통해 조회하는 것과
Jpa객체의 메서드 ex) findAllByXXX(~) 로 조회하는 것은 다르다.
후자의 경우, JPQL로 실행되서 쿼리문이 모든 객체들에 대해 생성되고, N+1 문제 등 성능이 나빠지게 된다. 
만약 Order를 조회할 때, Order가 갖고 있는 Member도 조회하는 쿼리문을 생성하게 된다.

그렇다고 EAGER로 설정하면 될까? EAGER 전략을 사용하는 것은 객체를 조회할 때 관련 객체를 함께 조회하도록 강제하는 것이고, 이는 결국 좋지 않은 성능으로 이어진다.
일반적으로는 지연로딩을 기본으로 하고, 성능 최적화 시 페치조인(fetch join)을 사용하자.

<br>

---

## 6. Lazy 로딩으로 인한 많은 쿼리 호출 문제 

### 성능 개선 1) 패치 조인(Fecth Join)
Order 조회 시 LAZY를 무시하고 한번에 Member와 Delivery 데이터를 조인해서 가져온다.
JPA에만 있는 문법으로, 엔티티 조회 시 성능 최적화를 위해 사용한다.

- 패치조인과 일반 조인 차이점
  ```
  1. 영속화 대상
  일반 조인은 조인 대상 엔티티는 영속화되지 않는다.
  패치 조인은 select 대상의 엔티티뿐만 아니라 조인의 대상까지 영속화하여 가져온다.

  2. 조인 대상에 대한 쿼리문 발생
  일반 조인은 연관 엔티티를 프록시로 가져오고, 연관 엔티티의 필드까지 조회할 때 추가 쿼리문이 발생한다.
  패치 조인은 연관 엔티티의 필드까지 함께 조인하여 추가 쿼리문을 발생시키지 않는다.
  => 일반 조인은 연관 엔티티가 검색 조건에 포함되고, 조회의 주체가 검색 엔티티뿐일 때 사용하면 좋다.
  ```

- 사용 예제 
  - 코드
  ```java
  public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
      "select o from Order o" +
      " join fetch o.member m" + 
      " join fetch o.delivery d", Order.class)
      .getResultList(); 
    )
  }
  ```
  - 생성된 쿼리문
  ```sql
  select
        order0_.order_id as order_id1_9_0_,
        member1_.member_id as member_i1_6_1_,
        delivery2_.delivery_id as delivery1_4_2_,
        order0_.delivery_id as delivery4_9_0_,
        order0_.member_id as member_i5_9_0_,
        order0_.order_date as order_da2_9_0_,
        order0_.status as status3_9_0_,
        member1_.city as city2_6_1_,
        member1_.street as street3_6_1_,
        member1_.zipcode as zipcode4_6_1_,
        member1_.name as name5_6_1_,
        delivery2_.city as city2_4_2_,
        delivery2_.street as street3_4_2_,
        delivery2_.zipcode as zipcode4_4_2_,
        delivery2_.status as status5_4_2_ 
    from
        orders order0_ 
    inner join
        member member1_ 
            on order0_.member_id=member1_.member_id 
    inner join
        delivery delivery2_ 
            on order0_.delivery_id=delivery2_.delivery_id
  ```
- 패치 조인을 사용해서 쿼리 1번에 루트 엔티티와 연관 엔티티를 조회한다. (지연로딩이 설정되어있어도 지연로딩되지 않는다.)
- 문제 : 위 sql문은 Entity를 그대로 조회하기 때문에 필요없는 필드까지 반환하게 된다. 
이를 DTO로 조회되도록 개선하여 필요한 필드만 반환도록하여 성능 최적화할 수도 있다.

### 성능 개선 2) DTO로 바로 반환하는 레파지토리 (using 패치 조인)
엔티티가 아닌 필요한 필드들로 구성된 DTO만을 조회하므로 DB => 애플리케이션으로의 네트워크 용량이 최적화된다. 

```
성능 개선 많이 이루어질까?
사실 요즘은 네트워크 성능 또한 좋아져서 미비하다.
물론 고객 트래픽이 매우 많이 들어오는 API인데 데이터 크기가 너무 클 때(2-30개)는 고려해야하지만, 그렇지 않거나 어드민 API이면 큰 성능 개선은 볼 수 없다.
```

> 패치 조인(fetch join)은 엔티티 그래프를 한번에 같이 조회하기 위해 사용하는 기능이다.(select 절에 엔티티를 지정해야 한다.) 그래서 엔티티를 조회하지 않으면 패치 조인은 실패한다.

- 사용 예제
  - 코드
  ```java
  public List<SimpleOrderQueryDto> findOrderDtos() {
      return em.createQuery("select new jpabook.jpashop.dto.SimpleOrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
        " from Order o" +
        " join o.member m" +
        " join o.delivery d", SimpleOrderQueryDto.class
      ).getResultList();
  }
  ```
  - 생성된 쿼리문
  ```sql
  select
      order0_.order_id as col_0_0_,
      member1_.name as col_1_0_,
      order0_.order_date as col_2_0_,
      order0_.status as col_3_0_,
      delivery2_.city as col_4_0_,
      delivery2_.street as col_4_1_,
      delivery2_.zipcode as col_4_2_ 
  from
      orders order0_ 
  inner join
      member member1_ 
  on order0_.member_id=member1_.member_id 
  inner join
      delivery delivery2_ 
  on order0_.delivery_id=delivery2_.delivery_id
  ```

대부분 join이나, 혹은 where문에 걸리는 칼럼이 인덱스가 걸리지 않는 경우가 성능에 문제가 된다.
(이를 제대로 판별하기 위해서는 성능 테스트가 필요하다.)

단점도 있다.
- 레파지토리 재사용성이 떨어지는 점
- API 스펙에 맞춰진 코드가 레파지토리에 그대로 반영되는 점 (즉, 화면에 의존성이 있는 스펙이 레파지토리까지 반영된 것이다.)

위 단점을 상쇄하는 방법이 있다.
repository 패키지 아래에, DTO로 반환하는 레파지토리와 DTO를 묶는 하나의 패키지로 따로 생성한다.
이렇게 함으로써 순수 엔티티를 반환하는 용도로 사용하는 기존 Repository와 분리함으로써 유지보수성이 더 좋아진다.
- repository
  - order.simplequery
      - OrderSimpleQueryDto.java
      - OrderSimpleQueryRepository.java
  - 기존 Repository들..    

