package dev.ramar.e2;

import java.util.*;
import java.io.*;

/*
Class: Main
 - Entrypoint from the command line
 - Use the static constructor to add your class
   as an Entrypoint to add yourself to main() 
   execution, *before* main actually executes.
 - If this is bundled within Dev Ramar's Project Template,
   any dependency in the build file's jar/dependency folder
   will be loaded and ready to be used **IF YOU USE THE 
   ENDPOINT SYSTEM**
*/
public class Main
{

    private static final String JAR_NAME = "EngineR2.jar";
    /* Static Constructor */
    private static void onSetup()
    {
        E2Main.setup();
    }


    /* RListening_Entrypoints 
    -===-------------------------
     This RListening implementation allows outside
     classes to run on command line execution, while
     also having a mandatory pre-process done before
     envokation.
     If this is bundled within Dev Ramar's Project Template,
     the pre-processing is loading the PROJ_dependency folder
     that's inside the jar file into the directory, so all
     Entrypoints can access dependencies.
    */
    public static class Entrypoints
    {
        /* Implementable Interface
        -====------------------------
         Use this interface as a lambda / anonymous class
         with addEntrypoint() to add an entrypoint properly
        */
        public interface Entrypoint
        {
            public void start(String[] cmargs);
        }
    
        // all entrypoints
        private static final List<Entrypoint> entrypoints = new ArrayList<>();
        // some extra info in-case someone tries to addEntrypoint() after
        // main has run (runs straight away)
        private static boolean started = false;
        private static String[] startArgs = null;

        private static final List<Thread> startedThreads = new ArrayList<>();

        private static void startEntrypoint(Entrypoint ep)
        {
            Thread t = null;
            synchronized(startedThreads)
            {
                t = new Thread(() -> 
                {
                    outputLN("Start!");
                    ep.start(startArgs);
                }, "RamaRunner #" + (startedThreads.size() + 1));
                startedThreads.add(t);
            }

            t.start();
        }


        public static void addEntrypoint(Entrypoint ep)
        {
            if( started )
                startEntrypoint(ep);

            synchronized(entrypoints)
            {
                entrypoints.add(ep);
            }
        }


        public static void removeEntrypoints(Entrypoint ep)
        {
            synchronized(entrypoints)
            {
                entrypoints.remove(ep);
            }
        }



        private static void start(String[] cmargs)
        {
            startArgs = cmargs;
            started = true;

            synchronized(entrypoints)
            {
                for( Entrypoint ep : entrypoints )
                    startEntrypoint(ep);                    
            }
        }


        private static boolean waitForGroupDeath()
        {
            boolean exp = true;
            try
            {
                for( Thread t : startedThreads )
                    t.join();
            }
            catch(InterruptedException e)
            {
                exp = false;
            }

            return exp;
        }
    }



    /* Helper Methods
    -==-----------------
    */
    public static void outputLN(String msg)
    {
        String name = Thread.currentThread().getName();
        System.out.println("[" + name + "] " + msg);
    }




    /*
    Main:
     - To load all dependencies into a usable format
     - Keep this as is if you want dependencies to work
    */
    public static void main(String[] args)
    {
        // TEST if args[0] could be the name of the jar
        try
        {
            System.out.println("[RamaRunner] Inflating dependencies");
            System.out.println(Arrays.toString(new File(".").listFiles()));
            Process p = synchronousExecute("jar xf ./" + JAR_NAME + " PROG_dependencies");
            onSetup();
            System.out.println("Starting!");
            System.out.println("--------------------");
            Entrypoints.start(args);
            Entrypoints.waitForGroupDeath();
            System.out.println("--------------------");
            System.out.println("Shutting down!");
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("[RamaRunner] Clearing dependencies");
            deleteDirectoryLegacyIO(new File("PROG_dependencies"));
        }
    }


    public static void deleteDirectoryLegacyIO(File file) 
    {

        File[] list = file.listFiles();
        if (list != null) 
        {
            for (File temp : list) 
            {
                //recursive delete
                deleteDirectoryLegacyIO(temp);
            }
        }

        if( file.delete() )
            System.out.println("deleted " + file);
        else
            System.out.println("failed to delete " + file);
    }

    private static Process synchronousExecute(String command) throws IOException
    {
        Process exp = Runtime.getRuntime().exec(command);
        try
        {
            exp.waitFor();
        }
        catch(InterruptedException e) {}

        return exp;
    }


}