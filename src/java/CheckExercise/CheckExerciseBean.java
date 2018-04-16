package CheckExercise;

import helpers.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author Joshua Lambert, Ayman Bagabas, Benson Tran
 */
@Named(value = "checkExerciseBean")
@SessionScoped
public class CheckExerciseBean implements Serializable {

    private String header = "Welcome to Exercise01_01 Program Checker";
    private Boolean chapterSelectable = true;
    private String chapter;
    private Chapter[] chapters;
    private String programName;
    private Exercise exercise;
    private Exercise[] exercises;
    private String programStyle = "width:575px; font-weight: normal; margin-top: 1px;height:177px; background-color: white; border: 1px solid #f6912f; font-size: 96%;height:257px;";
    private String program;
    private String inputPrompt = "Enter input data for the program (Sample data provided below. You may modify it.)";
    private Boolean sampleDataProvided = false;
    private String input = "";
    private Boolean gradable = true;
    private Boolean autoCheck; //Change to true when doing automatic check
    private String resultStyle;
    private String gradeResult = "We recommend that you use this tool to test"
            + " the code. If your code is wrong, the tool will display your"
            + " output and the correct output so to help you debug the error. "
            + "Compile/Run is provided for your convenience to compile and run"
            + " the code. The extra exercises are available for instructors."
            + " Email y.daniel.liang@gmail.com to request a copy of the extra"
            + " exercises.";
    private String status = "Status Message goes here";
    private String result;
    private String outputCommandText;

    @PostConstruct
    public void init() {
        this.chapters = Utils.populateChapters(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF") + "/" + Utils.descriptionsPath);
        this.chapter = (chapters[0] != null) ? chapters[0].toString() : "";
        this.exercises = chapters[0].getExercises();
        this.exercise = getExercises()[0];
        this.programName = (chapters[0] != null && getExercise() != null) ? getExercise().toString() : "";
        this.program = "/* Paste your "+this.programName+" here and click Automatic Check.\n"
            + "For all programming projects, the numbers should be double \n"
            + "unless it is explicitly stated as integer.\n"
            + "If you get a java.util.InputMismatchException error, check if \n"
            + "your code used input.readInt(), but it should be input.readDouble().\n"
            + "For integers, use int unless it is explicitly stated as long. */";
        this.autoCheck = true;
        loadExercise();
    }

    /**
     * Creates a new instance of CheckExerciseBean
     */
    public CheckExerciseBean() {
    }

    /**
     * Actions and Listeners
     *
     */
    public void changeChapter() {
        int chapterNo = Integer.parseInt(chapter.substring(chapter.lastIndexOf(" ") + 1));
        setExercises(chapters[chapterNo - 1].getExercises());
    }

    public void loadExercise() {
        chooseExercise();
        setResult("");          // Reset resutl
        setAutoCheck(true);     // Hide output box
        setProgram("/* Paste your " + getExercise() + " here and click Automatic Check.\n"
                + "For all programming projects, the numbers should be double \n"
                + "unless it is explicitly stated as integer.\n"
                + "If you get a java.util.InputMismatchException error, check if \n"
                + "your code used input.readInt(), but it should be input.readDouble().\n"
                + "For integers, use int unless it is explicitly stated as long. */");

        setHeader("Welcome to " + getExercise() + " Program Checker");
        
        // Initialize IO Files for the exercise
        try{
            File path = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF"), Utils.gradeexercise);
            getExercise().setIOFiles(path.getAbsolutePath());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        if(getExercise().getInputFiles().length > 0) {
            setSampleDataProvided(true);
            //Display the input string into the input
            setInput(getExercise().getFirstInputFile());
        }
        else{
            setSampleDataProvided(false);
        }
        
    }

    public void chooseExercise() {
        for (Exercise e : getExercises()) {
            if (programName.equals(e.toString())) {
                setExercise(e);
                return;
            }
        }
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    // Automatic check
    public void grade() {
        
                Compile c = new Compile(exercise, program);
//                Compile c2 = new Compile(exercise);
        for(int i = 0; i < exercise.getInputFiles().length; i++) {
            File in = exercise.getInputFiles()[i];
//            File out = exercise.getOutputFiles()[i];
            File out1 = new File(Compile.TEMP_PATH + File.separator + "CheckExercise", "output1.output");
            File out2 = new File(Compile.TEMP_PATH + File.separator + "CheckExercise", "output2.output");
            File workarea = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF"), Utils.exerciseworkarea);
            System.out.println(out2.getAbsolutePath());
            try {
//                BufferedReader br = new BufferedReader(new FileReader(in));
                
                String output1 = c.run(in, out1);
                String output2 = c.run(in, out2, workarea.getAbsolutePath());
                System.out.println(output1);
                System.out.println(output2);
                if (!output1.equals(output2)) {
                    int iterator = 0;
                    while(output1.charAt(iterator) == output2.charAt(iterator)) iterator++;
                    StringBuilder userHighlightedOutput = new StringBuilder(output1);
                    StringBuilder HisHighlightedOutput = new StringBuilder(output2);
                    String replaceUser = "<span style=\"background-color=red;\">" + output1.charAt(iterator) + "</span>";
                    String replaceHis = "<span style=\"background-color=red;\">" + output2.charAt(iterator) + "</span>";
                    userHighlightedOutput.replace(iterator, iterator+1, replaceUser);
                    HisHighlightedOutput.replace(iterator, iterator+1, replaceHis);
                    setGradeResult(userHighlightedOutput + "\n\n" + HisHighlightedOutput);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        c.cleanUp();
        setGradeResult("Correct!");

    }

    public void run() {

        setAutoCheck(false); //Lets editor for result to display
        Compile compiler = new Compile(this.exercise, getProgram());
        try {
            setResult(compiler.run(getInput()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compiler.cleanUp();
        }
    }

    public void checkURL() {

    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Chapter[] getChapters() {
        return chapters;
    }

    public void setChapters(Chapter[] chapters) {
        this.chapters = chapters;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Exercise[] getExercises() {
        return exercises;
    }

    public void setExercises(Exercise[] exercises) {
        this.exercises = exercises;
    }

    public String getProgramStyle() {
        return programStyle;
    }

    public void setProgramStyle(String programStyle) {
        this.programStyle = programStyle;
    }

    public String getProgram() {
        return (program != null) ? program: "";
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getInputPrompt() {
        return inputPrompt;
    }

    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    public Boolean isSampleDataProvided() {
        return sampleDataProvided;
    }

    public void setSampleDataProvided(Boolean sampleDataProvided) {
        this.sampleDataProvided = sampleDataProvided;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Boolean isGradable() {
        return gradable;
    }

    public void setGradable(Boolean gradable) {
        this.gradable = gradable;
    }

    public Boolean isAutoCheck() {
        return autoCheck;
    }

    public void setAutoCheck(Boolean autoCheck) {
        this.autoCheck = autoCheck;
    }

    public String getResultStyle() {
        return resultStyle;
    }

    public void setResultStyle(String resultStyle) {
        this.resultStyle = resultStyle;
    }

    public String getGradeResult() {
        return gradeResult;
    }

    public void setGradeResult(String gradeResult) {
        this.gradeResult = gradeResult;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Boolean isChapterSelectable() {
        return chapterSelectable;
    }

    public void setChapterSelectable(Boolean chapterSelectable) {
        this.chapterSelectable = chapterSelectable;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
    
    public String getOutputCommandText() {
        return outputCommandText;
    }

    public void setOutputCommandText(String outputCommandText) {
        this.outputCommandText = outputCommandText;
    }

}
