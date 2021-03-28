package cw;

import Cryptography.Cipher;
import Cryptography.crypto1.Cryptography;
import Cryptography.crypto3.LFSR;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executors;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.InputChecker;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class CW3 implements AssignmentExercise {

    private static String WORKING_MODE = "Start";
    private static String WORKING_MODE_ENCRYPT = "Encrypt";
    public BufferedImage img;

    @Override
    public Tab createExecriseTab(Stage primaryStage) {

        String[] functionLabels = new String[]{"Step", "Initialize"};
        String[] functionLabelsEncrypt = new String[]{"Encrypt", "Decrypt"};
        InputChecker inputInfo = new InputChecker("");
        Tab bsk1 = new Tab("Ćw 3");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        bsk1.setContent(grid);

        Label randomNumberGeneratorLabel = new Label("Linear Feedback Shift Register");
        grid.add(randomNumberGeneratorLabel,0,0);

        Label inputLabel = new Label("Input data");
        inputLabel.setWrapText(true);
        inputLabel.setMaxWidth(grid.getMaxWidth()+300/2);
        grid.add(inputLabel, 0, 4);

        TextField keyInputArea = new TextField();
        inputInfo.setId("input-checker");
        keyInputArea.textProperty().addListener(actionEvent -> {
                inputInfo.setCheckerPattern(Pattern.compile("^([0-9],?){1,}$"));
                if (inputInfo.isInputCorrect(keyInputArea.getText())) {
                    inputInfo.setText("OK!");
                } else {
                    inputInfo.setText("Wrong! Input powers of a polynomial ie. x1+x2+x3 (1,2,3).");
                }
        });


        HBox userInputsHBox = new HBox(keyInputArea, inputInfo);

        grid.add(userInputsHBox, 0, 3);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Button encryptBtn = new Button("Step");
        LFSR lfsr = new LFSR();

        encryptBtn.setOnAction(actionEvent -> {
            Future<Boolean[]> finalValue = exec.submit(lfsr);
            if (WORKING_MODE.equals("Step")) {
                try {
                    inputLabel.setText(booleanArrayToString(finalValue.get()));
                } catch (InterruptedException e) {
                    System.out.println("ERROR INTERRUPTED");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    System.out.println("ERROR EXEC");
                    e.printStackTrace();
                }
            } else if (WORKING_MODE.equals("Initialize")) {
                lfsr.setUserPolynomialInput(keyInputArea.getText());
                lfsr.initialize();
            }
        });

        Button openFileBtn = new Button("Select file...");
        openFileBtn.setOnAction(actionEvent -> {
            try{
                if(!inputInfo.isInputCorrect(keyInputArea.getText())){
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Something went wrong");
                        alert.setHeaderText("There was an error during encryption/decryption");
                        alert.setContentText("Probably your key is in a wrong format, try to input the correct key.");
                        alert.showAndWait();
                    }
                }else{
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(primaryStage);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    img = ImageIO.read(fileInputStream);
                }
            }catch (NullPointerException | IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setHeaderText("There is an error with your key.");
                alert.setContentText("Don't leave key field empty.");
                alert.showAndWait();
            }

        });
        grid.add(openFileBtn,1,3);

        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as file");
            File file = fileChooser.showSaveDialog(primaryStage);
            Cipher cipher = new Cryptography();
            if (file != null && img != null) {
                try {
                    String key = inputLabel.getText();
                    BufferedImage imageEncrypted;
                    if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                        lfsr.encrypt(img,key,file);
                    } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                        lfsr.decrypt(img,key,file);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(encryptButton,1,4);

        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(encryptBtn);
        grid.add(hbBtn, 1, 5);

        ChoiceBox functionChoiceBox = new ChoiceBox(FXCollections.observableArrayList("Step", "Initialize"));
        functionChoiceBox.setValue("Step");
        functionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    encryptBtn.setText(functionLabels[new_val.intValue()]);
                    WORKING_MODE = functionLabels[new_val.intValue()];
                }
        );
        grid.add(functionChoiceBox, 0, 1);
        final Separator genEncrSeparator = new Separator();
        grid.add(genEncrSeparator,0,6);

        ChoiceBox functionChoiceBoxEncrypt = new ChoiceBox(FXCollections.observableArrayList("Encrypt", "Decrypt"));
        functionChoiceBox.setValue("Encrypt");
        functionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    encryptButton.setText(functionLabelsEncrypt[new_val.intValue()]);
                    WORKING_MODE_ENCRYPT = functionLabelsEncrypt[new_val.intValue()];
                }
        );

        grid.add(functionChoiceBoxEncrypt, 1,1);

        Label streamCipherLabel = new Label("Stream Cipher");
        grid.add(streamCipherLabel,0,7);


        return bsk1;
    }

    public byte[] readFile(File file) throws IOException {
        byte[] text = Files.readAllBytes(file.toPath());
        return text;
    }

    public void writeFile(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }

    public String booleanArrayToString(Boolean[] arr){
        String cipherKey="";
        for (int i = 0; i < arr.length ; i++) {
            if(arr[i] == true){
                cipherKey = cipherKey.concat("1");
            }else{
                cipherKey = cipherKey.concat("0");
            }
        }
        if(cipherKey.length()>20){
            return "..." + cipherKey.substring(cipherKey.length()-15);
        }else{
            return cipherKey;
        }
    }


}
