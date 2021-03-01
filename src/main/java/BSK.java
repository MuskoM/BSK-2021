import crypto1.Cryptography;
import crypto1.CryptographyB;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;


public class BSK extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String[] functionLabels  = new String[]{"Encrypt","Decrypt"};
        primaryStage.setTitle("Decryptor&Encryptor BSK-2021");
        primaryStage.show();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Text baseInfo = new Text("Function");
        baseInfo.setFont(Font.font("Tahoma", FontWeight.NORMAL,20));
        grid.add(baseInfo,0,0,2,1);

        Label inputTypeLabel = new Label("Choose input file");
        grid.add(inputTypeLabel,0,1);

        Label inputLabel = new Label("Input data");
        grid.add(inputLabel,0,2);

        final FileChooser fileChooser = new FileChooser();

        Button openFileBtn = new Button("Select file...");
        openFileBtn.setOnAction(actionEvent -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    byte[] data = readFile(file);
                    inputLabel.setText(Cryptography.encryptedString(Arrays.copyOf(data,15)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        grid.add(openFileBtn,1,1);



        Button encryptBtn = new Button("Encrypt");
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(encryptBtn);
        grid.add(hbBtn,1,4);

        ChoiceBox functionChoiceBox = new ChoiceBox(FXCollections.observableArrayList("Encrypt","Decrypt"));
        functionChoiceBox.setValue("Encrypt");
        functionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val)->{
                    encryptBtn.setText(functionLabels[new_val.intValue()]);
                }
        );
        grid.add(functionChoiceBox,1,0);

        Scene scene = new Scene(grid,300,275);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("./style.css");

    }

    public byte[] readFile(File file) throws IOException {
        byte[] text = Files.readAllBytes(file.toPath());
        return text;
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

