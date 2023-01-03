package org.example.multiClassCall;

import com.alibaba.fastjson.JSON;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.example.GlobalUtils.*;
import static soot.SootClass.SIGNATURES;

public class TestLoadFile {
    public static void main(String[] args) throws IOException {
//        Object o = readObjectFromFile("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\resources\\savedFiles\\structure.dat");
        Object o = readObjectFromFile(saveDir+File.separator+jarFileName);
        HashSet<String> structure = (HashSet<String>) o;
//        for (String s : structure) {
//            System.out.println(s);
//        }

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);

        Scene.v().setSootClassPath("D:\\java\\jdk1.8\\jre\\lib\\rt.jar");//rt.jar的路径
        Map<String, SootClass> string2SootClass = new HashMap<>();

        loadAllClassesRecursively(ProjectDir, string2SootClass, SIGNATURES);
        Scene.v().loadNecessaryClasses();
        CHATransformer.v().transform();
        HashSet<SootClass> allClasses = new HashSet<>();
        for (String s : string2SootClass.keySet()) {
            allClasses.add(string2SootClass.get(s));
        }


//        for (String s : string2SootClass.keySet()) {
//
//            System.out.println(s);
//            SootClass sootClass = string2SootClass.get(s);
//
//            System.out.println("------------------------------sep----------------------");

//            List<Map<String, List<String>>> maps = matchedCallsite(sootClass, structure);

            //持久化
//            writeObjectToFile(maps,saveDir+ File.separator+sootClass.getName()+"_callsiteData.dat");


//            File file = new File(saveDir + File.separator + sootClass.getName() + "_callsiteData.json");
//            FileWriter fileWriter = new FileWriter(file);
//            fileWriter.write(s2);

//            try(FileWriter writer = new FileWriter(new File(saveDir + File.separator + sootClass.getName() + "_callsiteData.json"))) {
//            System.out.println(s2);
//            OutputStreamWriter(new File("data.json"))


//            if (maps != null) {
//                for (Map<String, List<String>> map : maps) {
//                    for (String s1 : map.keySet()) {
//                        List<String> callsites = map.get(s1);
//                        for (String callsite : callsites) {
//                            System.out.println(s1 + "  999  " + callsite);
//                        }
//                    }
//                }
//            }
//        }

        List<List<String>> jsonObj = getJsonOfCallsites(allClasses, structure);
        String s2 = JSON.toJSONString(jsonObj);
        try (FileWriter writer = new FileWriter(new File(saveDir + File.separator +"callsiteData.json"))) {
            writer.write(s2);
        } catch (IOException e) {
            // Handle the exception
            System.out.println("ERROR");
        }

    }
}
