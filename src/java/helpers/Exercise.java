/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author ayman
 */
public class Exercise {
    private final Integer chapter;
    private final Integer exercise;
    private final Boolean isExtra;
    private String description;
    private File[] inputFiles;
    private File[] outputFiles;
    
    public Exercise(int chapter, int exercise) {
        this.chapter = chapter;
        this.exercise = exercise;
        this.isExtra = false;
    }
    
    public Exercise(int chapter, int exercise, boolean isExtra) {
        this.chapter = chapter;
        this.exercise = exercise;
        this.isExtra = isExtra;
    }
    
    public Compile getCompile() {
        return new Compile(this);
    }
    
    private File[] getIOFiles(String path, String ext) throws IOException {
        Stream<File> files = Utils.getFilesWalk(path)
                .filter(p -> p.getName().contains(this.toString()))
                .filter(p -> p.getName().endsWith(ext));
        if (this.isExtra()) {
            files = files.filter(p -> p.getName().contains("Extra"));
        } else {
            files = files.filter(p -> !p.getName().contains("Extra"));
        }
        List<File> list = files.collect(Collectors.toList());
        return list.toArray(new File[list.size()]);
    }
    
    public void setIOFiles(String path) throws IOException {
        this.setInputFiles(getIOFiles(path, "input"));
        this.setOutputFiles(getIOFiles(path, "output"));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExercise() {
        return exercise;
    }

    public File[] getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(File[] input) {
        this.inputFiles = input;
    }

    public File[] getOutputFiles() {
        return outputFiles;
    }

    public void setOutputFiles(File[] output) {
        this.outputFiles = output;
    }

    public Boolean isExtra() {
        return isExtra;
    }
    
    @Override
    public String toString() {
        String extra = (isExtra) ? "Extra": "";
        return String.format("Exercise%02d_%02d%s", chapter, exercise, extra);
    }
}
