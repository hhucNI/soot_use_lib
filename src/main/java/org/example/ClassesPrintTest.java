package org.example;


import org.objectweb.asm.ClassReader;

import java.io.IOException;

public class ClassesPrintTest {

    public static void main(String[] args) {
        try {
            ClassReader cr = new ClassReader("org.example.Task");
            ClassPrintVisitor cp = new ClassPrintVisitor();
            cr.accept(cp, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}