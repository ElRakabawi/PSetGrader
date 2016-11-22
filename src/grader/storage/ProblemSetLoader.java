package grader.storage;

import grader.Constants;
import grader.models.ProblemSetModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Basim on 22/11/2016.
 */
public class ProblemSetLoader {

    public static ProblemSetModel loadProblemSet(String dirName) throws FileNotFoundException {

        // Look for the config file and load that
        File configFile = new File(dirName + File.pathSeparator + Constants.MAIN_CONFIG);
        BufferedReader configReader = new BufferedReader(new FileReader(configFile));

        // TODO: Finish loading the problem set

        return null;
    }
}
