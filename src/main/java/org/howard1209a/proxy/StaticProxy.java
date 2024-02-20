package org.howard1209a.proxy;

public class StaticProxy {
    private static class EnhancedApple implements Fruit {
        private Fruit fruit;

        public EnhancedApple(Fruit fruit) {
            this.fruit = fruit;
        }

        @Override
        public String eat(String food) {
            before();
            String result = fruit.eat(food);
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
        Fruit fruit = new EnhancedApple(new Apple());
        String result = fruit.eat("orange");
        System.out.println("result is " + result);
    }
}


