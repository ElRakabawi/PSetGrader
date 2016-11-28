package grader;

import grader.models.ProblemSetModel;
import grader.scoring.ExecutionResult;
import grader.scoring.Executor;
import grader.storage.ProblemSetLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        //launch(args);

        ProblemSetModel pset = ProblemSetLoader.loadProblemSet("assets/ProblemSetX");

        for (ExecutionResult res: Executor.gradeProgram(pset.problems.get(0), "assets/fact.cpp")) {
            System.out.println(res.name());
        }
    }
}
