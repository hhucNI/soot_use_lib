package org.example.multiClassCall;

import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static soot.SootClass.SIGNATURES;

import static org.example.GlobalUtils.loadAllClassesRecursively;
import static org.example.GlobalUtils.loadClass;

public class Test2 {
    public static void main(String[] args) {
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
        Map<String, SootClass> string2SootClass=new HashMap<>();
        loadAllClassesRecursively("D:\\1javawork\\software_analysis_projs\\forTest\\forTest\\target",string2SootClass,SIGNATURES);
        loadAllClassesRecursively("D:\\1javawork\\software_analysis_projs\\forTest\\forTest\\target",string2SootClass,SIGNATURES);

        for (String s : string2SootClass.keySet()) {
            System.out.println(s);
        }
        SootClass mainClass = string2SootClass.get("D:\\1javawork\\software_analysis_projs\\forTest\\forTest\\target\\classes\\excelUtil.class");
        Scene.v().setMainClass(mainClass);
        CHATransformer.v().transform();

        for (String s : string2SootClass.keySet()) {
            SootClass sootClass = string2SootClass.get(s);

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
