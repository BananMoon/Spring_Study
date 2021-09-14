## Lombok
- 롬복이란? 
  - 자바 프로젝트를 진행하는데 거의 필수적으로 필요한 메소드/생성자 등을 자동생성해줌으로써 코드를 절약할 수 있도록 도와주는 라이브러리
- 현재 프로젝트에서는 
  - Course 클래스에 `@Getter` 와 `@NoArgsConstructor` 적용
  - CourseService 클래스에 `@RequiredArgsConstructor` 적용

<br>

## DTO
- 현재 main 메서드에서 Update문을 호출할 때 `id`와 `Course 클래스` 정보를 전달한다. <br>
  But, 이렇게 DB와 맞닿아 있는 클래스를 함부로 사용하게 되면 안전성에 문제가 있다.
- 완충재로 활용하는 것이 DTO(Data Transfer Object)이다.
- Update 혹은 Read 기능 시 필요한 클래스 정보를 (필요할 때 마다) 매번 클래스를 생성해서 이용하는 것이다. (실제 클래스 대신)
> 방법
> 1. `CourseRequestDTO.java` 생성
> ```java
> @NoArgsConstructor
> @Getter
> public class CourseRequestDto {
>     private String title;
>     private String tutor;
> 
>     public CourseRequestDto(String title, String tutor) {
>         this.title = title;
>         this.tutor = tutor;
>     }
> }
> ```
> 2. CourseService&Course 클래스와 main 메서드에서 사용했던 `Course 클래스` 대신 `CourseRequestDTO 클래스`를 사용하도록 변경한다.


#### 참고 - [파일 변경 링크](https://github.com/BananMoon/Spring_Study/pull/1/files)
