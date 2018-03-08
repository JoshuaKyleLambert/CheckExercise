# CheckExercise
Java compiler and answer checker.

Team Members: Ayman Bagabas , Benson Tran , Joshua Lambert
Total Points (100 pts)      __________________
Due:  April 23 2018 before the class

Team Project: CheckExercise Tool

CSCI 5520 Rapid Java Application Development
Armstrong State University

This is a team project. Form a team of 2 or 3 students. But you have to write your own code.

Develop a CheckExercise Tool exactly like the one I have created:

https://liveexample.pearsoncmg.com/CheckExercise/faces/CheckExercise.xhtml


How to get all exercises? 
We will discuss it in details in the class.
The exercisedescription folder contains all exercise descriptions. 
Each file in the folder is for one exercise. If the file contains the text 

“This exercise can be compiled and submitted, but cannot be run and automatically graded.”, 

the exercise cannot be auto-graded.
Put exercisedescription in c:\ags10e\exercisedescription
Download it from www.cs.armstrong.edu/liang/ags10e.zip 


How to grade an exercise?
We will discuss it in details in the class.
The gradeexercise folder contains all exercise grading input and output files. 

Put gradeexercise in c:\ags10e\gradeexercise


How to get the initial input in the CheckExercise?
If an exercise has input, it must have a file name Exercise???a.input in the gradeexercise folder. Use the data from this file as the initial input.

How to get the correct output for an exercise?
The correct output is contained in a file in the exerciseworkarea folder named as Exercise???a.correctoutput, Exercise???b.correctoutput, etc, which corresponds to the input case a, b, etc. If a program does not take input, the correct output file is named Exercise??.correctoutput.
Download the correct output files from www.cs.armstrong.edu/liang/correctoutput.zip 

Where to get sample code to test your program?

Exercise01_01:
public class Exercise01_01 {
  public static void main(String[] args) {
    System.out.println("Welcome to Java");
    System.out.println("Welcome to Computer Science");
    System.out.println("Programming is fun");
  }
}

Exercise02_01:
import java.util.Scanner;

public class Exercise02_01 {
  // Main method
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    // Enter a temperature in Celsius
    System.out.print("Enter a temperature in Celsius: ");
    double celsius = input.nextDouble();

    // Convert it to Fahrenheit
    double fahrenheit = (9.0 / 5) * celsius + 32;

    // Display the result
    System.out.println(celsius + " Celsius is " +
      fahrenheit + " Fahrenheit");
  }
}


How to compile and run a program?
Discuss it in the class.




Develop the project using JSF. Name your URL:

http://localhost/CheckExercise/faces/CheckExercise.xhtml


What to submit?

A GitHub URL for your project.


We will have weekly meetings (every Thursday) in the class to give update of your progress and discuss the project.




Grading For the Team Project:

User interface

Choose chapter and exercise and set an exercise.

Compile/Run exercises

Grade exercises

Individual Contribution for the team project

Bonus: Code coloring (ace editor)

Class Presentation

