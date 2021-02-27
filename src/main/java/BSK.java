import crypto1.Cryptography;
import crypto1.CryptographyB;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.HBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.Stage;


//public class BSK extends Application {
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//
//
//
//
//        primaryStage.setTitle("Hello World!");
//
//        Button btn = new Button("_Button");
//        var lbl = new Label("Simple JavaFX application.");
//
//        lbl.setFont(Font.font("Serif", FontWeight.NORMAL, 20));
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                lbl.setText("Hello World");
//            }
//        });
//
//        HBox root = new HBox();
//        root.setId("Root");
//        root.setPadding(new Insets(25));
//        root.getChildren().add(btn);
//        root.getChildren().add(lbl);
//        var scene = new Scene(root, 300, 250);
//        scene.getStylesheets().add("style.css");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}


class BSK{
    public static void main(String[] args) throws IOException {
        String a = CryptographyB.calculateCrypto("CONVENIENCE", "HERE IS A SECRET MESSAGE ENCIPHERED BY TRANSPOSITION");
        System.out.println(a);
        String b = CryptographyB.calculateUnCrypto("CONVENIENCE",a);
        System.out.println(b);

        int[] key = new int[]{4,3,5,2,1,6,7,8};

        byte[] text = Files.readAllBytes(new File("src/main/resources/data.nvi0").toPath());

        byte[] encryptedMessage = Cryptography.offsetMatrixEncryption(text,key);
        System.out.println(Cryptography.encryptedString(encryptedMessage));
        byte[] decryptedMessage = Cryptography.offsetMatrixDecryption(encryptedMessage,key);
        System.out.println(Cryptography.encryptedString(decryptedMessage));
    }

}

