import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MathInductionITS extends PApplet {
    // Loads images
    PImage sample;
    // To improve efficiency, only draws objects when a change occurs instead of each instance
    boolean change;
    // Determines the modes listed below from 0-3
    int mode;
    // Text file that holds dialogue for the problem
    File problem;
    // Array list that holds the dialogue from the txt file
    ArrayList<String> dialogue;
    // ArrayList that holds the dialogue that is currently displayed on the screen
    ArrayList<String> subDialogue;
    ArrayList <PImage> images;
    // Current image the lesson is on
    int currentImage;
    // First and last line of dialogue to be displayed
    int start;
    int end;
    // Whether a question is being displayed
    boolean questionMode;
    boolean answerMode;
    boolean initialQuestion;
    boolean initialAnswer;
    // Display difficulties for student to select
    boolean displayDifficulty;
    // Checks to see if answers should be displayed
    boolean displayAnswers;
    // List the multiple choice answers
    ArrayList<String> answers;
    // Shuffled displayed answers
    ArrayList<String> displayedAnswers;
    ArrayList<ArrayList<String>> responses;
    // Answer student chooses
    String answerChosen;
    // Index of answer student chooses
    int answerNumber;
    String[] goodCompliment;
    String[] greatCompliment;
    String[] wrong;
    String[] close;
    String[] encourage;
    PFont f;
    // Students score for the session
    int score;
    ArrayList<String> prevText;
    // First iteration of draw
    boolean first=true;
    // Past responses true or not
    ArrayList<Boolean> studentRecord;
    // Current answer given

    // Individual question tracker
    int indiv;

    // Current level student is at (beginner, medium, advanced)
    int level;
    // Problem number
    int probNum;

    public void setup(){
        f=createFont("Arial",12,true);
    }
    public void settings() {
        // Initialize all settings
        smooth(1);
        size(1400,600);
        //sample=loadImage("problems\\prob1.1\\Slide9.PNG");
        mode=0;
        dialogue=new ArrayList<String>();
        subDialogue=new ArrayList<String>();
        change=true;
        start=0;
        end=1;
        images=new ArrayList<PImage>();
        currentImage=0;
        questionMode=false;
        answerMode=false;
        initialQuestion=false;
        initialAnswer=false;
        answers=new ArrayList<String>();
        displayedAnswers=new ArrayList<String>();
        responses=new ArrayList<ArrayList<String>>();
        displayAnswers=false;
        answerChosen="placeHolder";
        answerNumber=0;
        goodCompliment= new String[]{"Good.", "Correct.", "That's Right.", "Indeed."};
        greatCompliment=new String[]{"Great!","Excellent!","Well Done!","Perfect!"};
        wrong=new String[]{"Hmm, not exactly.", "Not quite, but you are learning.","Hmm, that's not quite right.",};
        close=new String[]{"Almost!","Not quite!","You almost had it!","Very close!"};
        encourage=new String[]{"Excellent, I knew you had it!","Good recovery.","Nicely Done!"};
        score=0;
        prevText=new ArrayList<String>();
        studentRecord=new ArrayList<Boolean>();
        indiv=0;
        displayDifficulty=false;
        level=0;
        probNum=1;
    }
    public void draw(){
        // Draw initial background
        if (first){
            background(255);
            first=false;
        }
        textFont(f,13);
        // Begin program

        /*
        There will be different modes this will be in. It will always start at initial.
        0-Initial:
            Begins the session with the student. This is where the student can choose a starting point
        1-Setup:
            The tutoring system will ask for the students name, how comfortable they are with math induction, and if they want a review of the basics
                If they want a review of the basics, they can load up the basics tutorial
                If they don't want a review, then they will be directed to an example problem
        2-Between Sessions:
            A mode that will set up the next problem for the ITS to go over with the student
        3-Session:
            The student's file will keep track of what problem they are currently on. If the score surpasses a certain point, they have the OPTION to move to a higher diffiulty.
                It is important that the student remains in control of their overall progress, but the ITJ will simply guide them
            This is where most of the chat happens, asking the student questions
            The questions will be tabulated in the end, and from there the ITJ can determine if the student can move on.

        Meta Symbols for dialogue:
        $0 Display difficulties
        $1 display question
        $2 compliment student (light)
        $3 compliment student (higher)
        $4 Tell student they are wrong
        $5 Tell student they are incorrect but have the right mindset
        $6 Compliment student for making a comeback after getting the previous question wrong
        $7 Display question again after student got it wrong
        $8 End session
         */
        // Setup for a new interaction with a student
        if (mode==0){
            dialogue.add("Welcome to the Tutoring System for Math Induction.");
            dialogue.add("I will be your tutor for this session.");
            dialogue.add("I look forward to assisting you.");
            dialogue.add("Now then, before we begin, Please select the difficulty you would like to start with to your left.");
            dialogue.add("$0");
            // Added this to avoid out of range error
            dialogue.add("temp");
            for (int i=0; i<dialogue.size(); i++){
                System.out.println(dialogue.get(i));
            }
            mode=1;
            //image(images.get(6),0,0,950,540);
        }
        // Asking initial questions
        else if (mode==1){
            displayText(subDialogue);
            if (displayDifficulty){
                textSize(20);
                // Beginner
                if (mouseX>325 && mouseX<625 && mouseY>140 && mouseY<260){
                    fill(0,150,0);
                    if (mousePressed){
                        level=1;
                        mode=2;
                        displayDifficulty=false;
                        dialogue.add("Sounds good! We will begin with a problem perfect for beginners.");
                    }
                }
                else{
                    fill(0,180,0);
                }
                rect(325,140,300,120);
                fill(255);
                text("Beginner",435,210);
                // Intermediate
                if (mouseX>325 && mouseX<625 && mouseY>280 && mouseY<400){
                    fill(180,180,0);
                    // Start on an advanced question, setting variables accordingly
                    if (mousePressed){
                        score=8;
                        level=2;
                        mode=2;
                        displayDifficulty=false;
                        dialogue.add("Alright! We will begin with a problem perfect for intermediates.");
                    }
                }
                else{
                    fill(200,200,0);
                }
                rect(325, 280, 300, 120);
                fill(255);
                text("Intermediate",425,350);
                // Advanced
                if (mouseX>325 && mouseX<625 && mouseY>420 && mouseY<540){
                    fill(170,0,0);
                    // Start on an advanced question, setting variables accordingly
                    if (mousePressed){
                        score=18;
                        level=3;
                        mode=2;
                        displayDifficulty=false;
                        dialogue.add("Alright! We will begin with a problem perfect for those very familiar with mathematical induction.");
                    }
                }
                else{
                    fill(200,0,0);
                }
                rect(325, 420, 300, 120);
                fill(255);
                text("Advanced",435,490);
            }
        }
        // Prepare the next question
        else if (mode==2){
            currentImage=0;
            dialogue.remove(end);
            images.clear();
            // Count number of images in directory
            String path="problems\\prob"+level+"."+probNum;
            File dir= new File(path);
            int slides=dir.list().length-1;
            System.out.println(slides);
            for (int i=0; i<slides; i++){
                images.add(loadImage("problems\\prob"+level+"."+probNum+"\\Slide"+(i+1)+".PNG"));
            }
            try {
                problem=new File("problems\\prob"+level+"."+probNum+"\\prob"+level+"."+probNum+".txt");
                Scanner in=new Scanner(problem);
                while (in.hasNextLine()) {
                    dialogue.add(in.nextLine());
                }
                in.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            // subDialogue.add(dialogue.get(start));
            mode=3;
        }
        // Going into a session with the student.
        else if (mode==3){
            // Draw buttons
            // Draw first image
            // Draw first text
            //mode=2;
            /*if (mousePressed) {
                update(0, 0);
            }*/
            strokeWeight(1);
            if (questionMode){
                if (initialQuestion){
                    initialQuestion=false;
                    // Remove NEXT
                    dialogue.remove(end);
                    do{
                        answers.add(dialogue.get(end));
                        dialogue.remove(end);
                    }while(!dialogue.get(end).equals("NEXT"));
                    // Add to correct answers
                    dialogue.remove(end);
                    boolean exit=false;
                    // Create Arraylist of Arraylist of responses
                    for (int i=0; i<answers.size(); i++){
                        ArrayList<String> temp=new ArrayList<String>();
                        do{
                            temp.add(dialogue.get(end));
                            dialogue.remove(end);
                        }while(!dialogue.get(end).equals("NEXT"));
                        dialogue.remove(end);
                        responses.add(temp);
                        /*if (dialogue.get(end).equals("END")){
                            break;
                        }*/
                    }
                    /*for (String i : answers){
                        System.out.println(i);
                    }
                    System.out.println();
                    for (ArrayList<String> response : responses){
                        for (String i : response){
                            System.out.println(i);
                        }
                        System.out.println();
                    }*/
                    // Displayed answers will be shuffled
                    displayedAnswers.addAll(answers);
                    Collections.shuffle(displayedAnswers);
                    /*for (String line: dialogue){
                        System.out.println(line);
                    }*/
                }
                if (questionMode){
                    // Set coordinates for text
                    int initX=980;
                    int initY=500;
                    stroke(0);
                    // See whether to display the current answers
                    if (mouseX>965 && mouseX<1265 && mouseY>485 && mouseY<505){
                        fill(0,0,200);
                        displayAnswers=true;
                        //System.out.println(mouseX);
                    }
                    else{
                        fill(255);
                        //System.out.println("idk y it isn't showing");
                        //System.out.println(mouseX);
                    }
                    textSize(15);
                    rect(965, 485, 400, 20);
                    fill(0);
                    text("Please select an answer", initX, initY);
                    int count=1;
                    if (!answerChosen.equals("placeHolder")){
                        answerMode=true;
                        questionMode=false;
                    }
                    else if (displayAnswers) {
                        for (String answer : displayedAnswers) {
                            if (mouseX>965 && mouseX<1265 && mouseY>485+20*count && mouseY<485+20+20*count) {
                                if (mousePressed){
                                    answerChosen=answer;
                                    System.out.println("Answer chosen: "+answerChosen);
                                    displayAnswers=false;
                                    for (int i=0; i<answers.size(); i++){
                                        if (answers.get(i).equals(answerChosen)){
                                            answerNumber=i;
                                            questionMode=false;
                                            initialAnswer=true;
                                            // Setting up for the answer response from the itj

                                            // Block out the displayed answers
                                            stroke(255);
                                            fill(255);
                                            rect(965,485,300,90);
                                            initialAnswer=false;
                                            // Add the new dialogue to the overall dialogue
                                            ArrayList<String> newText = new ArrayList<String>();
                                            newText.addAll(responses.get(answerNumber));
                                            newText.add(0, "Answer: "+answerChosen);
                                            // Check if there are any code and add the appropriate type of dialogue
                                            for (int j=newText.size()-1; j>=0; j--) {
                                                if (newText.get(j).equals("$2")){
                                                    currentImage++;
                                                }
                                                if (newText.get(j).equals("$3")){
                                                    if (indiv>0){
                                                        indiv=0;
                                                        dialogue.add(end,encourage[(int) random(0,goodCompliment.length-1)]);
                                                    }
                                                    else{
                                                        dialogue.add(end,goodCompliment[(int) random(0,goodCompliment.length-1)]);
                                                    }

                                                }
                                                else if (newText.get(j).equals("$4")){
                                                    if (indiv>0){
                                                        indiv=0;
                                                        dialogue.add(end,encourage[(int) random(0,goodCompliment.length-1)]);
                                                    }
                                                    else{
                                                        dialogue.add(end,greatCompliment[(int) random(0,goodCompliment.length-1)]);
                                                    }
                                                }
                                                else if (newText.get(j).equals("$5")){
                                                    dialogue.add(end,wrong[(int) random(0,goodCompliment.length-1)]);
                                                }
                                                else if (newText.get(j).equals("$6")){
                                                    dialogue.add(end,close[(int) random(0,goodCompliment.length-1)]);
                                                }
                                                // Makes sure it's not used for an image change
                                                else if (!newText.get(j).equals("$2")){
                                                    dialogue.add(end,newText.get(j));
                                                    //System.out.println("Added text: "+newText.get(i));
                                                }
                                            }
                                            // Reset if correct and moves on
                                            if (answerNumber==0){
                                                studentRecord.add(true);
                                                score++;
                                                answers.clear();
                                                displayedAnswers.clear();
                                                responses.clear();
                                            }
                                            // Simply removes option if incorrect
                                            else{
                                                indiv++;
                                                studentRecord.add(false);
                                                score--;
                                                answers.remove(answerNumber);
                                                responses.remove(answerNumber);
                                                displayedAnswers.clear();
                                                displayedAnswers.addAll(answers);
                                                Collections.shuffle(displayedAnswers);
                                            }
                                            answerMode=false;
                                            questionMode=false;
                                            answerChosen="placeHolder";
                                            // For testing
                                            /*for (String line: dialogue){
                                                System.out.println(line);
                                            }*/
                                            //update(mouseX,mouseY);
                                            break;
                                        }
                                    }
                                    break;
                                }
                                fill(0, 0, 190);
                            }
                            else{
                                fill(255);
                            }
                            rect(965, 485 + 20 * count, 400, 20);
                            fill(0);
                            text(answer, initX, initY + 20 * count);
                            count++;
                        }
                    }
                }
                displayText(subDialogue);
                //image(images.get(currentImage), 0, 20, 950, 540);
                //drawOutline();
            }
            else if (answerMode){
                //System.out.println("here");
                displayText(subDialogue);
                //image(images.get(currentImage), 0, 20, 950, 540);
                //drawOutline();
            }
            else {
                // Display text and current image as usual
                displayText(subDialogue);
                //image(images.get(currentImage), 0, 20, 950, 540);
                //drawOutline();
            }
        }
    }
    public void drawOutline(){
        // Draw rectangular outline with headers
        strokeWeight(3);
        noFill();
        rect(1, 1, 1396, 597);
        //rect(900,0,10,1000);
        line(950, 0, 950, 800);
        line(950,450,1395,450);
        line(0,30,1400,30);
        textSize(22);
        fill(255);
        noStroke();
        rect(397,6,175,21);
        fill(0);
        stroke(0);
        text("Problem Solution",400,25);
        fill(255);
        stroke(255);
        stroke(0);
        fill(0);
        text("Tutoring Session",1100,25);
        strokeWeight(1);
        textSize(20);
        if (level>0) {
            fill(255);
            stroke(255);
            rect(8,573,200,20);
            fill(0);
            stroke(0);
            text("Difficulty:", 10, 590);
            if (level==1){
                fill(0,200,0);
                text("Beginner", 93, 590);
            }
            if (level==2){
                fill(200,200,0);
                text("Intermediate", 93, 590);
            }
            if (level==3){
                fill(200,0,0);
                text("Advanced", 93, 590);
            }
            fill(0);
        }
        /*fill(255);
        stroke(255);
        rect(900,400,10,195);
        stroke(0);*/
    }
    public void displayText(ArrayList<String> text){
        if (!questionMode) {
            fill(255);
            rect(950, 3, 1395, 955);
            noFill();
            String output="";
            // Display appropriate dialogue
            textSize(15);
            int numAnswers=0;
            int space=0;
            for (String t : text){
                if (t.length()>6) {
                    if (t.substring(0,6).equals("Answer")) {
                        numAnswers++;
                    }
                }
            }
            for (String t : text){
                fill(0);
                if (t.length()>6) {
                    if (t.substring(0,6).equals("Answer")) {
                        if (studentRecord.get(studentRecord.size()-numAnswers)) {
                            fill(0, 170, 0);
                        }
                        else {
                            fill(200, 0, 0);
                        }
                        numAnswers--;
                    }
                }
                text(t,1000,50+space*70,380,100);
                space++;
                //output+="\n\n"+t;
            }
            //System.out.println(output);
            fill(0);
            //textSize(15);
            //text(output, 1000, 0, 380, 550);
            prevText=text;
            /*System.out.println("Previous: "+prevText.get(0));
            System.out.println("Current: "+text.get(0));*/
            // Display Image and redraw outline
            if (mode==3) {
                image(images.get(currentImage), 0, 20, 950, 540);
            }
            drawOutline();
        }

    }
    public void mouseClicked(){
        // For easy tracking
        println(mouseX,mouseY);
        if (mode==3 || mode==1){
            update(mouseX,mouseY);
        }
    }
    public void update(int x, int y){
        //System.out.println("Current end: "+dialogue.get(end));
        // Asking for difficulty
        if (dialogue.get(end).equals("$0")){
            dialogue.remove(end);
            displayDifficulty=true;
        }
        // Set up for a questions
        if (dialogue.get(end).equals("$1") && !(questionMode || answerMode)){
            //System.out.println("question");
            // get question ready for the student to answer
            questionMode=true;
            initialQuestion=true;
        }
        // Return to question as the student got it wrong
        else if (dialogue.get(end).equals("$7")){
            dialogue.remove(end);
            //System.out.println("Going back to question");
            questionMode=true;
        }
        else if (!(questionMode || displayDifficulty)){
            // End of the session. Tell's the student where they currently stand
            if (dialogue.get(end).equals("$8") || dialogue.get(end).equals("")){
                boolean next=false;
                if (dialogue.get(end).equals("$8")){
                    next=true;
                }
                // Move onto the next question
                mode=2;
                dialogue.remove(end);
                // Student performed exceptionally and/or is ready to move to the next level
                if (next) {
                    if (score > level * 10) {
                        dialogue.add("It seems you have an amazing grasp on the basics of mathematical induction.");
                        dialogue.add("Well done! Let's move onto the next level of difficulty with some intermediate problems.");
                        dialogue.add("Click when you are ready to begin the next problem.");
                    /*dialogue.add("If you would like, we can move on to the next level of difficulty.");
                    dialogue.add("Keep in mind, this is optional, and only move on if you feel comfortable with the material.");
                    */
                        if (level < 3) {
                            level++;
                        }
                        probNum = 1;
                        // Set score to match new level
                        score = level * 10;
                    }
                    // Student performed adequately, but needs more time on this level
                    else if (score > (level - 1) * 10 + 4) {
                        dialogue.add("You seem to have a grasp on some of the concepts of this level math induction");
                        dialogue.add("However, I recommend we go through a few more examples together before we move on to the next level.");
                        dialogue.add("You are off to a good start!");
                        dialogue.add("However, I would like to make sure you are well-prepared before trying the next level of problems.");
                        dialogue.add("Let's move on to another question.");
                        // Stay in level, go to next problem
                        probNum++;
                        // For testing so there is not a null error
                        if (probNum == 4) {
                            probNum = 1;
                        }
                        // Gives student a "head start" since they seem to grasp the material somewhat
                        score = (level - 1) * 10 + probNum * 2;
                    }
                    // Student performed very poorly and going down a level
                    else if (score < (level - 1) * 10 && level > 1) {
                        dialogue.add("It seems this level of difficulty may be too difficult for you.");
                        dialogue.add("There is no need to worry!");
                        dialogue.add("This is a difficult topic and we will get through these questions together.");
                        dialogue.add("Let's try going down a level for now.");
                        dialogue.add("Once we ace the more basic skills, we will come back to this question.");
                        dialogue.add("Let's move onto an easier example.");
                        // Start student from beginning of new level
                        level--;
                        score = (level - 1) * 10;
                    }
                    // Student performed poorly
                    else {
                        dialogue.add("It seems this level of mathematical induction and its concepts are still a bit fresh.");
                        dialogue.add("There is no need to worry!");
                        dialogue.add("This is a difficult topic and we will get through these questions together.");
                        dialogue.add("Let's move on to another question.");
                        // Stay in level, go to next problem
                        probNum++;
                        // For testing so there is not a null error
                        if (probNum == 4) {
                            probNum = 1;
                        }
                        // Starts student fresh on this level
                        score = (level - 1) * 10;
                    }
                    println(level, probNum, score);
                    next=false;
                }
            }
            change=true;
            if (end>5){
                start++;
            }
            end++;
            subDialogue.clear();
            for (int i=start; i<end; i++){
                if (dialogue.get(i).equals("$2")){
                    currentImage++;
                    dialogue.remove(i);
                }
                subDialogue.add(dialogue.get(i));
            }
        }

    }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "MathInductionITS" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        }
        else {
            PApplet.main(appletArgs);
        }
    }
}