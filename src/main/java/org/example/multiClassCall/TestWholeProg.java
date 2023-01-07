package org.example.multiClassCall;

import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;
import soot.util.Chain;
import static soot.SootClass.SIGNATURES;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.example.GlobalUtils.*;

public class TestWholeProg {
    static Set<String> structure=null;
    public static void main(String[] args) throws FileNotFoundException {
        initSootEnvironment();

        loadAllClassesRecursively(jarDir,SIGNATURES);
        Chain<SootClass> JarClasses = Scene.v().getClasses();

        Scene.v().loadNecessaryClasses();

        structure=getMethodSigSet(JarClasses);
        writeObjectToFile(structure,saveDir+ File.separator+jarFileName);

        System.out.println("done");







//        loadAllClassesRecursively("D:\\1javawork\\software_analysis_projs\\SeveralPackage\\target\\classes",SIGNATURES);
//        Map<String, SootClass> string2SootClassForJar=new HashMap<>();
//        Chain<SootClass> classChain1 = Scene.v().getClasses();
//        Chain<SootClass> classChain = Scene.v().getApplicationClasses();
    }




    public static Map<String,List<String>> getStructure(Map<String,SootClass> string2SootClass){
        Map<String,List<String>> sootClass2MethodSig=new HashMap<>();
        for (String s : string2SootClass.keySet()) {
            SootClass sootClass = string2SootClass.get(s);
            List<SootMethod> methods = sootClass.getMethods();
            List<String> sigs=new ArrayList<>();

            for (SootMethod method : methods) {
                sigs.add(method.getSignature());

//                getCallsite(method);
            }
            sootClass2MethodSig.put(sootClass.getName(),sigs);
        }
        return sootClass2MethodSig;
    }


    public static Set<String> getMethodSigSet(Chain<SootClass> classes){
        Set<String> methodSigSet=new HashSet<>();
        for (SootClass aClass : classes) {
            List<SootMethod> methods = aClass.getMethods();
            for (SootMethod method : methods) {
                methodSigSet.add(method.getSignature());
            }
        }
        return methodSigSet;
    }
    public static Set<String> getMethodSigSet(Map<String,SootClass> string2SootClass){
        Set<String> methodSigSet=new HashSet<>();
        for (String s : string2SootClass.keySet()) {
            SootClass sootClass = string2SootClass.get(s);
            List<SootMethod> methods = sootClass.getMethods();

            for (SootMethod method : methods) {
                methodSigSet.add(method.getSignature());
            }
        }
        return methodSigSet;
    }
}
