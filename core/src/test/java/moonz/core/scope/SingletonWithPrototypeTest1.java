package moonz.core.scope;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {
    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUserPrototypeTest() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);

        // ClientBean은 prototypeBean 생성 시점에 의존 주입이 되기 때문에
        // 그 후로 getBean을 할 때마다 처음에 생성된 그 prototypeBean을 가져와서 clientBean2를 생성하는 것이다.
        // 그래서 count==2
    }
    @Scope("singleton")
    @RequiredArgsConstructor
    static class ClientBean {
//        private final PrototypeBean prototypeBean;

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;    // PrototypeBean을 찾아서 제공해주는 provider

        /**
         * 자바 표준의 Provider 사용할 경우,
         * @Autowired private Provider<PrototypeBean> prototypeBeanProvider;
         */
        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();    // logic 호출할 때 마다 스프링 컨테이너에 요청하면 PrototypeBean 생성해서 반환
//            PrototypeBean prototypeBean = prototypeBeanProvider.get();    // 자바 표준 Provider의 단일 기능 get()
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
        public void addCount() {
            count++;
        }
        public int getCount() {
            return count;
        }
        // 스프링 컨테이너 뜨고 호출
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init : " + this);
        }
        // 스프링 컨테이너 내려갈 때 호출 (prototype 스코프므로 호출 안함)
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
