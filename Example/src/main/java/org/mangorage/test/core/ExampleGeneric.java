package org.mangorage.test.core;

public abstract class ExampleGeneric<T> {
    public String getType() {
        throw new RuntimeException("This will be implemented by a transformer");
    }
}
