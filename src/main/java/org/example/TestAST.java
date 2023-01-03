package org.example;

import soot.SootClass;
import soot.jimple.parser.JimpleAST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestAST {
    public static void main(String[] args) {
        try {
            File jf=new File("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\sootOutput\\H.jimple");
            InputStream is = new FileInputStream(jf);
            JimpleAST jast = new JimpleAST(is);
            SootClass sc = jast.createSootClass();
            System.out.println(sc.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
