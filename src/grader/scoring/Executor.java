package grader.scoring;

import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.OsProcess;
import com.jezhumble.javasysmon.ProcessInfo;
import com.jezhumble.javasysmon.ProcessVisitor;
import grader.models.ProblemModel;
import grader.models.SubTaskModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Basim on 27/11/2016.
 */
public class Executor {


    /**
     * Compare whether the two streams produce identical results
     *
     * @param programOutput The stream for the program
     * @param expectedOutput The stream for the expected output
     * @return
     * @throws IOException
     */
    protected static boolean compareResult(Reader programOutput, Reader expectedOutput) throws IOException {
        BufferedReader programReader = new BufferedReader(programOutput);
        BufferedReader expectedReader = new BufferedReader(expectedOutput);

        boolean same = true;

        // While both have characters read lines and compare for equality
        while (programReader.ready() && expectedReader.ready()) {

            // Trim leading and trailing whitespace
            String out = programReader.readLine().trim();
            String expected = expectedReader.readLine().trim();

            if (!out.equals(expected)) {
                same = false;
                break;
            }
        }

        // If either stream has more values left then set it to false
        if (same && (programReader.ready() || expectedReader.ready()) )  {
            same = false;
        }

        programReader.close();
        expectedReader.close();

        return same;
    }

    /**
     * Computes the result of running the program on the
     * sub-task specified
     *
     * @param subTask The sub-task description
     * @param exeFile The file containing the executable to run
     * @return The result: TLE, WA, AC or MLE
     */
    protected static List<ExecutionResult> gradeSubTask(SubTaskModel subTask, String exeFile) throws IOException, InterruptedException {


        JavaSysMon mon = new JavaSysMon();
        Set<OsProcess> before = new HashSet<>(), after  = new HashSet<>();

        ProcessVisitor beforeVisitor = (osProcess, i) -> { before.add(osProcess); return false; };
        ProcessVisitor afterVisitor = (osProcess, i) -> { after.add(osProcess); return false; };

        Runtime rt = Runtime.getRuntime();
        List<ExecutionResult> results = new ArrayList<>();

        for (int i = 0; i < subTask.inputFiles.size(); i++) {

            byte[] inputBytes = Files.readAllBytes(Paths.get(subTask.inputFiles.get(i)));
            before.clear();
            after.clear();

            mon.visitProcessTree(mon.currentPid(), beforeVisitor);
            Process pr = rt.exec(exeFile);
            mon.visitProcessTree(mon.currentPid(), afterVisitor);

            after.removeAll(before);
            OsProcess process = after.stream().filter(p -> p.processInfo().getName().compareToIgnoreCase("java") == 0).findFirst().get();
            ProcessInfo info = process.processInfo();

            System.out.println();
            System.out.println(info.getName());
            System.out.println(info.getPid());
            System.out.println(info.getParentPid());
            System.out.println(info.getResidentBytes() / 1048576.0);

            pr.getOutputStream().write(inputBytes);
            pr.getOutputStream().write('\n');
            pr.getOutputStream().flush();

            Thread.sleep(50);

            if (pr.isAlive()) {
                pr.destroy();
                results.add(ExecutionResult.TLE);

            } else if (pr.exitValue() != 0) {

                results.add(ExecutionResult.RTE);
            } else {

                if (compareResult(new InputStreamReader(pr.getInputStream()), new FileReader(subTask.outputFiles.get(i)))) {
                    results.add(ExecutionResult.AC);
                } else {
                    results.add(ExecutionResult.WA);
                }
            }
        }

        return results;
    }

    /**
     * Compile the given C/C++ source code and return the
     * exe file path
     *
     * @param programFileName The program to compile
     * @return The exe file or the empty string "" in case of compile error
     */
    protected static String compileProgram(String programFileName) throws IOException, InterruptedException {

        String exeFile = "assets/temp.exe";
        Runtime rt = Runtime.getRuntime();
        String compileCommand = "g++ " + programFileName + " -o " + exeFile;

        Process pr = rt.exec(compileCommand); // compile code into exe
        pr.waitFor();

        return (pr.exitValue() == 0) ? exeFile : "";
    }

    /**
     * Computes the result of running this program on every single
     * sub-task specified in the problem given.
     *
     * @param problem
     * @param programFile
     * @return The list of execution results or NULL in-case of compile error
     */
    public static List<ExecutionResult> gradeProgram(ProblemModel problem, String programFile) throws IOException, InterruptedException {

        String exeFile = compileProgram(programFile);

        // Check for compile error, if so return with error
        if (exeFile == "") return null;

        List<ExecutionResult> results = new ArrayList<>();

        // Grade each sub-task by calling the function above
        for (SubTaskModel subTask: problem.subTasks) {
            results.addAll(gradeSubTask(subTask, exeFile));
        }

        // Cleanup by deleting the temporary exe file
        Files.delete(Paths.get(exeFile));

        return results;
    }
}
