package org.mangorage.test.core.transformers;

import org.mangorage.mangoloader.api.ITransformer;
import org.mangorage.mangoloader.api.TransformerFlags;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

public class ExampleGenericTransformer implements ITransformer {

    private static final String CLASS_TYPE = "org/mangorage/test/core/ExampleGeneric";
    private static final String FUNC_NAME = "getType";
    private static final String FUNC_DESC = "()Ljava/lang/String;";



    @Override
    public int transform(ClassNode classNode, Type classType) {

        if (CLASS_TYPE.equals(classNode.name)) {
            for (MethodNode mtd : classNode.methods) {
                if (FUNC_NAME.equals(mtd.name) && FUNC_DESC.equals(mtd.desc))  {
                    mtd.access &= ~Opcodes.ACC_FINAL;
                }
            }
            return TransformerFlags.REWRITE;
        } else if (CLASS_TYPE.equals(classNode.superName)) {
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
                    if (!stack.isEmpty() && CLASS_TYPE.equals(stack.peek()))
                        cls.set(val);
                }
            });

            if (cls.get() == null)
                throw new IllegalStateException("Could not find signature for GenericExample on " + classNode.name + " from " + classNode.signature);

            var mtd = classNode.visitMethod(Opcodes.ACC_PUBLIC, FUNC_NAME, FUNC_DESC, null, new String[0]);
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