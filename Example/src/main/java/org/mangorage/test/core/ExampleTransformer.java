package org.mangorage.test.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ExampleTransformer extends ClassVisitor {
    public ExampleTransformer(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        // Check if the method is the 'call' method
        if ("call".equals(name) && "()V".equals(desc)) {
            // Replace the entire method with a simple print statement
            return new MethodVisitor(Opcodes.ASM5, mv) {
                @Override
                public void visitCode() {
                    super.visitCode();
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn(1000);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
                    mv.visitInsn(Opcodes.RETURN);
                }
            };
        }

        return mv;
    }

    public static byte[] transform(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ExampleTransformer transformer = new ExampleTransformer(cw);
        cr.accept(transformer, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}