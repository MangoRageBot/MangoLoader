package org.mangorage.test;

import org.mangorage.test.core.ExampleGeneric;
import org.mangorage.test.token.TokenManager;

public class Test {
    public static void main(String[] args) {
        ExampleGeneric<Integer> TEST = TokenManager.get(new ExampleGeneric<>() {});
        ExampleGeneric<Integer> TEST_TWO = TokenManager.get(new ExampleGeneric<>() {});

        System.out.println(TEST.getType());
        System.out.println(TEST_TWO.getType());
        System.out.println(TEST == TEST_TWO);
    }
}
