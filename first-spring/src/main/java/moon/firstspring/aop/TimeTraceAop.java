package moon.firstspring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component // : 스프링 빈 등록을 Component로도 할 수 있지만 특별한 기능이므로 직접 bean에 등록하는 것이 일반적이다.
@Aspect //AOP로 쓰기위한 어노테이션
public class TimeTraceAop {

    @Around("execution(* moon.firstspring..*(..))")  // 공통관심사항 적용 (타겟팅)!  * 패키지명..클래스명(파라미터 타입))" -> * moon.firstspring..*(..))
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());   // 어떤 메서드를 콜할지 알수 있음

        try {
            return joinPoint.proceed();    // 다음 메서드로 진행
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
