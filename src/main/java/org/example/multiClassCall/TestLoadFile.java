package org.example.multiClassCall;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.example.GlobalUtils.*;
import static soot.SootClass.SIGNATURES;

public class TestLoadFile {
    public static void main(String[] args) {
        Object o = readObjectFromFile("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\resources\\savedFiles\\structure.dat");
        HashSet<String> structure = (HashSet<String>) o;
//        for (String s : structure) {
//            System.out.println(s);
//        }

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
        Map<String, SootClass> string2SootClass=new HashMap<>();

        loadAllClassesRecursively(ProjectDir,string2SootClass,SIGNATURES);
        Scene.v().loadNecessaryClasses();
        CHATransformer.v().transform();


        for (String s : string2SootClass.keySet()) {
            System.out.println(s);
            SootClass sootClass = string2SootClass.get(s);

            System.out.println("------------------------------sep----------------------");

            List<Map<String, List<String>>> maps = matchedCallsite(sootClass, structure);

            //持久化
            writeObjectToFile(maps,saveDir+ File.separator+sootClass.getName()+"_callsiteData.dat");

            if(maps!=null) {
                for (Map<String, List<String>> map : maps) {
                    for (String s1 : map.keySet()) {
                        List<String> callsites = map.get(s1);
                        for (String callsite : callsites) {
                            System.out.println(s1+"  999  "+callsite);
                        }

                    }
                }
            }


        }


    }
}
