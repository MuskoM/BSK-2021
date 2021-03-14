package cw;

import Cryptography.Cipher;
import Cryptography.crypto1.Cryptography;
import Cryptography.crypto1.CryptographyA;
import Cryptography.crypto1.CryptographyB;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        RadioButton railFenceRadioBtn = new RadioButton("RailFence");
        RadioButton encrIntKeyRadioBtn = new RadioButton("Encr-intkey");
        RadioButton encrStrKeyRadioBtn = new RadioButton("Encr-strkey");

        ToggleGroup encryptOptionGroup = new ToggleGroup();
        railFenceRadioBtn.setToggleGroup(encryptOptionGroup);
        encrIntKeyRadioBtn.setToggleGroup(encryptOptionGroup);
        encrStrKeyRadioBtn.setToggleGroup(encryptOptionGroup);

        HBox encrHbox = new HBox(railFenceRadioBtn,encrIntKeyRadioBtn,encrStrKeyRadioBtn);
        railFenceRadioBtn.fire();
        grid.add(encrHbox,0,1);


        Label inputLabel = new Label("Input data");
        grid.add(inputLabel,0,3);

        final byte[][] data = {new byte[0]};

        Button openFileBtn = new Button("Select file...");
        openFileBtn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    data[0] = readFile(file);
                    inputLabel.setText(new String(Arrays.copyOf(data[0], 150),StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        TextField keyInputArea = new TextField("Input key");
        HBox userInputsHBox = new HBox(openFileBtn,keyInputArea);

        grid.add(userInputsHBox,0,2);

        Button encryptBtn = new Button("Encrypt");
        encryptBtn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as file");
            File file = fileChooser.showSaveDialog(primaryStage);
            Cipher cipher = new Cryptography();
            if (file != null){
                try {
                    byte[] file_data;
                    if (railFenceRadioBtn.equals(encryptOptionGroup.getSelectedToggle())) {
                        cipher = new CryptographyA();
                        int int_key = UserInputToInt(keyInputArea.getText());
                        if(WORKING_MODE.equals("Encrypt")){
                            file_data = cipher.encrypt(data[0],int_key);
                            writeFile(file,file_data);

                        }else if(WORKING_MODE.equals("Decrypt")){
                            file_data = cipher.decrypt(data[0],int_key);
                            writeFile(file,file_data);
                        }
                    }
                    else if(encrIntKeyRadioBtn.equals(encryptOptionGroup.getSelectedToggle())){
                        cipher = new Cryptography();
                        int[] arr_key = UserInputToIntArray(keyInputArea.getText());
                        if(WORKING_MODE.equals("Encrypt")){
                            file_data = cipher.encrypt(data[0],arr_key);
                            writeFile(file,file_data);

                        }else if(WORKING_MODE.equals("Decrypt")){
                            file_data = cipher.decrypt(data[0],arr_key);
                            writeFile(file,file_data);
                        }

                    }else if(encrStrKeyRadioBtn.equals(encryptOptionGroup.getSelectedToggle())){
                        cipher = new CryptographyB();
                        String string_key = keyInputArea.getText();
                        if(WORKING_MODE.equals("Encrypt")){
                            file_data = cipher.encrypt(data[0],string_key);
                            writeFile(file,file_data);
                        }else if(WORKING_MODE.equals("Decrypt")){
                            file_data = cipher.decrypt(data[0],string_key);
                            writeFile(file,file_data);
                        }
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
        grid.add(functionChoiceBox,0,0);

        return bsk1;
    }

    public byte[] readFile(File file) throws IOException {
        byte[] text = Files.readAllBytes(file.toPath());
        return text;
    }

    public void writeFile(File file, byte[] data)throws IOException{
        Files.write(file.toPath(),data);
    }

    private int UserInputToInt(String input){
        return Integer.parseInt(input);
    }

    private int[] UserInputToIntArray(String input){
        int[] key = new int[input.length()];
        for (int i=0;i<input.length();i++){
            key[i] = Integer.parseInt(String.valueOf(input.charAt(i)));
        }

        return key;
    }

}
