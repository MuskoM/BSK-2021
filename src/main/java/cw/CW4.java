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
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import utils.Bits;

public class CW4 implements AssignmentExercise {

    private static String WORKING_MODE = "Initialize";
    private static String WORKING_MODE_ENCRYPT = "Encrypt";
    public BufferedImage img;
    public byte[] input;
    public byte[] key;

    @Override
    public Tab createExecriseTab(Stage primaryStage) {
        Tab des_tab = new Tab("Ćw 4");
        DES des = new DES();
        final File[] file = {null};
        final Map<Integer, Bits>[] keyMap = new Map[]{des.generateKeys()};

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
                file[0] = fileChooser.showOpenDialog(primaryStage);
                FileInputStream fileIn = new FileInputStream(file[0]);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                keyMap[0] = (Map<Integer, Bits>) in.readObject();
                System.out.println(keyMap[0].toString());
                in.close();
                fileIn.close();
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
                file[0] = fileChooser.showOpenDialog(primaryStage);
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setHeaderText("There is an error with your key.");
                alert.setContentText("Don't leave key field empty.");
                alert.showAndWait();
            }
        });
        grid.add(openFileBtn, 0 ,2);

        //encrypt/decrypt button
        Button encryptButton = new Button("Encrypt");
        encryptButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as file");
            file[0] = fileChooser.showSaveDialog(primaryStage);
            System.out.println(keyMap[0].toString());
            if (file[0] != null && img != null) {
                try {
                    if (file[0].getName().split("\\.")[1].equals("png")) {
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            Map<Integer, byte[]> encryptedBytes = des.encryptDES(keyMap[0], file[0]);
                            ImageIO.write(ImageIO.read((File) encryptedBytes), "png", file[0]);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            Map<Integer, byte[]> decryptedBytes = des.decryptDES(keyMap[0], file[0]);
                            ImageIO.write(ImageIO.read((File) decryptedBytes), "png", file[0]);
                        }
                    } else if (file[0].getName().split("\\.")[1].equals("jpg")) {
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            Map<Integer, byte[]> encryptedBytes = des.encryptDES(keyMap[0], file[0]);
                            ImageIO.write(ImageIO.read((File)encryptedBytes), "jpg", file[0]);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            Map<Integer, byte[]> decryptedBytes = des.decryptDES(keyMap[0], file[0]);
                            ImageIO.write(ImageIO.read((File)decryptedBytes), "jpg", file[0]);
                        }
                    } else {
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            Map<Integer, byte[]> encryptedBytes = des.encryptDES(keyMap[0], file[0]);
                            writeFile(file[0], encryptedBytes);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            Map<Integer, byte[]> decryptedBytes = des.decryptDES(keyMap[0], file[0]);
                            writeFile(file[0], decryptedBytes);
                        }
                    }
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
            file[0] = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(file[0]);
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    out.writeObject(keymap);
                    out.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(saveKeyButton, 0, 4);


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
        Files.write(file.toPath(), (Iterable<? extends CharSequence>) data);
    }

    public void writeKeys(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }
}
