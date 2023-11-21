package org.mangorage.test;

import org.mangorage.test.core.ExampleGeneric;
import org.mangorage.test.token.TokenManager;

import java.net.URLClassLoader;

public class Test {

    /*
            Challenge idea:

            here's one I can think of on the spot
            make a system of annotations, applicable to native methods, which allow for defining simple operations involving fields of an object
            for example (you may vary as you like):
            final int firstNumber;
            final int anotherOne;
            // Assume both are assigned

            @Operation(op = @Add, first = "firstNumber", second = "anotherOne")
            protected native int add();

            your ASM code should (essentially) replace the add method with something like
            // Copy above annotations
            protected int add() {
              return firstNumer + anotherOne;
            }
     */

    public static void main(String[] args) {
        ExampleGeneric<Integer> TEST = TokenManager.get(new ExampleGeneric<>() {

        });
        ExampleGeneric<Integer> TEST_TWO = TokenManager.get(new ExampleGeneric<>() {});

        System.out.println(TEST.getType());
        System.out.println(TEST_TWO.getType());
        System.out.println(TEST == TEST_TWO);
    }
}
