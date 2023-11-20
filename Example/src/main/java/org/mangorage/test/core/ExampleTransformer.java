package org.mangorage.test.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ExampleTransformer extends ClassVisitor {
    public ExampleTransformer(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        System.out.println(access + " -> 1");
        System.out.println(name+ " -> 2");
        System.out.println(desc+ " -> 3");
        System.out.println(signature+ " -> 4");
        // Check if the method is the 'call' method
        if ("call".equals(name) && "()V".equals(desc)) {
            // Replace the entire method with a simple print statement
            return new MethodVisitor(Opcodes.ASM9, mv) {
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

        if ("<init>".equals(name) && "()V".equals(desc)) {
            return new MethodVisitor(Opcodes.ASM9, mv) {
                @Override
                public void visitCode() {
                    super.visitCode();

                    // Original
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("lol c");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                    // New Additional Code
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("We have initiated this Example class");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }


            };
        }

        return mv;
    }

    public static byte[] transform(String className, byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ExampleTransformer transformer = new ExampleTransformer(cw);
        cr.accept(transformer, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}