package grader.scoring;

import grader.Checker;
import grader.models.ProblemModel;
import grader.models.SubTaskModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basim on 27/11/2016.
 */
public class ExecutorCode {

    /**
     * Computes the result of running the program on the
     * sub-task specified
     *
     * @param subTask The sub-task description
     * @param exeFile The file containing the executable to run
     * @return The result: TLE, WA, AC or MLE
     */
    private static ExecutionResult gradeSubTask(SubTaskModel subTask, String exeFile) throws IOException, InterruptedException {

        Checker ch = new Checker();

        // we either need to place the input file in the same directory as exe
        // or if it is standard input, we can feed it in to the program
        int outIndex = 0;
        String judgeOutput;
        String userOutput = "output.txt"; // for testing
        ExecutionResult er;

        synchronized ( ch ) {

            for (String input : subTask.inputFiles) {
                judgeOutput = subTask.outputFiles.get(outIndex++);

                er = ch.execute(exeFile);

                if (er != er.None) return er;

                er = ch.compare(userOutput, judgeOutput);

                if ( er != ExecutionResult.AC ) return  er;

            }
        }

        return ExecutionResult.AC;
    }

    /**
     * Compile the given C/C++ source code and return the
     * exe file path
     *
     * @param programFile The program to compile
     * @return The exe file or the empty string "" in case of compile error
     */

    /**
     * Computes the result of running this program on every single
     * sub-task specified in the problem given.
     *
     * @param problem
     * @param programFile
     * @return The list of execution results or NULL in-case of compile error
     */
    public static List<ExecutionResult> gradeProgram(ProblemModel problem, String programFile) throws IOException, InterruptedException {

        Checker ch = new Checker();
        String exeFile = "";

        synchronized ( ch ) {

            if (ch.compile(programFile) == 0) {
                ch.pr("Compilation Successful!"); // optional
                exeFile = "assets/" + programFile + ".exe";
            } else {
                ch.pr("Compilation Error!");
                return null;
            }

        }

        List<ExecutionResult> results = new ArrayList<>();

        // Grade each sub-task by calling the function above
        for (SubTaskModel subTask: problem.subTasks) {
            results.add(gradeSubTask(subTask, exeFile));
        }

        return results;
    }
}
