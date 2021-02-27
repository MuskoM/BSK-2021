import crypto1.Cryptography;
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
    public static void main(String[] args) {
        int[] key = new int[]{3,1,4,2};
        byte[] encryptedMessage = Cryptography.offsetMatrixEncryption("CRYPTOGRAPHYOSA".getBytes(),key);
        System.out.println(Cryptography.encryptedString(encryptedMessage));
        byte[] decryptedMessage = Cryptography.offsetMatrixDecryption(encryptedMessage,key);
        System.out.println(Cryptography.encryptedString(decryptedMessage));
    }

}

