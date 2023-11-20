package org.mangorage.mangoloader.core;

@FunctionalInterface
public interface ITransformer {
    byte[] transform(String className, byte[] original);
}
