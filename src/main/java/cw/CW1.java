package cw;

import crypto1.Cryptography;
import crypto1.CryptographyB;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class CW1 implements AssignmentExercise{
    public static String WORKING_MODE = "Encrypt";

    @Override
    public Tab createExecriseTab(Stage primaryStage) {
        String[] functionLabels  = new String[]{"Encrypt","Decrypt"};
        Tab bsk1 = new Tab("Ćw 1");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        bsk1.setContent(grid);


        Text baseInfo = new Text("Function");
        baseInfo.setFont(Font.font("Tahoma", FontWeight.NORMAL,20));
        grid.add(baseInfo,0,0,2,1);

        Label inputTypeLabel = new Label("Choose input file");
        grid.add(inputTypeLabel,0,1);

        Label inputLabel = new Label("Input data");
        grid.add(inputLabel,0,2);

        final byte[][] data = {new byte[0]};

        Button openFileBtn = new Button("Select file...");
        openFileBtn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    data[0] = readFile(file);
                    inputLabel.setText(Cryptography.encryptedString(Arrays.copyOf(data[0],15)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        grid.add(openFileBtn,1,1);

        Button encryptBtn = new Button("Encrypt");
        encryptBtn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as file");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null){
                try {
                    byte[] file_data;
                    int[] key = new int[]{4,3,5,2,1,6,7,8};
                    if(WORKING_MODE.equals("Encrypt")){

                        file_data = CryptographyB.calculateCrypto("BATMAN",data[0]);
                        writeFile(file,file_data);

                    }else if(WORKING_MODE.equals("Decrypt")){
                        file_data = CryptographyB.calculateUnCrypto("BATMAN",data[0]);
                        writeFile(file,file_data);
                    }
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        });

        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(encryptBtn);
        grid.add(hbBtn,1,4);

        ChoiceBox functionChoiceBox = new ChoiceBox(FXCollections.observableArrayList("Encrypt","Decrypt"));
        functionChoiceBox.setValue("Encrypt");
        functionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val)->{
                    encryptBtn.setText(functionLabels[new_val.intValue()]);
                    WORKING_MODE = functionLabels[new_val.intValue()];
                }
        );
        grid.add(functionChoiceBox,1,0);

        return bsk1;
    }

    public byte[] readFile(File file) throws IOException {
        byte[] text = Files.readAllBytes(file.toPath());
        return text;
    }

    public void writeFile(File file, byte[] data)throws IOException{
        Files.write(file.toPath(),data);
    }

}
