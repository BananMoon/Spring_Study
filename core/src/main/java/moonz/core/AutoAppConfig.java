package moonz.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "moonz.core",    // 만약 moonz.core.member로 하면 member 아래부터 탐색됨
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // 수동으로 등록하는 AppConfig 파일을 제외 !
)
public class AutoAppConfig {
}
