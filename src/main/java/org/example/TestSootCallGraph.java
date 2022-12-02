package org.example;
import java.io.File;
import java.util.*;

import soot.*;
import soot.jimple.Stmt;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;

public class TestSootCallGraph extends SceneTransformer {

    static LinkedList<String> excludeList;

    public static void main(String[] args)	{

        String mainclass = "PP";
        String classesDir = "D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\resources\\testClasses\\multiLevel";
//        String classesDir = "D:\\1javawork\\software_analysis_projs\\soot_use_lib\\src\\main\\resources\\testClasses";
//		//set classpath
        String javapath = System.getProperty("java.class.path");
        String jredir = System.getProperty("java.home")+"/lib/rt.jar";
        String path = javapath+File.pathSeparator+jredir+File.pathSeparator + classesDir;
        Scene.v().setSootClassPath(path);

        //add an intra-procedural analysis phase to Soot
        TestSootCallGraph analysis = new TestSootCallGraph();
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.TestSootCallGraph", analysis));


        excludeJDKLibrary();
//        Options.v().set_prepend_classpath();
        //whole program analysis

        Options.v().set_whole_program(true);
        Options.v().set_process_dir(Arrays.asList(classesDir));
        //load and set main class
        Options.v().set_app(true);
        SootClass appclass = Scene.v().loadClassAndSupport(mainclass);
        Scene.v().setMainClass(appclass);
        Scene.v().loadNecessaryClasses();


        //enable call graph
        //enableCHACallGraph();
        //enableSparkCallGraph();

        //start working
        PackManager.v().runPacks();
    }
    private static void excludeJDKLibrary()
    {
        //exclude jdk classes
        Options.v().set_exclude(excludeList());
        //this option must be disabled for a sound call graph
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
    }
    private static void enableSparkCallGraph() {

        //Enable Spark
        HashMap<String,String> opt = new HashMap<String,String>();
        //opt.put("propagator","worklist");
        //opt.put("simple-edges-bidirectional","false");
        opt.put("on-fly-cg","true");
        //opt.put("set-impl","double");
        //opt.put("double-set-old","hybrid");
        //opt.put("double-set-new","hybrid");
        //opt.put("pre_jimplify", "true");
        SparkTransformer.v().transform("",opt);
        PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
    }

    private static void enableCHACallGraph() {
        CHATransformer.v().transform();
    }

    private static LinkedList<String> excludeList()
    {
        if(excludeList==null)
        {
            excludeList = new LinkedList<String> ();

            excludeList.add("java.");
            excludeList.add("javax.");
            excludeList.add("sun.");
            excludeList.add("sunw.");
            excludeList.add("com.sun.");
            excludeList.add("com.ibm.");
            excludeList.add("com.apple.");
            excludeList.add("apple.awt.");
        }
        return excludeList;
    }

    @Override
    protected void internalTransform(String phaseName,
                                     Map options) {

        int numOfEdges =0;

        CallGraph callGraph = Scene.v().getCallGraph();
        for(SootClass sc : Scene.v().getApplicationClasses()){
            for(SootMethod m : sc.getMethods()){

                Iterator<MethodOrMethodContext> targets = new Targets(
                        callGraph.edgesOutOf(m));

                while (targets.hasNext()) {

                    numOfEdges++;

                    SootMethod tgt = (SootMethod) targets.next();
                    System.out.println(m + " may call " + tgt);
                }
            }
        }

        System.err.println("Total Edges:" + numOfEdges);

    }
}