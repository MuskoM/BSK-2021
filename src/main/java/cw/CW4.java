package cw;

import Cryptography.crypto4.DES;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import utils.Bits;

public class CW4 implements AssignmentExercise {

    private static String WORKING_MODE = "Initialize";
    private static String WORKING_MODE_ENCRYPT = "Encrypt";
    public BufferedImage img;
    public byte[] input;
    public byte[] key;
    public File file;
    @Override
    public Tab createExecriseTab(Stage primaryStage) {
        Tab des_tab = new Tab("Ćw 4");
        final byte[][] data = {new byte[0]};
        final byte[][] key_data = {new byte[0]};
        DES des = new DES();
        AtomicReference<Object> o = null;

        //Main grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        des_tab.setContent(grid);

        //main label
        Label mainLabel = new Label("DES Algorithm");
        grid.add(mainLabel, 0, 0);

        String[] funcLabels = new String[]{"Encrypt", "Decrypt"};

        //key file chooser
        Button openKeyFileBtn = new Button("Select key file");
        openKeyFileBtn.setDisable(true);
        openKeyFileBtn.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                file = fileChooser.showOpenDialog(primaryStage);
                key = readFile(file);
                key_data[0] = readFile(file);
                ByteArrayInputStream bis = new ByteArrayInputStream(key_data[0]);
                ObjectInput in = new ObjectInputStream(bis);
                assert o != null;
                o.set(in.readObject());
            } catch (NullPointerException | IOException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setHeaderText("There is an error with your key.");
                alert.setContentText("Provided key file is corrupted. Please provide correct key file.");
                alert.showAndWait();
            }
        });
        grid.add(openKeyFileBtn, 0, 1);

        //file chooser
        Button openFileBtn = new Button("Select file");
        openFileBtn.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                file = fileChooser.showOpenDialog(primaryStage);
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setHeaderText("There is an error with your key.");
                alert.setContentText("Don't leave key field empty.");
                alert.showAndWait();
            }
        });
        grid.add(openFileBtn, 0 ,2);
        Bits baseKey =des.generateBaseKey();
        Map<String,Bits> dividedKey = des.divideKey(baseKey);
        //encrypt/decrypt button
        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as file");

            File saveFile = fileChooser.showSaveDialog(primaryStage);
            System.out.println("kkkk");
            if (saveFile != null) {
                System.out.println("aaaaa");
                try {
                    if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                        for(int i=0;i<dividedKey.size();i++){
                            System.out.println("to tu : "+dividedKey);
                        }

                        Map<Integer, byte[]> encryptedBytes = des.encryptDES(dividedKey, file);
                        System.out.println("wyjscie");
                        writeFile(saveFile, encryptedBytes);
                    } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                        Map<Integer, byte[]> decryptedBytes = des.decryptDES(dividedKey, file);
                        writeFile(saveFile, decryptedBytes);
                    }
                    /*if (file.getName().split("\\.")[1].equals("png")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(img, "png", bos);
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {

                            Map<Integer, byte[]> encryptedBytes = des.encryptDES(a, file);

                            ImageIO.write(ImageIO.read((File) encryptedBytes), "png", file);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            Map<Integer, byte[]> decryptedBytes = des.decryptDES(a, file);
                            ImageIO.write(ImageIO.read((File) decryptedBytes), "png", file);
                        }
                    } else if (file.getName().split("\\.")[1].equals("jpg")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(img, "jpg", bos);
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            Map<Integer, byte[]> encryptedBytes = des.encryptDES(a, file);
                            ImageIO.write(ImageIO.read((File)encryptedBytes), "jpg", file);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            Map<Integer, byte[]> decryptedBytes = des.decryptDES(a, file);
                            ImageIO.write(ImageIO.read((File)decryptedBytes), "jpg", file);
                        }
                    } else {

                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(encryptButton, 1, 4);

        Button saveKeyButton = new Button("Save key to disk");
        saveKeyButton.setOnAction(actionEvent -> {
            Map<Integer, Bits> keymap = des.generateKeys();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save key file");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                ByteArrayOutputStream keyOut = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(keyOut);
                    out.writeObject(keymap);
                    out.flush();
                    writeKeys(file, keyOut.toByteArray());
                    keyOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(saveKeyButton, 0, 10);


        ChoiceBox functionChoiceBoxEncrypt = new ChoiceBox(FXCollections.observableArrayList("Encrypt", "Decrypt"));
        functionChoiceBoxEncrypt.setValue("Encrypt");
        functionChoiceBoxEncrypt.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    encryptButton.setText(funcLabels[new_val.intValue()]);
                    WORKING_MODE_ENCRYPT = funcLabels[new_val.intValue()];
                }
        );
        functionChoiceBoxEncrypt.setOnAction(actionEvent -> {
            openKeyFileBtn.setDisable(WORKING_MODE_ENCRYPT.equals("Encrypt"));
            saveKeyButton.setDisable(WORKING_MODE_ENCRYPT.equals("Decrypt"));
        });
        grid.add(functionChoiceBoxEncrypt, 0, 3);

        return des_tab;
    }

    public byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public void writeFile(File file, Map<Integer, byte[]> data) throws IOException {

        byte[] yes = new byte[data.size()*8];
        for(int i=0;i<data.size();i++){
            byte[] temp = data.get(i);
            for(int j=0;j<yes.length;j++){
                yes[j] = temp[j%8];
            }
        }
        System.out.println(Arrays.toString(yes));
        Files.write(file.toPath(), yes);
    }

    public void writeKeys(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }
}
