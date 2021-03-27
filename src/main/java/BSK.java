import cw.AssignmentExercise;
import cw.CW1;
import cw.CW2;
import cw.CW3;
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

        TabPane tabPane = new TabPane(cw1Tab);
        tabPane.getTabs().add(cw2Tab);
        tabPane.getTabs().add(cw3Tab);
        Scene scene = new Scene(tabPane,620,360);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("./style.css");

    }

}


//class BSK{
//    public static void main(String[] args) throws IOException {
//        int[] key = new int[]{4,3,5,2,1,6,7,8};
//
//        byte[] text = Files.readAllBytes(new File("src/main/resources/data.nvi0").toPath());
//
//        byte[] encryptedMessage = Cryptography.offsetMatrixEncryption(text,key);
//        System.out.println(Cryptography.encryptedString(encryptedMessage));
//        byte[] decryptedMessage = Cryptography.offsetMatrixDecryption(encryptedMessage,key);
//        System.out.println(Cryptography.encryptedString(decryptedMessage));
//    }
//
//}
