/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author ayman
 */
public class Utils {
    
    public static final String descriptionsPath = "exercisedescription"; // Directiory name within the project directiory
    public static final String gradeexercise =  "gradeexercise";

    public static File[] getFiles(String path) {
        File currentDirectory = new File(path);
        File[] filesList = currentDirectory.listFiles();
        Arrays.sort(filesList);
        return filesList;
    }
    
    // Since we have a description for each exercise, we can determine how many
    // exercises for each chapter using the file name of each description file.
    public static Chapter[] populateChapters(String pathToDir) {
        ArrayList<Chapter> list = new ArrayList<>();
        File[] descriptionFiles = getFiles(pathToDir);
        Pattern p = Pattern.compile("Exercise(\\d{2})_(\\d{2}(Extra)?)"); // Group 1 contains the chapter, group 2 contains the exercise
        Matcher m;
        int workingChapter = 1;
        ArrayList<String> exercisesInAChapter = new ArrayList<>();
        for(File f: descriptionFiles) {
            m = p.matcher(f.getName());
            String chapter, exercise;
            if (m.matches()) {
                chapter = m.group(1);
                exercise = m.group(2);
                if (workingChapter != Integer.parseInt(chapter)) {
                    list.add(new Chapter(workingChapter, exercisesInAChapter.toArray(new String[exercisesInAChapter.size()])));
                    exercisesInAChapter = new ArrayList<>();
                    workingChapter = Integer.parseInt(chapter);
                }
                exercisesInAChapter.add(exercise);
            }
            
        }
        return list.toArray(new Chapter[list.size()]);
    }
    
    public static Stream<File> getFilesWalk(String path) throws IOException {
        Stream<File> files = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toFile);
        return files;
    }

    // This function is only for testing purposes and it should not be used and it should be always commented
//    public static void main(String[] args) {
//    }
}
