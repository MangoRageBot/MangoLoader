package org.mangorage.test.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ExampleTransformer extends ClassVisitor {
    public ExampleTransformer(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
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
                    // Call the overridden visitCode method of the original MethodVisitor (mv)
                    mv.visitCode();

                    // New Additional Code
                    mv.visitVarInsn(Opcodes.ALOAD, 0); // Load 'this' onto the stack
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("We have initiated this Example class");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    mv.visitInsn(Opcodes.RETURN);
                }
            };
        }

        return mv;
    }
}