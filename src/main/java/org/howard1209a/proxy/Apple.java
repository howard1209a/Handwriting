package org.howard1209a.proxy;

public class Apple implements Fruit {
    @Override
    public String eat(String food) {
        System.out.println("eat " + food);
        return "eat " + food;
    }
}
