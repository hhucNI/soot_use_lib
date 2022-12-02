package org.example.multiClassCall;

import java.io.File;

public class Testpath {
    public static void main(String[] args) {
        File f1=new File("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\java\\org\\example\\multiClassCall\\Test11.java");
        File f2=new File("src/main/java/org/example/multiClassCall/Test11.java");
        System.out.println(f2.getAbsolutePath());
        System.out.println(f1.equals(f2));
    }
}
