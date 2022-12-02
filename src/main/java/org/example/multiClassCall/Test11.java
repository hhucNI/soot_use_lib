package org.example.multiClassCall;

import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import soot.tagkit.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.GlobalUtils.loadClass;


public class Test11 {
    public static void main(String[] args) {
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
//        Options.v().no
        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
//        new String("D:\\1javawork\\software_analysis_projs\\sootclasses-trunk-jar-with-dependencies.jar")
        Scene.v().extendSootClassPath("./src/main/resources/testClasses/multiLevel");//classpath的路径
        Scene.v().extendSootClassPath("./src/main/resources/testClasses");//classpath的路径
//
//        Scene.v().getActiveHierarchy();
        List<String> allClasses = Utils.getAllClasses("./src/main/resources/testClasses");

//        List<SootClass> allSootClasses=new ArrayList<>();
        Map<String, SootClass> str2SootClass = new HashMap<>();
        allClasses.forEach(c -> {
            String substring = c.substring(0, c.lastIndexOf('.'));
            System.out.println(c);
            str2SootClass.put(c, loadClass(substring));
        });


        System.out.println(Scene.v().getSootClassPath());
        System.out.println(Scene.v().getClasses());
        SootClass mainClass = str2SootClass.get("PP.class");
        Scene.v().setMainClass(mainClass);
        Scene.v().loadNecessaryClasses();

        CHATransformer.v().transform();
        for (String s : allClasses) {
            SootClass sootClass = str2SootClass.get(s);

            System.out.println("-------------------------------CLASS SEP-------------------------------------------");
            List<SootMethod> methods = sootClass.getMethods();

            for (SootMethod method : methods) {
                System.out.println("-----------**********--------METHOD SEP-------------------------------------------");

                System.out.println(method.getSignature());
                if (!method.hasActiveBody()) continue;
                Body activeBody = method.getActiveBody();
                PatchingChain<Unit> units = activeBody.getUnits();
                for (Unit unit : units) {
                    List<ValueBox> useBoxes = unit.getUseBoxes();
                    for (ValueBox useBox : useBoxes) {
                        if(useBox.getValue() instanceof InvokeExpr){
                            System.out.println("999    "+useBox.getValue());
//                            System.out.println(((InvokeExpr) useBox.getValue()).getMethodRef());
                        }
                    }
//                    System.out.println(unit);
                }

            }
        }

    }
}
