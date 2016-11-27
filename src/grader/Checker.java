package grader;

import grader.scoring.ExecutionResult;

import java.io.*;

/**
 * Created by ElBatanony on 24-Nov-16.
 */
public class Checker {

    public int compile( String fileName ) throws IOException, InterruptedException {

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

    public ExecutionResult execute(String exeFile) throws IOException, InterruptedException {

        //String exeFile = "assets/" + fileName + ".exe"; // the exe file
        //exeFile = "assets/" + exeFile + ".exe";
        Runtime rt = Runtime.getRuntime();

        int timeLimit = 2000; // milliseconds get from config of problem

        Process pr = rt.exec(exeFile); // run code
        // time and memory limit

        tlkill(pr,timeLimit); // kill if exceeded time limit

        int exValue = -1;
        if ( !pr.isAlive() ) exValue = pr.exitValue(); // check if exe exited correctly

        if ( exValue == -1 || exValue == 1 ) {
            pr("Time Limit Exceeded!");
            return ExecutionResult.TLE; // return 1 so we do not compare output
        } //else pr( "exit value : " + Integer.toString( exValue ) );

        return ExecutionResult.None;
    }

    public ExecutionResult compare(String outputFile, String judgeOutputFile) throws IOException {

        FileInputStream in = null;
        FileInputStream jin = null;

        try {

            // for testing
            outputFile = "output.txt"; judgeOutputFile = "judgeOut.txt";

            in = new FileInputStream(outputFile);
            jin = new FileInputStream(judgeOutputFile);
            BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
            BufferedReader brj = new BufferedReader( new InputStreamReader( jin ) );

            String l,l2;
            while ( ( l = brj.readLine() ) != null ) {

                l2 = br.readLine();
                if ( l2 == null ) { pr("Wrong Answer!"); return ExecutionResult.WA; }

                l = l.trim(); l2 = l2.trim();

                if ( !l.equals(l2) ) { pr("Wrong Answer!"); return ExecutionResult.WA; }

            }

            if ( ( l2 = br.readLine() ) != null ) { pr("Wrong Answer!"); return ExecutionResult.WA; }

            pr("Accepted!"); return ExecutionResult.AC;

        }finally {
            if (in != null) in.close();
            if (jin != null) jin.close();
        }
    }

    public static void pr(String x) {
        System.out.println(x);
    } // for convenience :D

    private void caller() throws IOException, InterruptedException {
        String fileName = "testCode"; // get code name from user interface
        if ( compile(fileName) == 0 ) {
            if (execute(fileName) == ExecutionResult.None) // did not give us a TLE (TLE = 1)
                compare("",""); // check output bit by bit
        } else {
            pr("Compilation Error!");
        }
    }

    public static void main(String []args) throws IOException, InterruptedException {

        Checker ch = new Checker(); // gets instance of checker class

        // using sync for the wait command
        synchronized ( ch ) {
            ch.caller(); // begin grading
        }

        System.out.println("Done!"); // YAY :D
    }

}
