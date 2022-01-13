package moonz.core.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})  // 애노테이션 적용 위치
@Retention(RetentionPolicy.RUNTIME) // 어노테이션을 유지하는 정책 설정
@Inherited      // 애너테이션이 MainDidscountPolicy의 자손 클래스에도 상속되도록 한다.
@Documented // javadoc으로 api 문서를 만들 때 해당 애노테이션에 대한 설명도 포함하도록 지정
@Qualifier("mainDiscountPolicy") // 추가
public @interface MainDiscountPolicy {
}
