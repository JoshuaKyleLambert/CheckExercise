/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author ayman
 */
public class Exercise {
    private final Integer chapter;
    private final Integer exercise;
    private final Boolean isExtra;
    private String description;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExercise() {
        return exercise;
    }
    
    @Override
    public String toString() {
        String extra = (isExtra) ? "Extra": "";
        return String.format("Exercise%02d_%02d%s", chapter, exercise, extra);
    }
}
