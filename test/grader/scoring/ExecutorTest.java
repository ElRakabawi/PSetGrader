package grader.scoring;

import grader.models.ProblemModel;
import grader.storage.ProblemSetLoader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Basim on 28/11/2016.
 */
public class ExecutorTest {

    @Test
    public void gradeProgram() throws Exception {

        ProblemModel factorial = ProblemSetLoader.loadProblem("testdata/TestProblemSet/factorial");
        List<ExecutionResult> resultList = Executor.gradeProgram(factorial, "testdata/fact.cpp");

        assertEquals("Correct no. of results", resultList.size(), 2);
        assertEquals("For n=3, fact.cpp is AC", ExecutionResult.AC, resultList.get(0));
        assertEquals("For n=4, fact.cpp is WA", ExecutionResult.WA, resultList.get(1));
    }

}