package cw;

import Cryptography.crypto3.LFSR;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.concurrent.Executors;
import java.util.concurrent.Executors.*;
import javafx.stage.Stage;
import utils.InputChecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class CW3 implements AssignmentExercise {

    private static String WORKING_MODE = "Start";

    @Override
    public Tab createExecriseTab(Stage primaryStage) {

        String[] functionLabels = new String[]{"Start", "Stop"};
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
        grid.add(inputLabel, 0, 4);

        TextField keyInputArea = new TextField();
        inputInfo.setId("input-checker");
        keyInputArea.textProperty().addListener(actionEvent -> {
                inputInfo.setCheckerPattern(Pattern.compile("([[x]\\d[?+]]{1,})"));
                if (inputInfo.isInputCorrect(keyInputArea.getText())) {
                    inputInfo.setText("OK!");
                } else {
                    inputInfo.setText("Wrong! A polynomial ie. x1+x2+x3.");
                }
        });


        HBox userInputsHBox = new HBox(keyInputArea, inputInfo);

        grid.add(userInputsHBox, 0, 3);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Button encryptBtn = new Button("Start");
        encryptBtn.setOnAction(actionEvent -> {
            LFSR lfsr = new LFSR(keyInputArea.getText());
            Future<Boolean[]> finalValue = exec.submit(lfsr);
            if (WORKING_MODE.equals("Start")) {
                try {
                    inputLabel.setText(finalValue.get().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else if (WORKING_MODE.equals("Stop")) {
                finalValue.cancel(true);
            }

//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Save as file");
//            File file = fileChooser.showSaveDialog(primaryStage);
//            if (file != null) {
//                try {
//                    byte[] file_data;
//                    if (WORKING_MODE.equals("Start")) {
//                        lfsr.run();
//                    } else if (WORKING_MODE.equals("Stop")) {
//                        lfsr.stop();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        });

        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(encryptBtn);
        grid.add(hbBtn, 1, 5);

        ChoiceBox functionChoiceBox = new ChoiceBox(FXCollections.observableArrayList("Start", "Stop"));
        functionChoiceBox.setValue("Start");
        functionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    encryptBtn.setText(functionLabels[new_val.intValue()]);
                    WORKING_MODE = functionLabels[new_val.intValue()];
                }
        );
        grid.add(functionChoiceBox, 0, 1);
        final Separator genEncrSeparator = new Separator();
        grid.add(genEncrSeparator,0,6);

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


}
