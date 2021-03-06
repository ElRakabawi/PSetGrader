package grader;

import grader.models.ProblemModel;
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

        ProblemModel problem = ProblemSetLoader.loadProblem("testdata/TestProblemSet/factorial");

        Executor.gradeProgram(problem, "testdata/fact.cpp");
    }
}
