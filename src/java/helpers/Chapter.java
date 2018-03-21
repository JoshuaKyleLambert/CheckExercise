/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ayman
 */
public class Chapter {

    private final Integer chapter;
    private final Exercise[] exercises;

    public Chapter(int chapter, String[] exercises) {
        this.chapter = chapter;
        this.exercises = new Exercise[exercises.length];
        Pattern p = Pattern.compile("(\\d{2})(Extra)?");
        Matcher m;
        for (int i = 0; i < exercises.length; i++) {
            m = p.matcher(exercises[i]);
            if (m.matches()) {
                int exercise = Integer.parseInt(m.group(1));
                // If regex couldn't find "Extra" in the string then it's not Extra
                // m.group(2) extracts the word "Extra" from the String
                // if that is not null, then it is not Extra
                this.exercises[i] = new Exercise(chapter, exercise, (m.group(2) != null));
                this.exercises[i].setDescription(readDescription(Utils.descriptionsPath+"/"+this.exercises[i]));
            }
        }
    }

    private String readDescription(String pathToFile) {
        File file = new File(pathToFile);
        if (file.isFile() && file.canRead()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int)file.length()];
                fis.read(data);
                fis.close();
                return new String(data, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Exercise[] getExercises() {
        return exercises;
    }

    public int getChapter() {
        return chapter;
    }

    @Override
    public String toString() {
        return String.format("Chapter %d", chapter);
    }
}
