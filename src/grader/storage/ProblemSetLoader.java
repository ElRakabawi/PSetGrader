package grader.storage;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;
import grader.models.ProblemModel;
import grader.models.ProblemSetModel;
import grader.models.SubTaskModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    protected static JSONDocument getJSONObject(String fileName) throws FileNotFoundException {
        JSONReader reader = new JSONStreamReaderImpl(new FileReader(fileName));
        JSONDocument doc = reader.build();

        reader.close();
        return doc;
    }

    /**
     * Takes an array of relative directories and files and
     * converts, in order, to all files contained amongst them
     *
     * @param parentDir The directory that the paths are relative to
     * @param relativePaths All files/directories to be transformed
     * @return The list of all absolute file paths
     * @throws IOException
     */
    private static List<String> getAbsolutePaths(String parentDir, Stream<String> relativePaths) throws IOException {

        List<String> absolutePaths = new ArrayList<>();

        relativePaths.forEach(pathName -> {
            Path path = Paths.get(parentDir, pathName);

            // Check if config.json specified a directory or a file
            if (Files.isDirectory(path)) {

                try {

                    // Get files in this directory
                    Stream<String> innerFiles = Files.list(path)
                            // Filter out system and hidden files
                            .filter(f -> !f.toFile().isHidden() && !f.startsWith("."))
                            // Store all the file names in sorted order
                            .map(f -> f.getFileName().toString()).sorted();

                    // Recursively get all test cases in the sub-directories
                    absolutePaths.addAll(getAbsolutePaths(path.toString(), innerFiles));

                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            // Simply add the file if it is just a file
            else {
                absolutePaths.add(path.toString());
            }
        });

        return absolutePaths;
    }

    /**
     *
     * Load a sub-task from the specified JSON document descriptor
     *
     * @param problemDir The directory of the problem to load from
     * @param configDoc The sub-task JSON descriptor to load from
     * @return The SubTaskModel for the directory
     * @throws FileNotFoundException If the config file wasn't found
     */
    public static SubTaskModel loadSubTask(String problemDir, JSONDocument configDoc) throws IOException {

        SubTaskModel subTask = new SubTaskModel();

        subTask.memoryLimit = configDoc.getNumber("memory-limit").intValue();
        subTask.timeLimit = configDoc.getNumber("time-limit").intValue();

        subTask.inputFiles = getAbsolutePaths(problemDir, configDoc.getList("input").stream().map(s -> (String)s));
        subTask.outputFiles = getAbsolutePaths(problemDir, configDoc.getList("output").stream().map(s -> (String)s));

        return subTask;
    }

    /**
     *  Load a problem from the given directory
     *
     * @param dirName
     * @return
     * @throws FileNotFoundException
     */
    public static ProblemModel loadProblem(String dirName) throws IOException {

        ProblemModel problem = new ProblemModel();
        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        // Extract properties from the config file
        problem.name = configDoc.getString("name");
        problem.description = configDoc.getString("description");
        problem.authors = configDoc.getList("authors").stream().map(a -> (String) a).collect(Collectors.toList());

        problem.subTasks = new ArrayList<>();
        for (Object object: configDoc.getList("sub-tasks")) {
            JSONDocument subTask = (JSONDocument) object;
            problem.subTasks.add(loadSubTask(dirName, subTask));
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
    public static ProblemSetModel loadProblemSet(String dirName) throws IOException {

        ProblemSetModel problemSet = new ProblemSetModel();

        JSONDocument configDoc = getJSONObject(dirName + File.separator + MAIN_CONFIG);

        // Extract config properties
        problemSet.name = configDoc.getString("name");
        problemSet.description = configDoc.getString("description");
        problemSet.authors = configDoc.getList("authors").stream().map(a -> (String) a).collect(Collectors.toList());

        problemSet.problems = new ArrayList<>();
        for (Object object: configDoc.getList("problems")) {
            String problemName = (String)object;
            problemSet.problems.add(loadProblem(dirName + File.separator + problemName));
        }

        return problemSet;
    }
}
