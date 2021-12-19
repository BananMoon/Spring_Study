package moonz.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)   // 해당 에너테이션이 어떤 타입에 붙을지 -> class에 붙는다.
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {  // 컴포넌트 스캔에서 포함시킬 대상에 붙임.
}
