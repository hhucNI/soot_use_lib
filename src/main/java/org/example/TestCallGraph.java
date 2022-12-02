package org.example;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Sources;
import soot.options.Options;

import java.util.Iterator;

public class TestCallGraph {

    public static CallGraph cg=null;

    public static void main(String[] args) {
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
//        Options.v().no
        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
//        new String("D:\\1javawork\\software_analysis_projs\\sootclasses-trunk-jar-with-dependencies.jar")
        Scene.v().extendSootClassPath("./src/main/resources/testClasses/");//classpath的路径
//
        loadClass("C",false);//.class
        loadClass("Number",false);//.class
        loadClass("Zero",false);//.class
        loadClass("One",false);//.class
        loadClass("Two",false);//.class
        SootClass mainClass = loadClass("OneCall", true);//.class
        Scene.v().loadNecessaryClasses();
        Body activeBody1 = mainClass.getMethodByName("main").getActiveBody();

        CHATransformer.v().transform();
        CallGraph callGraph = Scene.v().getCallGraph();
        Body activeBody = mainClass.getMethodByName("main").getActiveBody();

        System.out.println("the call graph edge number is : "+ callGraph.size());
        System.out.println("done");

    }


    public static SootClass loadClass(String name,
                                       boolean main) {
        SootClass c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
        if (main) Scene.v().setMainClass(c);
        return c;
    }
//    public static void main(String[] args) {
//        loadClass("Item",false);
//        loadClass("Container",false);
//        SootClass c = loadClass(args[1],true);
    public static void printPossibleCallers(SootMethod target) {
//        CallGraph cg = Scene.v().getCallGraph();
        Iterator sources = new Sources(cg.edgesInto(target));
        while (sources.hasNext()) {
            SootMethod src = (SootMethod)sources.next();
            System.out.println(target + " might be called by " + src);
        }
    }


//    CHATransformer.v().transform();
//    SootClass a = Scene.v().getSootClass("testers.A");
//    SootMethod src = Scene.v().getMainClass().getMethodByName("doStuff");
//    CallGraph cg = Scene.v().getCallGraph();
}
