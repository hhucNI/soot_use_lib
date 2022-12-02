package org.example;

import soot.Scene;
import soot.options.Options;

public class TestProcessDir {
    public static void main(String[] args) {
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
//        Options.v().no
        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
//        new String("D:\\1javawork\\software_analysis_projs\\sootclasses-trunk-jar-with-dependencies.jar")
        Scene.v().extendSootClassPath("./src/main/resources/testClasses");//classpath的路径

    }
}
