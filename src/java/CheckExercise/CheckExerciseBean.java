/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckExercise;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Joshu
 */
@Named(value = "checkExerciseBean")
@SessionScoped
public class CheckExerciseBean implements Serializable {

    /**
     * Creates a new instance of CheckExerciseBean
     */
    public CheckExerciseBean() {
    }
    
}
