/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Joshu
 */
public class Compile {

    final static int EXECUTION_TIME_ALLOWED = 1000;
    final static int EXECUTION_TIME_INTERVAL = 100;
    public final static String TEMP_PATH = System.getProperty("java.io.tmpdir");
    final String WORKING_DIR = System.getProperty("java.io.tmpdir") + "CheckExercise" + File.separator;

    private String program;
    private String programName;
    private String path;
    private String compilePrint = "";
    private String args;
    private Exercise exercise;
    private Output output;

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

    private Compile(Exercise exercise) {
        File temp = new File(TEMP_PATH, "CheckExercise");
        // Create CheckExercise directory within TEMP
        if (!temp.exists()) {
            temp.mkdir();
        }
        // Create a directory with the exercise name
        File exercisePath = new File(temp, exercise.toString());
//        System.out.println(exercisePath.getAbsolutePath());
        exercisePath.mkdir();
        this.path = exercisePath.getAbsolutePath();
        this.program = ""; // Init program to be empty
        this.output = new Output();
        this.exercise = exercise;
    }

    public Compile(Exercise exercise, String program) {
        this(exercise);
        this.setProgram(program);
    }

//    public int compile() throws IOException, Exception {
//        //Should create a file in your /build/web  directory  
//        File root = new File(path);
//        root.createNewFile();
//        // creates a FileWriter Object and writes to this file
//        File forWriter = new File(root, exercise.toString() + ".java");
//        FileWriter writer = new FileWriter(forWriter);
//        // Writes the content to the file
//        writer.write(program);
//        writer.flush();
//        writer.close();
//
//        // Return 0 if success otherwise if not
//        return runProcess("javac", forWriter.getAbsolutePath());
//    }
//
//    public int run(String... args) throws IOException, Exception {
//        int exitValue = this.compile();
//        // If compilation succeeded run file with args
//        // else return compilation exitValue.
//        // This way the output of compilation is accessable if 
//        // compilation failed since runProcess sets new output in each run.
//        if (exitValue == 0) {
//            ArrayList<String> argsList = new ArrayList<>(Arrays.asList(new String[]{"java", "-cp", path, exercise.toString()}));
//            // Pass args from user
//            argsList.addAll(Arrays.asList(args));
//            return runProcess(argsList.toArray(new String[argsList.size()]));
//        } else {
//            return exitValue;
//        }
//    }
    public static void cleanUp() {
        //Cleanup files
        File deletePathfile = new File(TEMP_PATH, "CheckExercise");
        // Delete all files within directory before deleting directory
        for (File f : deletePathfile.listFiles()) {
            if (f.isDirectory()) {
                for(File sub: f.listFiles()) {
                    sub.delete();
                }
            }
            f.delete();
        }
        deletePathfile.delete();
    }

    private static void writeToFile(File file, String data) throws Exception {
//        fixOutput(data);
        Files.deleteIfExists(file.toPath());
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write(data);
        fw.flush();
        fw.close();
    }
    
    private static String fixOutput(String program) {
        Pattern findScanner = Pattern.compile("Scanner\\s+([a-zA-Z0-9_]+)");
        Matcher m = findScanner.matcher(program);
        if (m.find()) {
            for(int i = 1; i < m.groupCount(); i++) {
                System.out.println(m.group(i+1));
                
            }
        }
        return null;
    }
    
    public String getPath() {
        return this.path;
    }

