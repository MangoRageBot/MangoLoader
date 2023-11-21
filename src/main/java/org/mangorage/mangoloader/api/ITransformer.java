package org.mangorage.mangoloader.api;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public interface ITransformer {
    int transform(ClassNode classNode, Type classType);
    String getName();
}
