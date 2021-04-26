import cw.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

public class BSK extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Decryptor&Encryptor BSK-2021");
        primaryStage.show();

        //Zadanie 1
        AssignmentExercise cw1 = new CW1();
        Tab cw1Tab = cw1.createExecriseTab(primaryStage);

        //Zadanie 2
        AssignmentExercise cw2 = new CW2();
        Tab cw2Tab = cw2.createExecriseTab(primaryStage);

        //Zadanie 3
        AssignmentExercise cw3 = new CW3();
        Tab cw3Tab = cw3.createExecriseTab(primaryStage);

        //Zadanie 4 - DES
        AssignmentExercise cw4 = new CW4();
        Tab cw4Tab = cw4.createExecriseTab(primaryStage);

        TabPane tabPane = new TabPane(cw1Tab);
        tabPane.getTabs().add(cw2Tab);
        tabPane.getTabs().add(cw3Tab);
        tabPane.getTabs().add(cw4Tab);
        Scene scene = new Scene(tabPane,620,360);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("./style.css");

    }

}

