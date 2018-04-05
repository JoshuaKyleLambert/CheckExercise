package CheckExercise;

import helpers.*;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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
    private String programStyle;
    private String program = "/* Paste your Exercise01_01 here and click Automatic Check.\n"
            + "For all programming projects, the numbers should be double \n"
            + "unless it is explicitly stated as integer.\n"
            + "If you get a java.util.InputMismatchException error, check if \n"
            + "your code used input.readInt(), but it should be input.readDouble().\n"
            + "For integers, use int unless it is explicitly stated as long. */";
    private String inputPrompt = "Enter input data for the program (Sample data provided below. You may modify it.)";
    private Boolean sampleDataProvided = false;
    private String input = "";
    private Boolean gradable = true;
    private Boolean autoCheck = true; //Change to true when doing automatic check
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
        setProgram("/* Paste your " + getExercise() + " here and click Automatic Check.\n"
                + "For all programming projects, the numbers should be double \n"
                + "unless it is explicitly stated as integer.\n"
                + "If you get a java.util.InputMismatchException error, check if \n"
                + "your code used input.readInt(), but it should be input.readDouble().\n"
                + "For integers, use int unless it is explicitly stated as long. */");

        setHeader("Welcome to " + getExercise() + " Program Checker");
        //Command text that appears before output
        setOutputCommandText("command>javac " + getExercise() + ".java \nCompiled successfully\n\n" + "command>java "+ getExercise() + "\n");
        
        // Initialize IO Files for the exercise
        try{
            getExercise().setIOFiles(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF") + File.separator + "gradeexercise");
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

    }

    public void run() {

        setAutoCheck(false); //Lets editor for result to display
        Compile compiler = new Compile(this.exercise, getProgram());
        try {
            if (compiler.runWithInput(getInput()) == 0) {
                setResult(outputCommandText + compiler.getOutput().output);
                System.out.println(compiler.getOutput().output);
            } else {
                setResult(compiler.getOutput().error);
            }
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
        return program;
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
