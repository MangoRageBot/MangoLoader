package org.mangorage.test.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class ExampleUtils {
    public static ClassNode getClassNode(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        return classNode;
    }
}
