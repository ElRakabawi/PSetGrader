package grader.storage;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;
import grader.models.ProblemModel;
import grader.models.ProblemSetModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Basim on 22/11/2016.
 */
public class ProblemSetLoader {

    private static final String MAIN_CONFIG = "config.txt";

    private static JSONDocument getJSONObject(String fileName) throws FileNotFoundException {
        JSONReader reader = new JSONStreamReaderImpl(new FileReader(fileName));
        JSONDocument doc = reader.build();

        reader.close();
        return doc;
    }

    private static ProblemModel loadProblem(String dirName, JSONDocument problemJson) {

        ProblemModel problem = new ProblemModel();
        problem.name = problemJson.getString("name");
        problem.description = problemJson.getString("description");

        return problem;
    }

    public static ProblemSetModel loadProblemSet(String dirName) throws FileNotFoundException {

        ProblemSetModel problemSet = new ProblemSetModel();

        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        problemSet.name = configDoc.getString("name");
        problemSet.description = configDoc.getString("description");
        problemSet.problems = new ArrayList<ProblemModel>();

        for (Object object: configDoc.getList("problems")) {
            JSONDocument doc = (JSONDocument) object;

            ProblemModel problem = new ProblemModel();

        }

        // TODO: Finish loading the problem set

        return problemSet;
    }
}
