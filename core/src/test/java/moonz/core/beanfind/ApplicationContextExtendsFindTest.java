package moonz.core.beanfind;

import moonz.core.discount.DiscountPolicy;
import moonz.core.discount.FixDiscountPolicy;
import moonz.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회 시 자식이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByParentTypeDuplicate() {
//        DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모 타입으로 조회 시 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다")
    void findBeanByParentTypeBeanName() {
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);// 타입 이름 지정
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    // 추천하지 않는 방법
    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    // 실무에서는 print를 하지 않는 것이 좋음. 나중에는 테스트를 자동화로 하는데 그땐 print로 일일히 확인할 수 없으니까.
    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllBeanByParentType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + ", value = " + beansOfType.get(key));
            /* key = rateDiscountPolicy, value = moonz.core.discount.RateDiscountPolicy@abf688e
                key = fixDiscountPolicy, value = moonz.core.discount.FixDiscountPolicy@478ee483
             */
        }
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllBeanByObjectTYpe() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }
   @Configuration
    static class TestConfig {
        @Bean
        public DiscountPolicy rateDiscountPolicy() {    // DiscountPolicy가 아닌 RateDiscountPolicy로 타입을 정해도 되지만 추상화가 더 이상적임
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
}
