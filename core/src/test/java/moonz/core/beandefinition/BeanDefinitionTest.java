package moonz.core.beandefinition;

import moonz.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class BeanDefinitionTest {
//    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    //    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class); 로 하면 ApplicationContext는 getBeanDefinition()이 포함되어있지 않음.
    GenericXmlApplicationContext ac = new GenericXmlApplicationContext("AppConfig.xml");    // log에서 더 구체적으로 메서드명을 보여줌
    @Test
    @DisplayName("빈 설정 메타정보 (BeanDefinition) 확인")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName +
                        " beanDefinition = " + beanDefinition);
            }
        }
    }
}
