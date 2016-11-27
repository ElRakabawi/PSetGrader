package grader.models;

import java.util.List;

/**
 * Created by Basim on 22/11/2016.
 */
public class SubTaskModel {

    /**
     * Memory limit in MiB
     */
    public int memoryLimit;

    /**
     * Time limit for the program in milliseconds
     */
    public int timeLimit;

    public List<String> inputFiles;
    public List<String> outputFiles;
}
