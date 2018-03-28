/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Joshu
 */
public class Compile {

    final static int EXECUTION_TIME_ALLOWED = 1000;
    final static int EXECUTION_TIME_INTERVAL = 100;

    private String program;
    private String programName;
    private String path;

    public static class Output {

        public String output = "";
        public String error = "";
        public boolean isInfiniteLoop = false;
        public int timeUsed = 100;
    }

    public Compile(String program, String programName) {

        this.program = program;
        this.programName = programName;
        

        // OS independent temp.
        // File.separator will append the appropriate slash at the end.
        this.path = System.getProperty("java.io.tmpdir") + File.separator;

    }

    public void compile() {

        // OS independent temp.
        // File.separator will append the appropriate slash at the end.
        String path = System.getProperty("java.io.tmpdir") + File.separator;

        // creates the file
        try {
            //Should create a file in your /build/web  directory  
            File root = new File(path);
            root.createNewFile();
            // creates a FileWriter Object and writes to this file
            File forWriter = new File(root, programName + ".java");
            FileWriter writer = new FileWriter(forWriter);
            // Writes the content to the file
            writer.write(program);
            writer.flush();
            writer.close();

            int k = runProcess("javac " + path + programName + ".java");
            if (k == 0) {
                //This is essentially the command line. Not sure how to deal with the
                //Input at this point.
                k = runProcess("java -cp " + path + " " + programName);
                //Change runProcess to return String and we can output that to 
                //the page check it etc.
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void cleanUp() {
        //Cleanup files
        try {
            Path detelePathfile = FileSystems.getDefault().getPath(path, programName + ".class");
            Files.deleteIfExists(detelePathfile);
            File forWriter = new File(new File(path), programName + ".java");
            forWriter.delete();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void executeProgram() {
        //        Output output;
//       
//
//        //running with input
//        output = executeProgram("java", "Exercise02_01",
//                "c:\\temp\\temp1", "c:\\temp\\temp1\\Exercise02_01a.input",
//                "c:\\temp\\temp1\\Exercise02_01a.output");
//        System.out.println(output.error);
//        System.out.println("Result: " + output.output);

    }

    public static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    //We can change this to return a string that can then be used. 
    public static int runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        // System.out.println(command + " exitValue() " + pro.exitValue());
        return pro.exitValue();
    }

    public static Output executeProgram(String command, String program,
            String programDirectory, String inputFile, String outputFile) {
        final Output result = new Output();
        ProcessBuilder pb;

        // For Java security, added c:/etext.policy in c:\program files\jre\bin\security\java.security
        pb = new ProcessBuilder(command, "-Djava.security.manager", program);

        pb.directory(new File(programDirectory));

        pb.redirectErrorStream(true);
        if (inputFile != null) {
            pb.redirectInput(Redirect.from(new File(inputFile)));
        }
        pb.redirectOutput(Redirect.to(new File(outputFile)));

        long startTime = System.currentTimeMillis();

        Process proc = null;
        try {
            proc = pb.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // This separate thread destroy the process if it takes too long time
        final Process proc1 = proc;
        new Thread() {

            public void run() {
                int sleepTime = 0;
                boolean isFinished = false;
                while (sleepTime <= EXECUTION_TIME_ALLOWED && !isFinished) {
                    try {
                        try {
                            Thread.sleep(EXECUTION_TIME_INTERVAL);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
//                System.out.println("sleepTime " + sleepTime);
                        sleepTime += EXECUTION_TIME_INTERVAL;
                        int exitValue = proc1.exitValue();
                        isFinished = true;
//                System.out.println("exitValue " + exitValue);
                    } catch (IllegalThreadStateException ex) {
                    }
                }

                if (!isFinished) {
                    proc1.destroy();
                    result.isInfiniteLoop = true;
//            System.out.println("Infinite loop");
                }
            }
        }.start();

        try {
            int exitCode = proc.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        result.timeUsed = (int) (System.currentTimeMillis() - startTime);

        return result;
    }

}
