package org.example.multiClassCall;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Utils {

    public static void main(String[] args) {
//        String ss = "fefiwjf";
//        int i = ss.lastIndexOf(".");
//
//        File dir = new File("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\resources\\testClasses");
//        System.out.println(dir.getPath());
//        String[] fileList = dir.list();
//        for (String s : fileList) {
//            System.out.println(s);
//        }

//        D:\1javawork\software_analysis_projs\soot_use_lib\target
//        System.out.println(i);


        List<String> allClasses = getAllClasses("D:\\1javawork\\software_analysis_projs\\soot_use_lib\\target");
        for (String c : allClasses) {
            System.out.println(c);
        }
    }



    public static List<String> getAllClasses(String dirName) {
        //BFS

        List<String> res = new ArrayList<>();
        Queue<File> dirQueue = new LinkedList<>();
        File dir = new File(dirName);
        dirQueue.offer(dir);
        while (!dirQueue.isEmpty()) {
            File curDir = dirQueue.poll();
            System.out.println(curDir.getPath());

            String[] fileList = curDir.list();
            for (String s : fileList) {
                File f = new File(curDir.getPath() + "/" + s);
                if (f.isFile()) {
                    String name = f.getName();
                    String subfix = "";
                    int dotIndex = name.lastIndexOf(".");
                    if (dotIndex != -1) {
                        subfix = name.substring(dotIndex);
                    }
                    if (".class".equals(subfix)) {
                        res.add(name);
                    }

                }else{
                    dirQueue.offer(f);
                }
            }
        }

        return res;
    }

}
