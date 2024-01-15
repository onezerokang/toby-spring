package kang.onezero.tobyspring.learningtest.proxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyTest {
    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget(); // 타깃은 인터페이스를 통해 접근하는 습관을 들이자.
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

        Hello proxiedHello = new HelloUppercase(hello);
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(), // 동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로더
            new Class[] { Hello.class }, // 구현할 인터페이스
            new UppercaseHandler(new HelloTarget())); // 부가기능과 위임 코드를 담은 InvocationHandler

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); // 타깃 설정
        pfBean.addAdvice(new UppercaseAdvice());; // 부가기능을 담은 어드바이스. 여러 개 추가 가능

        Hello proxiedHello = (Hello) pfBean.getObject(); // FactoryBean이므로 getObject()로 생성된 프록시를 가져온다.

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

    @Test
    public void classNamePointcutAdvice() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() { // 익명 내부 클래스 방식으로 클래스 정의
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT"); // 클래스 이름이 HelloT로 시작하는 것만 선정한다.
                    }
                };
            }
        };

        classMethodPointcut.setMappedName("sayH*"); // sayH로 시작하는 메소드 이름을 가진 메소드만 선정한다.

        // 테스트
        checkAdvice(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {};
        checkAdvice(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {};
        checkAdvice(new HelloToby(), classMethodPointcut, true);

    }

    private void checkAdvice(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            // 메소드 선정 방식을 통해 어드바이스 적용
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
            assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
        } else {
            // 어드바이스 적용 대상 후보에서 야예 탈락
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello Toby");
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi Toby");
            assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
        }
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다. MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문이다.
            String ret = (String) invocation.proceed();
            return ret.toUpperCase(); // 부가기능 적용
        }
    }

}