    private static String readFromFile(File file) throws Exception {
        String data = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            data += line + System.getProperty("line.separator");
        }
        br.close();
        return data;
    }

    public void setProgram(String program) {
        try {
            File source = new File(path, exercise.toString() + ".java");
            writeToFile(source, program);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.program = program;
    }

    // Use his methods or implement more readable and efficient methods below.
    // Implement max execution time.
    public void executeProgram() {
    }

    public void compileProgram() {
    }

    public void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
            setCompilePrint(compilePrint + "\n" + name + " " + line); //String to be sent to xhtml page (temp?)
        }
    }

    public String getLines(InputStream ins) throws Exception {
        String line;
        String output = "";
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            output += compilePrint + "\n" + line; //String to be sent to xhtml page (temp?)
        }
        return output;
    }

    public int runProcess(String... command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        output.output = getLines(pro.getInputStream());
        output.error = getLines(pro.getErrorStream());
        pro.waitFor();
        return pro.exitValue();
    }

    public int runWithInput(String input) throws IOException, Exception {
        int exitValue = 0; //this.compile();
        this.compile();

        if (exitValue == 0) {
            //Should create a file in your temp directory  
            File root = new File(WORKING_DIR + exercise.toString());
            root.createNewFile();
            // creates a FileWriter Object and writes to this file
            File forWriter = new File(root, exercise.toString() + "inputStreamFile");
            // Writes the content to the file
            try (FileWriter writer = new FileWriter(forWriter)) {
                // Writes the content to the file
                writer.write(input);
                writer.flush();
            }

//            System.out.println(WORKING_DIR);

            output = executeProgram("java",
                    exercise.toString(),
                    WORKING_DIR + exercise.toString(),
                    WORKING_DIR + exercise.toString() + File.separator + exercise.toString() + "inputStreamFile",
                    WORKING_DIR + exercise.toString() + File.separator + exercise.toString() + "output");

            // Return 0 if success otherwise if not
            //return runProcess("javac", forWriter.getAbsolutePath());
            setOutputString(output);
            return 0;
        } else {
            return 1;
        }
    }

    public void setOutputString(Output output) throws IOException {
        Scanner slate = new Scanner(new File(WORKING_DIR
                + exercise.toString()
                + File.separator
                + exercise.toString()
                + "output"));
        this.output.output = slate.useDelimiter("\\Z").next();
        slate.close();
    }

    public Output compile() {
        Output output = compileProgram("javac", path, exercise.toString() + ".java");
        return output;
    }
    
    public String run(File inputFile, File outputFile) {
        Output output = this.compile();
        String result = "command>javac " + exercise.toString() + System.getProperty("line.separator");
        if (output.error.isEmpty()) {
            result += "Compiled successful" + System.getProperty("line.separator") + System.getProperty("line.separator");
            result += "command>java " + exercise.toString() + System.getProperty("line.separator");
            return result + run(inputFile, outputFile, this.path);
        } else {
            return result + System.getProperty("line.separator") + output.error;
        }
    }

    public String run(File inputFile, File outputFile, String path) {
//        System.out.println(path);
            Output output = new Output();
            try {
                String inputPath = (inputFile != null) ? inputFile.getAbsolutePath(): null;
                String outputPath = outputFile.getAbsolutePath();
                output = executeProgram("java", exercise.toString(), path, inputPath, outputPath);
                // If infinite loop
                if (output.isInfiniteLoop) {
                    output.output = "Your program takes too long. "
                            + "It runs out of the allowed CPU time "+EXECUTION_TIME_ALLOWED+"ms. "
                            + "It may have an infinite loop or the expected input for the program is not provided or provided incorrectly.";
                } else {
                    output.output = readFromFile(outputFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output.output;
    }

    public String run(String input) {
        File inputFile = new File(path, exercise.toString() + ".input");
        File outputFile = new File(path, exercise.toString() + ".output");
        try {
            if (!input.isEmpty()) {
                Files.deleteIfExists(inputFile.toPath());
                inputFile.createNewFile();
                writeToFile(inputFile, input);
            } else {
                inputFile = null;
            }
            Files.deleteIfExists(outputFile.toPath());
            outputFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return run(inputFile, outputFile);
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
            @Override
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
//

    public static Output compileProgram(String command,
            String sourceDirectory, String program) {

        final Output result = new Output();

        ProcessBuilder pb;

        pb = new ProcessBuilder(command, "-classpath", sourceDirectory,
                "-Xlint:unchecked", "-nowarn", "-XDignore.symbol.file", program);

        pb.directory(new File(sourceDirectory));

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
            @Override
            public void run() {

                Scanner scanner1 = new Scanner(proc1.getInputStream());

                while (scanner1.hasNext()) {

                    result.output += scanner1.nextLine().replaceAll(" ", "&nbsp;") + System.getProperty("line.separator");

                    //  scanner1.close(); // You could have closed it too soon
                }

            }

        }.start();

        new Thread() {

            public void run() {

                // Process output from proc
                Scanner scanner2 = new Scanner(proc1.getErrorStream());

                while (scanner2.hasNext()) {

                    result.error += scanner2.nextLine() + System.getProperty("line.separator");

                }

                // scanner2.close(); // You could have closed it too soon
            }

        }.start();

        try {

            //Wait for the external process to finish
            int exitCode = proc.waitFor();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        result.output.replaceAll(" ", "&nbsp;");

        result.error.replaceAll(" ", "&nbsp;");

        // Ignore warnings
        if (result.error.indexOf("error") < 0) {

            result.error = "";

        }

//        if (result.error.indexOf("error") >= 0 || result.error.indexOf("Error") >= 0)
//          result.error = "";
        result.timeUsed = (int) (System.currentTimeMillis() - startTime);

        return result;

    }

    public String getCompilePrint() {
        return compilePrint;
    }

    public void setCompilePrint(String compilePrint) {
        this.compilePrint = compilePrint;
    }

    public Output getOutput() {
        return output;
    }

}
