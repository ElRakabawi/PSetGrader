package grader.scoring;

import grader.models.ProblemModel;
import grader.models.SubTaskModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basim on 27/11/2016.
 */
public class Executor {

    /**
     * Computes the result of running the program on the
     * sub-task specified
     *
     * @param subTask The sub-task description
     * @param programFile The file containing the executable to run
     * @return The result: TLE, WA, AC or MLE
     */
    private static ExecutionResult gradeSubTask(SubTaskModel subTask, String exeFile) {

        // TODO: Complete grading of sub task

        return ExecutionResult.AC;
    }

    /**
     * Compile the given C/C++ source code and return the
     * exe file path
     *
     * @param programFile The program to compile
     * @return The exe file or the empty string "" in case of compile error
     */
    private static String compileProgram(String programFile) {

        // TODO: Complete compiliation

        return ""; // Signifies compile error
    }

    /**
     * Computes the result of running this program on every single
     * sub-task specified in the problem given.
     *
     * @param problem
     * @param programFile
     * @return The list of execution results or NULL in-case of compile error
     */
    public static List<ExecutionResult> gradeProgram(ProblemModel problem, String programFile) {

        String exeFile = compileProgram(programFile);

        // Check for compile error
        if (exeFile == "") {
            return null; // Signify that there was a compile error
        }

        List<ExecutionResult> results = new ArrayList<>();

        // Grade each sub-task by calling the function above
        for (SubTaskModel subTask: problem.subTasks) {
            results.add(gradeSubTask(subTask, exeFile));
        }

        return results;
    }
}
