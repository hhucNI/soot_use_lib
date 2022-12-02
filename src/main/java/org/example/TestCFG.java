package org.example;

import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Iterator;
import java.util.List;

public class TestCFG {

    public static void main(String[] args) {
        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
//        new String("D:\\1javawork\\software_analysis_projs\\sootclasses-trunk-jar-with-dependencies.jar")
        Scene.v().extendSootClassPath("./src/main/resources/testClasses/");//classpath的路径
//
        SootClass c = Scene.v().loadClassAndSupport("A");//.class
        Scene.v().loadNecessaryClasses();
//        SootClass c = Scene.v().loadClassAndSupport("MyClass");
        c.setApplicationClass();
// Retrieve the method and its body
        List<SootMethod> methods = c.getMethods();
        for (SootMethod method : methods) {
            System.out.println(method.getName());
        }
        SootMethod m = c.getMethodByName("add");
        Body b = m.retrieveActiveBody();
// Build the CFG and run the analysis
        UnitGraph g = new ExceptionalUnitGraph(b);
//        VeryBusyExpressions an = new SimpleVeryBusyExpressions(g);
// Iterate over the results
        Iterator i = g.iterator();
        while (i.hasNext()) {
            Unit u = (Unit)i.next();
            List<ValueBox> useAndDefBoxes = u.getUseAndDefBoxes();
            for (ValueBox useAndDefBox : useAndDefBoxes) {
                System.out.println(useAndDefBox.getValue());
            }
//            List IN = an.getBusyExpressionsBefore(u);
//            List OUT = an.getBusyExpressionsAfter(u);
// Do something clever with the results
        }
    }
}
