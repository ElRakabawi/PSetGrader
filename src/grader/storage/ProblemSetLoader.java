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
 *
 * A class with a set of static methods to load the test
 * data models from their respective directories.
 *
 * Created by Basim on 22/11/2016.
 */
public class ProblemSetLoader {

    // The name of the config file within each directory
    private static final String MAIN_CONFIG = "config.json";

    /**
     *
     * Read a JSON object from the given filename and close the
     * JSONReader object used.
     *
     * @param fileName The file to load from
     * @return The parsed JSONObject for the file given
     * @throws FileNotFoundException If the file is invalid
     */
    private static JSONDocument getJSONObject(String fileName) throws FileNotFoundException {
        JSONReader reader = new JSONStreamReaderImpl(new FileReader(fileName));
        JSONDocument doc = reader.build();

        reader.close();
        return doc;
    }

    /**
     *
     * Load a sub-task from the specified directory
     *
     * @param dirName The directory to load from
     * @return The SubTaskModel for the directory
     * @throws FileNotFoundException If the config file wasn't found
     */
    public static SubTaskModel loadSubTask(String dirName) throws FileNotFoundException {

        SubTaskModel subTask = new SubTaskModel();
        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        subTask.memoryLimit = configDoc.getNumber("memory-limit").intValue();
        subTask.timeLimit = configDoc.getNumber("time-limit").intValue();

        subTask.inputFiles = new ArrayList<>();
        subTask.outputFiles = new ArrayList<>();

        File inputDir = new File(dirName + File.separator + "input");
        File outputDir = new File(dirName + File.separator + "output");

        if (!inputDir.exists() || !outputDir.exists())
            throw new FileNotFoundException("Test data directories not present");

        FileFilter txtFilter = pathname -> pathname.getName().endsWith(".txt");

        // Load all files, filtering currently allows only text files to prevent config files getting in the way
        for (File input: inputDir.listFiles(txtFilter)) subTask.inputFiles.add(input.getName());
        for (File output: outputDir.listFiles(txtFilter)) subTask.outputFiles.add(output.getName());

        Collections.sort(subTask.inputFiles);
        Collections.sort(subTask.outputFiles);

        return subTask;
    }

    /**
     *  Load a problem from the given directory
     *
     * @param dirName
     * @return
     * @throws FileNotFoundException
     */
    public static ProblemModel loadProblem(String dirName) throws FileNotFoundException{

        ProblemModel problem = new ProblemModel();
        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        // Extract properties from the config file
        problem.name = configDoc.getString("name");
        problem.description = configDoc.getString("description");

        problem.authors = new ArrayList<>();
        problem.subTasks = new ArrayList<>();

        for (Object author: configDoc.getList("authors")) problem.authors.add((String) author);

        // Iterate over each sub-task specified and load it's sub-directory
        for (Object object: configDoc.getList("sub-tasks")) {
            String subTaskName = (String)object;
            problem.subTasks.add(loadSubTask(dirName + File.separator + subTaskName));
        }

        return problem;
    }

    /**
     * Load the entire problem set from a given directory
     *
     * @param dirName
     * @return
     * @throws FileNotFoundException
     */
    public static ProblemSetModel loadProblemSet(String dirName) throws FileNotFoundException {

        ProblemSetModel problemSet = new ProblemSetModel();

        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        // Extract config properties
        problemSet.name = configDoc.getString("name");
        problemSet.description = configDoc.getString("description");
        problemSet.problems = new ArrayList<>();
        problemSet.authors = new ArrayList<>();

        for (Object author: configDoc.getList("authors")) problemSet.authors.add((String) author);

        // Iterate over each specified problem and parse the problems from their own directories
        for (Object object: configDoc.getList("problems")) {
            String problemName = (String)object;
            problemSet.problems.add(loadProblem(dirName + File.separator + problemName));
        }

        return problemSet;
    }
}
