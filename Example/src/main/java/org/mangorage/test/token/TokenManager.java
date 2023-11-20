package org.mangorage.test.token;

import org.mangorage.test.core.ExampleGeneric;

import java.util.HashMap;

public class TokenManager {

    public static final HashMap<String, ExampleGeneric<?>> TOKENS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> ExampleGeneric<T> get(ExampleGeneric<T> exampleGeneric) {
        return (ExampleGeneric<T>) TOKENS.computeIfAbsent(exampleGeneric.getType().intern(), (a) -> exampleGeneric);
    }

}
