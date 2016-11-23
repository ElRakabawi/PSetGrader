package grader.storage;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;
import grader.models.ProblemModel;
import grader.models.ProblemSetModel;
import grader.models.SubTaskModel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Basim on 22/11/2016.
 */
public class ProblemSetLoader {

    private static final String MAIN_CONFIG = "config.json";

    private static JSONDocument getJSONObject(String fileName) throws FileNotFoundException {
        JSONReader reader = new JSONStreamReaderImpl(new FileReader(fileName));
        JSONDocument doc = reader.build();

        reader.close();
        return doc;
    }

    public static SubTaskModel loadSubTask(String dirName) throws FileNotFoundException {

        SubTaskModel subTask = new SubTaskModel();

        subTask.inputFiles = new ArrayList<String>();
        subTask.outputFiles = new ArrayList<String>();

        File inputDir = new File(dirName + File.separator + "input");
        File outputDir = new File(dirName + File.separator + "output");

        FileFilter txtFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }
        };

        for (File input: inputDir.listFiles(txtFilter)) subTask.inputFiles.add(input.getName());
        for (File output: outputDir.listFiles(txtFilter)) subTask.outputFiles.add(output.getName());

        Collections.sort(subTask.inputFiles);
        Collections.sort(subTask.outputFiles);

        return subTask;
    }

    public static ProblemModel loadProblem(String dirName) throws FileNotFoundException{

        ProblemModel problem = new ProblemModel();

        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        problem.name = configDoc.getString("name");
        problem.description = configDoc.getString("description");
        problem.subTasks = new ArrayList<SubTaskModel>();

        for (Object object: configDoc.getList("sub-tasks")) {
            String subTaskName = (String)object;
            problem.subTasks.add(loadSubTask(dirName + File.separator + subTaskName));
        }

        return problem;
    }

    public static ProblemSetModel loadProblemSet(String dirName) throws FileNotFoundException {

        ProblemSetModel problemSet = new ProblemSetModel();

        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        problemSet.name = configDoc.getString("name");
        problemSet.description = configDoc.getString("description");
        problemSet.problems = new ArrayList<ProblemModel>();

        for (Object object: configDoc.getList("problems")) {
            String problemName = (String)object;
            problemSet.problems.add(loadProblem(dirName + File.separator + problemName));
        }

        return problemSet;
    }
}
