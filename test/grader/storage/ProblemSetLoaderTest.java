package grader.storage;

import com.oracle.javafx.jmx.json.JSONDocument;
import grader.models.ProblemModel;
import grader.models.ProblemSetModel;
import grader.models.SubTaskModel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Basim on 29/11/2016.
 */
public class ProblemSetLoaderTest {

    @Test
    public void loadSubTask() throws Exception {
        JSONDocument jsDoc = ProblemSetLoader.getJSONObject("testdata/TestProblemSet/factorial/config.json");
        SubTaskModel subTask = ProblemSetLoader.loadSubTask("testdata/TestProblemSet/factorial", (JSONDocument) jsDoc.getList("sub-tasks").get(0));

        assertEquals("Correct time limit", subTask.timeLimit, 500);
        assertEquals("Correct memory limit", subTask.memoryLimit, 100);
        assertEquals("Correct no. of inputs", subTask.inputFiles.size(), 2);
        assertEquals("Correct no. of outputs", subTask.outputFiles.size(), 2);
        assertEquals("Equal inputs and outputs", subTask.outputFiles.size(), subTask.inputFiles.size());
    }

    @Test
    public void loadProblem() throws Exception {
        ProblemModel problem = ProblemSetLoader.loadProblem("testdata/TestProblemSet/factorial");

        assertEquals("Correct name", problem.name, "factorial");
        assertEquals("Correct description", problem.description, "Expects a program to take a single integer n and output n! .");
        assertEquals("Correct no. of sub-tasks", problem.subTasks.size(), 1);
        assertEquals("Correct no. of authors", problem.authors.size(), 1);
    }

    @Test
    public void loadProblemSet() throws Exception {

        ProblemSetModel problemSet = ProblemSetLoader.loadProblemSet("testdata/TestProblemSet");

        assertEquals("Correct name", problemSet.name, "TestProblemSet");
        assertEquals("Correct description", problemSet.description, "A test problem set");
        assertEquals("Correct no. of authors", problemSet.authors.size(), 2);
        assertEquals("Correct no. of problems", problemSet.problems.size(), 1);
    }

}