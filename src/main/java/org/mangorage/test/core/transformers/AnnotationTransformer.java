package org.mangorage.test.core.transformers;

import org.mangorage.mangoloader.api.ITransformer;
import org.mangorage.mangoloader.api.TransformerFlags;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public class AnnotationTransformer implements ITransformer {
    @Override
    public int transform(ClassNode classNode, Type classType) {








        return TransformerFlags.NO_REWRITE;
    }

    @Override
    public String getName() {
        return "Annotation Transformer";
    }
}
