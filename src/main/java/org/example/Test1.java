package org.example;

import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.options.Options;
import soot.util.Chain;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Test1 {
    public static void testPath() {
        //        Scene.v().extendSootClassPath("./src/");//classpath的路径
        File directory = new File(".");

        try {
            System.out.println(directory.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));

        System.out.println(Test1.class.getResource("/").getPath());
        System.out.println(Test1.class.getResource("").getPath());
//        System.out.println(Test1.class.getResource("").getPath());



    }
    public static void main(String[] args) {
        Options.v().set_whole_program(true);

        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
//        new String("D:\\1javawork\\software_analysis_projs\\sootclasses-trunk-jar-with-dependencies.jar")
        Scene.v().extendSootClassPath("./src/main/resources/testClasses/");//classpath的路径
//
        SootClass sClass = Scene.v().loadClassAndSupport("OneCall");//.class
        SootClass C= Scene.v().loadClassAndSupport("C");//.class
//        SootClass sClass = Scene.v().loadClassAndSupport("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\target\\classes\\testClasses\\A.java");//.class
//        Chain<SootClass> classes = Scene.v().getClasses();
//        for (SootClass aClass : classes) {
//            System.out.println(aClass.getName());
//        }

        CallGraph callGraph = Scene.v().getCallGraph();

        SootMethod id = C.getMethod("id");

        Iterator sources = new Sources(callGraph.edgesInto(id));
        while (sources.hasNext()) {
            SootMethod src = (SootMethod)sources.next();
            System.out.println(id + " might be called by " + src);
        }

        Scene.v().loadNecessaryClasses();//加载必须的类
        //TODO 试一下加载多个类 以及build call graph


        List<SootMethod> sMethods = sClass.getMethods();
        Chain<SootField> sFields = sClass.getFields();

        List<SootMethod> cm = C.getMethods();
        Chain<SootField> cf= C.getFields();
//        new String("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\target")
        //    System.out.println("getClasses()");
        //    for(SootClass c : Scene.v().getClasses()){
        //      System.out.println(c);
        //    }

        for(SootMethod m : sMethods){
            System.out.println(m);
        }

//        System.out.println("getDeclaration()");
//        System.out.println(sFields.size());

        for(SootField f : sFields){
            System.out.println(f.getDeclaration());
        }
        System.out.println("----------------------------------.");

        for(SootMethod m : cm){
            System.out.println(m);
        }
        System.out.println("In the end.");

    }
}
