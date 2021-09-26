### Sparta에서 2주차까지 수업을 듣고나니 여태까지 배운 것을 스스로 해보는 숙제가 주어졌다. 
### 숙제를 진행하면서 기억해놓고 싶은 것들:sparkles:을 적어보았다. 
<hr>

### 0. 프로젝트 Setting
#### :fire: Spring boot 프로젝트 Setting을 진행한다. 
> <참고><br>
> [SpringBoot페이지](https://start.spring.io/)에서 아래와 같이 프로젝트 셋팅을 진행하였다. 다운로드받은 후, 알집을 풀어준다. <br>
> 그후, IDE(본인은 IntelliJ)에서 새 프로젝트 생성 버튼을 클릭하여, (Gradle Project인 경우) build.gradle 파일 선택 후 open하면 프로젝트가 잘 셋팅된다.
<p align="center">
  <img src="https://user-images.githubusercontent.com/66311276/134805672-bb27173e-8e6e-42a7-bea8-de5616b9e12a.png" alt="text" width="700" />
</p>
<br>

### 1. 디렉토리 구조
#### :fire: 필요한 폴더는 총 `Domain`, `Service`, `Controller` 이다.<br>
📁 **Domain**<br>
       └ `Person 클래스`<br>
       └ `Person 레포지토리`<br>
       └ `Timestamped 클래스`<br>
       └ `PersonRequestDto 클래스`<br>
       
📁 **Service**<br>
       └ `PersonService 클래스`<br>
       
📁 **Controller**<br>
       └ `PersonController 클래스`<br>
<br>

### 2. Person 클래스 (`@Entity`)
#### :fire: Person 클래스에는 3개의 멤버변수를 두었다.
(1) 사람의 이름, 주소, 직업을 멤버변수로 두었고, 각자 `name`, `address`, `job` 으로 하였다.<br>
(2) 멤버변수 설정
- id는 자동으로 생성되도록 annotation하였고 나머지 3개는 not null로 설정하였다.
- 기본생성자는 `annotation`으로 생성하였고, 매개변수있는 생성자도 추가하였다.
- `getter` 또한 `annotation` 처리
<br>

### 3. PersonRepository 클래스
#### 데이터를 관리하는 클래스로, JPARepository 인터페이스를 상속받는다.
- JPARepository 인터페이스를 상속하여 `findAll()`, `findById()`, `save()`, `delete()` 등등 을 사용할 수 있다.
<br>

### 4. Timestamped 클래스
#### :fire: 데이터를 저장, 수정 시 자동으로 날짜 및 시간이 저장되도록 한다.
- 추상클래스이기 때문에 상속받아서 사용할 수 있다. 
- 데이터타입이 `LocalDateTime`인 **createdAt**과 **modifiedAt** 멤버변수 생성
- main 함수를 갖고 있는 클래스(`FirstprojectApplication`)에 `@EnableJPAAuditing`을 추가하여 JPA를 활성화한다. (그래야  데이터 추가, 수정 시 반영됨)
- JPA Entity 클래스들(Person)이 해당 추상 클래스를 상속(extends)할 경우 **createdAt**과 **modifiedAt**를 컬럼으로 인식하도록 `@MappedSuperclass` 추가
- Person 클래스에서 상속! (`extends Timestamped`)
<br>

### 5.FirstprojectApplication 클래스 (`main` 메서드 포함)
#### :fire: 테스트를 위해 `@Bean` 사용해서 commandLine으로 확인해본다.
- PersonRepository 이용해서 *데이터 추가, 전체 조회, id로 조회, 데이터 삭제* 진행
- *데이터 수정*은 `Service`의 역할! 
  - But:heavy_exclamation_mark: 데이터를 업데이트하기 위해 **DB와 맞닿아 있는 Person 클래스로 생성하는 것이 과연 안전할까?**
    - 완충재로 활용하는 것이 **DTO**(Data Transfer Object) -> `PersonRequestDto 클래스` 생성
- 서버를 run하면 **h2-console**에서 저장된 데이터를 확인할 수 있다.
- 터미널의 commandLine으로도 확인 가능하다.
<br>

### 6. PersonRequestDto 클래스
#### :fire:데이터를 수정하기 전, Person 객체를 임시 생성하는 역할
- 똑같이 멤버변수 3개 생성, 기본생성자 annotation 생성, 매개변수있는 생성자 생성, getter annotation 생성
<br>

### 7. PersonService
#### :fire: 데이터 수정 시에는 PersonRepository가 아닌 Service에서 진행한다.
- update 메서드는 update될 `객체 id`와 반영될 `Person 정보`가 인자로 필요하다.
- 위에서 언급했듯이, 반영할 객체를 위해 Person 클래스로 접근해서 생성하는 것은 안전하지 않으므로, `PersonRequestDto 클래스` 사용한다.
  - `Repository`로 해당 id의 Person 객체 정보를 조회하여
  - `RequestDto`로 해당 Person 객체를 Update 시킨다.  (이때는 Person 클래스에 접근해야함. 이를 위해 Person 클래스에도 update 메서드를 정의해준다.)
<br>

### 8. PersonController
#### :fire: CommandLine으로 확인해봤으니, 이제 Rest API 요청에 대한 응답 역할을 할 `Controller`를 생성한다.
- 5번에서 작성한 코드를 각 REST API (데이터 조회 `GET`, 추가 `POST`, 수정 `PUT`, 삭제 `DELETE`) 에 이용할 수 있다. 
- 각 메서드마다 `@XXXMapping("url")` annotation을 붙여주면, api 호출 시 자동으로 메서드가 매핑되어 실행한다.  <br> EX) `@PutMapping("/api/persons/{id}")`
- `@RequestBody` : POST 요청 시 body에 담아 전송된 데이터를 인자에 담아주기 위한 annotation
- `@PathVariable` : PUT, DELETE 요청 시 url에 담아 전송된 데이터를 인자에 담아주기 위한 annotation<br>
:point_right: **Postman**을 이용하여 API 호출 테스트 완료!
<br>

> 전체코드는 [여기](https://github.com/BananMoon/Spring_Study/pull/3/files)를 참조하세요 🥰
