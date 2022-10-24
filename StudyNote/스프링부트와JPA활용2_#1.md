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


## 4. X to One 의 성능 최적화
xToOne에는 Many To One, One To One이 있다.

Ex) Order에서 Member를 참조하고,
Member에서 리스트 형태로 Order들을 갖고 있게 되면 다시 리스트 내 Order들에 접근한다.

이와 같이 양방향으로 참조하고 있는 엔티티 간에는 무한 참조가 발생한다.

> 이를 해결하는 방법

1. One 쪽에서 참조하는 리스트타입의 필드에 `@JsonIgnore` 애노테이션을 붙여서 더 이상 순환하지 않도록 한다.
