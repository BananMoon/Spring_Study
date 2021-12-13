package moonz.core.beanfind;

import moonz.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);   // 타입 지정 안했으니 Object임
            System.out.println("name = " + beanDefinitionName + ", object = "+ bean);    // soutv 찍으면 변수명 찍어줌
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // 스프링에 등록된 모든 빈 이름을 조회
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);  // bean의 메타데이터 정보를 꺼내는 메서드

//            ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈
//            ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {  // spring이 내부를 위해 등록한 bean이 아니라 애플리케이션을 위해 혹은 외부 라이브러리를 위해 등록
                Object bean = ac.getBean(beanDefinitionName);   // 빈 이름으로 빈 객체 조회. 타입 지정 안했으니 Object임
                System.out.println("name = " + beanDefinitionName + ", object = "+ bean);    // soutv 찍으면 변수명 찍어줌
            }
        }
    }
}
