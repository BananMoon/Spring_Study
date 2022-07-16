package com.moonz.securitypractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityPracticeApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        child c = new child();
        c.justSay();    // 부모의 일반 메서드 사용 가능.
        parent pc = new child();    // 추상화가 가능..
        pc.justSay();   // 어떤 클래스의 메서드가 호출되는가?

        impleChild ic = new impleChild();
        ic.Say();
        ic.defaultSay();    // 부모의 디폴트 메서드 사용 가능.
        interfaceParent ip = new impleChild();  // 추상화가 가능..
        ip.defaultSay();    // 어떤 메서드가 호출되는가?

    }

}

abstract class parent {
    public abstract void Say();
    public void justSay() {
        System.out.println("부모 - 일반 메서드");
    }
}

class child extends parent {
    @Override
    public void Say() {
        System.out.println("추상클래스는 추상메서드 구현 강제");
    }
    public void anotherSay() {
        System.out.println("다른 메서드 추가 가능");
    }

    public void justSay() {
        System.out.println("자식이 재정의한 일반 메서드");
    }
}

interface interfaceParent {
    public void Say();
    public default void defaultSay() {
        System.out.println("인터페이스 - 디폴트 메서드");
    }
}

class impleChild implements interfaceParent {
    @Override
    public void Say() {
        System.out.println("인터페이스는 구현 강제. 디폴트 메서드는 구현 강제 X");
    }

    public void defaultSay() {
        System.out.println("자식 - 디폴트 메서드");
    }
}
