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