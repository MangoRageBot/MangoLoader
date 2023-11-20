package org.mangorage.test.core;

public class Example {

    public Example() {
        System.out.println("lol");
    }

    public void call() {
        throw new IllegalStateException("Transformer needs to transform this");
    }
}
