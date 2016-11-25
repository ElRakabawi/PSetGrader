package grader;

import java.io.*;

/**
 * Created by ElBatanony on 24-Nov-16.
 */
public class Checker {

    private int compile( String fileName ) throws IOException, InterruptedException {

        String cppFile = fileName+".cpp"; // for c++ code
        String exeFile = "assets/" + fileName + ".exe"; // the exe file

        Runtime rt = Runtime.getRuntime();
        String compileCommand = "g++ " + cppFile + " -o " + exeFile;
        Process pr = rt.exec(compileCommand); // compile code into exe
        pr.waitFor();

        return pr.exitValue();
    }

    private void tlkill(Process pr, int timeLimit) throws InterruptedException {
        wait(timeLimit);
        if (pr.isAlive()) pr.destroy();
    }

    private int execute(String fileName) throws IOException, InterruptedException {

        String exeFile = "assets/" + fileName + ".exe"; // the exe file
        Runtime rt = Runtime.getRuntime();

        int timeLimit = 2000; // milliseconds get from config of problem

        Process pr = rt.exec(exeFile); // run code
        // time and memory limit

        tlkill(pr,timeLimit); // kill if exceeded time limit

        int exValue = -1;
        if ( !pr.isAlive() ) exValue = pr.exitValue(); // check if exe exited correctly

        if ( exValue == -1 || exValue == 1 ) {
            pr("Time Limit Exceeded!");
            return 1; // return 1 so we do not compare output
        } //else pr( "exit value : " + Integer.toString( exValue ) );

        return  0;
    }

    private void compare(String fileName) throws IOException {

        FileInputStream in = null;
        FileInputStream jin = null;

        try {

            in = new FileInputStream("output.txt");
            jin = new FileInputStream("judgeOut.txt");

            int c,c2;
            while ( (c = in.read()) != -1) {

                c2 = jin.read();

                if (c2 == -1) {
                    System.out.println("WA! Answer too long!");
                    return;
                }

                if ( c != c2 ) {
                    System.out.println("Difference at " + (char)(c) + " is " + (char)(c2) );
                    System.out.println("Wrong Answer!");
                    return;
                }

            }

            if ( jin.available() > 0 ) {
                System.out.println("WA! Answer incomplete!");
                return;
            }

            System.out.println("Accepted!");

        }finally {
            if (in != null) in.close();
            if (jin != null) jin.close();
        }
    }

    private static void pr(String x) {
        System.out.println(x);
    } // for convenience :D

    private void caller() throws IOException, InterruptedException {
        String fileName = "testCode"; // get code name from user interface
        if ( compile(fileName) == 0 ) {
            if (execute(fileName) != 1) // did not give us a TLE (TLE = 1)
                compare(fileName); // check output bit by bit
        } else {
            pr("Compilation Error!");
        }
    }


    public static void main(String []args) throws IOException, InterruptedException {

        Checker ch = new Checker(); // gets instance of checker class

        // using sync for the wait command
        synchronized ( ch ) {
            ch.caller(); // begging grading
        }

        System.out.println("Done!"); // YAY :D
    }

}