package org.howard1209a.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy {
    private static class FruitHandler implements InvocationHandler {
        private Fruit fruit;

        public FruitHandler(Fruit fruit) {
            this.fruit = fruit;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            before();
            Object result = method.invoke(fruit, args);
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

    public static void main(String[] args) {
        Fruit fruit = (Fruit) Proxy.newProxyInstance(Fruit.class.getClassLoader(), new Class[]{Fruit.class}, new FruitHandler(new Apple()));
        String result = fruit.eat("banana");
        System.out.println("result is " + result);
    }
}
