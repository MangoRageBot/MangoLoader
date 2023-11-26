package org.mangorage.test.core.transformers;

import org.mangorage.mangoloader.core.MangoClassloader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class AnnotationTransformer implements MangoClassloader.ITransformer {
    @Override
    public MangoClassloader.TransformerFlags transform(ClassNode classNode, Type classType) {








        return MangoClassloader.TransformerFlags.NO_REWRITE;
    }

    @Override
    public String getName() {
        return "Annotation Transformer";
    }
}
