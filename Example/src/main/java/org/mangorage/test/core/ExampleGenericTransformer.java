package org.mangorage.test.core;

import org.mangorage.mangoloader.api.ITransformer;
import org.mangorage.mangoloader.api.TransformerFlags;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

public class ExampleGenericTransformer implements ITransformer {

    @Override
    public int transform(ClassNode classNode, Type classType) {

        if ("org/mangorage/test/core/ExampleGeneric".equals(classNode.superName)) {
            AtomicReference<String> cls = new AtomicReference<>();

            SignatureReader reader = new SignatureReader(classNode.signature);
            reader.accept(new SignatureVisitor(Opcodes.ASM9) {
                Deque<String> stack = new ArrayDeque<>();

                @Override
                public void visitClassType(final String name) {
                    stack.push(name);
                }

                @Override
                public void visitInnerClassType(final String name) {
                    stack.push(stack.pop() + '$' + name);
                }

                @Override
                public void visitEnd() {
                    var val = stack.pop();
                    if (!stack.isEmpty() && "org/mangorage/test/core/ExampleGeneric".equals(stack.peek()))
                        cls.set(val);
                }
            });

            if (cls.get() == null)
                throw new IllegalStateException("Could not find signature for GenericExample on " + classNode.name + " from " + classNode.signature);

            var mtd = classNode.visitMethod(Opcodes.ACC_PUBLIC, "getType", "()Ljava/lang/String;", null, new String[0]);
            mtd.visitLdcInsn(cls.get());
            mtd.visitInsn(Opcodes.ARETURN);
            mtd.visitEnd();

            return TransformerFlags.REWRITE;
        }

        return TransformerFlags.NO_REWRITE;
    }

    @Override
    public String getName() {
        return "ExampleGenericTransformer";
    }
}