package org.howard1209a.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(Apple.class.getClassLoader());
        enhancer.setSuperclass(Apple.class);
        enhancer.setCallback(new AppleMethodIntercepter());
        Apple apple = (Apple) enhancer.create();
        String result = apple.eat("water");
        System.out.println("result is " + result);
    }

    private static class AppleMethodIntercepter implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            before();
            Object result = methodProxy.invokeSuper(o, args);
            after();
            return result;
        }

        private void before() {
            System.out.println("before...");
        }

        private void after() {
            System.out.println("after...");
        }
    }
}
