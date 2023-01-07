package org.example;

import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.toolkits.callgraph.Sources;
import soot.options.Options;

import java.io.*;
import java.sql.Array;
import java.util.*;

import static soot.SootClass.SIGNATURES;


public class GlobalUtils {

//    public static String ProjectDir="D:\\1javawork\\software_analysis_projs\\SeveralPackage\\target\\classes";
    public static String ProjectDir="D:\\1javawork\\software_analysis_projs\\forTest\\forTest\\target\\classes";


    public static String jarFileName;
//    public static String jarDir = "D:\\1javawork\\software_analysis_projs\\gson-2.2.4";
    public static String jarDir = "D:\\1javawork\\software_analysis_projs\\poi-ooxml-4.1.2";

    public static String jdkPath="D:\\java\\jdk1.8\\jre\\lib\\rt.jar";




    static {
        jarFileName=jarDir.substring(jarDir.lastIndexOf('\\')+1)+"_.dat";
    }
    public static String saveDir="D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\resources\\savedFiles";
    public static void main(String[] args) {

    }
    public static void initSootEnvironment() {
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Scene.v().setSootClassPath(jdkPath);//rt.jar的路径
    }
//    public static Object readStrFromFile(String filePath)

    public static Object readObjectFromFile(String filePath) {
        Object temp = null;
        File file = new File(filePath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * TODO 在这个函数中构建library中的类的标记，返回一个hashmap这样的
     * TODO 可以不用维护树结构，可以用双层hashmap，
     * TODO 第一层key是目录，第一层value也是第二层key是filename，第二层value是自定义的数据结构
     * todo 含有一个标记是否为lib class以及一个sootclass
     * <p>
     * TODO 说明一定要用反斜杠吧，规定一下路径格式，防止找不到对应的key
     *
     * @param dirName
     * @return
     */
    public static void loadAllClassesRecursively(String dirName, int resolvingLevel) {
        Queue<File> dirQueue = new LinkedList<>();
        int dirNameLen = dirName.length();
        File dir = new File(dirName);
        dirQueue.offer(dir);
        while (!dirQueue.isEmpty()) {
            File curDir = dirQueue.poll();
            //get relative path(package name)


            //add classpath to soot
            String curDirPath = curDir.getPath();
            String relativePath = curDirPath.substring(curDirPath.lastIndexOf(dirName) + dirNameLen);
            String replaced;
            if (relativePath.length() == 0) {
                replaced = "";
            } else {
                replaced = relativePath.substring(1).replace("\\", ".") + ".";
            }
            Scene.v().extendSootClassPath(curDirPath);//classpath的路径

            String[] fileList = curDir.list();
            for (String s : fileList) {
                File f = new File(curDirPath + "/" + s);
                if (f.isFile()) {
                    String name = f.getName();
                    String subfix = "";
                    int dotIndex = name.lastIndexOf(".");
                    if (dotIndex != -1) {
                        subfix = name.substring(dotIndex);
                    }
                    if (".class".equals(subfix)) {
                        String fileNameWithNoSubfix = name.substring(0, name.lastIndexOf('.'));
                        //todo load 全类名
                        // todo 鉴于一开始get不到package name，先使用路径
                        //

//                        String fullPackage = getPackageNameFromJava(curDirPath + "/" + s.split("\\.")[0] + ".java");
                        String fullPackage = replaced;//先这样处理
                        String fullClassName = fullPackage + fileNameWithNoSubfix.trim();
                        SootClass c = loadClass(fullClassName);
//                        Scene.v().addBasicClass(fullClassName,resolvingLevel);

                    }

                } else {
                    dirQueue.offer(f);
                }
            }
        }
    }


    public static String getPackageNameFromClass(String filePath) {
        return "";
    }

    //    public static String getPackageNameFromDir(){
//
//    }
    public static void printCallsite(SootMethod method) {
        if (!method.hasActiveBody()) return;
        Body activeBody = method.getActiveBody();
        PatchingChain<Unit> units = activeBody.getUnits();
        for (Unit unit : units) {
            List<ValueBox> useBoxes = unit.getUseBoxes();
            for (ValueBox useBox : useBoxes) {
                if (useBox.getValue() instanceof InvokeExpr) {
//                    System.out.println("999    "+useBox.getValue());
                    System.out.println("methodref   : " + ((InvokeExpr) useBox.getValue()).getMethodRef());
                }
            }
        }
    }
    public static List<List<String>> getJsonOfCallsites(Set<SootClass> allClasses,Set<String> dependency){
        List<List<String>> ret=new ArrayList<>();
        for (SootClass sc : allClasses) {
            List<SootMethod> methods = sc.getMethods();
            for (SootMethod method : methods) {
//                Body body = method.getSource().getBody();
//
//                if (!method.hasActiveBody()) continue;
                
                Body activeBody = method.retrieveActiveBody();
                PatchingChain<Unit> units = activeBody.getUnits();
                for (Unit unit : units) {
                    List<ValueBox> useBoxes = unit.getUseBoxes();
                    for (ValueBox useBox : useBoxes) {
                        if (useBox.getValue() instanceof InvokeExpr) {
                            InvokeExpr invokeExpr = (InvokeExpr) useBox.getValue();
                            SootMethodRef methodRef = invokeExpr.getMethodRef();

                            //过滤jre
                            String classNamePart = methodRef.toString().split(":")[0];
//                            TODO fix bug classNamePart.indexOf("java")!=1？ is that right

                            String tt = methodRef.toString();
                            if (dependency.contains(tt) && classNamePart.indexOf("java")!=1) {
                                //外部依赖匹配
                                int lineNumber = unit.getJavaSourceStartLineNumber();

//                                useBox.getJavaSourceStartLineNumber();
//                                invokeExpr.getL
                                List<String> Line=new ArrayList<>();
                                Line.add(sc.getName());
                                Line.add(method.getSignature());
                                Line.add(lineNumber+"");
                                Line.add(methodRef.toString());
                                ret.add(Line);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    public static List<Map<String, List<String>>> matchedCallsite(SootClass sootClass, Set<String> dependency) {
        //需要一个method的集合
        List<Map<String, List<String>>> ret=new ArrayList<>();
        List<SootMethod> methods = sootClass.getMethods();
        for (SootMethod method : methods) {
            Map<String, List<String>> method2Callsite = new HashMap<>();
            ret.add(method2Callsite);
            List<String> callsitesOfOneMethod=new ArrayList<>();
            String key=method.getSignature();
            method2Callsite.put(key,callsitesOfOneMethod);

            if (!method.hasActiveBody()) continue;

            Body activeBody = method.getActiveBody();
            PatchingChain<Unit> units = activeBody.getUnits();
            for (Unit unit : units) {
                List<ValueBox> useBoxes = unit.getUseBoxes();
                for (ValueBox useBox : useBoxes) {
                    if (useBox.getValue() instanceof InvokeExpr) {
//                    System.out.println("999    "+useBox.getValue());
                        SootMethodRef methodRef = ((InvokeExpr) useBox.getValue()).getMethodRef();

                        //过滤jre
                        String classNamePart = methodRef.toString().split(":")[0];

                        if (dependency.contains(methodRef.toString()) && classNamePart.indexOf("java")!=1) {
                            callsitesOfOneMethod.add(methodRef.toString());
                            //外部依赖匹配
                        }
                    }
                }
            }
//            printCallsite(method);
            if(callsitesOfOneMethod.size()==0){
                method2Callsite.remove(key);
            }
        }
        return ret;

    }

    public static void IsInJar(SootMethod method, Set<SootMethod> s) {
        if (!method.hasActiveBody()) return;
        Body activeBody = method.getActiveBody();
        PatchingChain<Unit> units = activeBody.getUnits();
        for (Unit unit : units) {
            List<ValueBox> useBoxes = unit.getUseBoxes();
            for (ValueBox useBox : useBoxes) {
                if (useBox.getValue() instanceof InvokeExpr) {
//                    System.out.println("999    "+useBox.getValue());
                    String s1 = ((InvokeExpr) useBox.getValue()).getMethodRef().toString();
                    if (s.contains(s1)) {
                        System.out.println("methodref   : " + s1);

                    }
                }
            }
//                    System.out.println(unit);
        }
    }


    public static String getPackageNameFromJava(String filePath) {
        Scanner sc = null;

        try {
            //先假设这个文件一定有
            File corespondJavaFile = new File(filePath);

            sc = new Scanner(corespondJavaFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().length() == 0) continue;
            //如果不是空行
            int lastIndex = line.lastIndexOf("package");
            if (lastIndex != -1) {
                //找到package name
                System.out.println(lastIndex);
                String trimmed = line.substring(lastIndex + "package".length()).trim();
                return trimmed.substring(0, trimmed.length() - 1) + ".";

            } else {
                //是别的内容，说明没有包名
                return "";
            }

        }
        return "";
    }

    public static void loadCurDirClasses(File curDir) {

    }

    public static void writeObjectToFile(Object obj, String saveFileName) {
        File file = new File(saveFileName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    public static void loadAllClassesRecursively(String dirName, Map<String, SootClass> path2SootClass, int resolvingLevel) {
        Queue<File> dirQueue = new LinkedList<>();
        int dirNameLen = dirName.length();
        File dir = new File(dirName);
        dirQueue.offer(dir);
        while (!dirQueue.isEmpty()) {
            File curDir = dirQueue.poll();
            //get relative path(package name)


            //add classpath to soot
            String curDirPath = curDir.getPath();
            String relativePath = curDirPath.substring(curDirPath.lastIndexOf(dirName) + dirNameLen);
            String replaced;
            if (relativePath.length() == 0) {
                replaced = "";
            } else {
                replaced = relativePath.substring(1).replace("\\", ".") + ".";
            }
            Scene.v().extendSootClassPath(curDirPath);//classpath的路径
            System.out.println(curDirPath);
            String[] fileList = curDir.list();
            for (String s : fileList) {
                File f = new File(curDirPath + "/" + s);
                if (f.isFile()) {
                    String name = f.getName();
                    String subfix = "";
                    int dotIndex = name.lastIndexOf(".");
                    if (dotIndex != -1) {
                        subfix = name.substring(dotIndex);
                    }
                    if (".class".equals(subfix)) {
                        String fileNameWithNoSubfix = name.substring(0, name.lastIndexOf('.'));
                        //todo load 全类名
                        // todo 鉴于一开始get不到package name，先使用路径
                        //

//                        String fullPackage = getPackageNameFromJava(curDirPath + "/" + s.split("\\.")[0] + ".java");
                        String fullPackage = replaced;//先这样处理
                        String fullClassName = fullPackage + fileNameWithNoSubfix.trim();
                        Scene.v().addBasicClass(fullClassName, resolvingLevel);
                        System.out.println(fullClassName);
                        SootClass c = loadClass(fullClassName);
                        try {
                            path2SootClass.put(f.getCanonicalPath(), c);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                } else {
                    dirQueue.offer(f);
                }
            }
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

                } else {
                    dirQueue.offer(f);
                }
            }
        }

        return res;
    }

    public static SootClass loadClass(String name,
                                      boolean main) {
        SootClass c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
        if (main) Scene.v().setMainClass(c);
        return c;
    }

    public static SootClass loadClassAndAddBasic(String name) {
        SootClass c = Scene.v().loadClassAndSupport(name);
        Scene.v().addBasicClass(name, SIGNATURES);
        c.setApplicationClass();
        return c;
    }

    public static SootClass loadClass(String name) {
        SootClass c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
        return c;
    }
//    public static void printPossibleCallers(SootMethod target) {
////        CallGraph cg = Scene.v().getCallGraph();
//        Iterator sources = new Sources(cg.edgesInto(target));
//        while (sources.hasNext()) {
//            SootMethod src = (SootMethod)sources.next();
//            System.out.println(target + " might be called by " + src);
//        }
//    }
}
