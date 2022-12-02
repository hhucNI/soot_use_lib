package org.example;


import org.objectweb.asm.*;

public class ClassPrintVisitor extends ClassVisitor {


    public ClassPrintVisitor() {
        super(Opcodes.ASM4);
    }

    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " {");
    }

    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        if (name.startsWith("is")) {
            System.out.println(" field name: " + name + desc);
        }
        return null;
    }

    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        if (name.startsWith("is")) {
            System.out.println(" start with is method: " + name + desc);
        }
        return null;
    }

    public void visitEnd() {
        System.out.println("}");
    }
}