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

public class CW4 implements AssignmentExercise {

    private static String WORKING_MODE = "Initialize";
    private static String WORKING_MODE_ENCRYPT = "Encrypt";
    public BufferedImage img;
    public byte[] input;
    public byte[] key;

    @Override
    public Tab createExecriseTab(Stage primaryStage) {
        Tab des_tab = new Tab("Ćw 4");
        final byte[][] data = {new byte[0]};
        final byte[][] key_data = {new byte[0]};
        DES des = new DES();

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
        openKeyFileBtn.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);
                key = readFile(file);
                key_data[0] = readFile(file);
            } catch (NullPointerException | IOException e) {
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
                File file = fileChooser.showOpenDialog(primaryStage);
                FileInputStream fileInputStream = new FileInputStream(file);
                img = ImageIO.read(fileInputStream);
                data[0] = readFile(file);
            } catch (NullPointerException | IOException e) {
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
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null && img != null) {
                try {
                    if (file.getName().split("\\.")[1].equals("png")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(img, "png", bos);
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            byte[] encryptedBytes = des.encrypt(bos.toByteArray(), key);
                            ByteArrayInputStream bis = new ByteArrayInputStream(encryptedBytes);
                            ImageIO.write(ImageIO.read(bis), "png", file);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            byte[] decryptedBytes = des.decrypt(bos.toByteArray(), key);
                            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBytes);
                            ImageIO.write(ImageIO.read(bis), "png", file);
                        }
                    } else if (file.getName().split("\\.")[1].equals("jpg")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(img, "jpg", bos);
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            byte[] encryptedBytes = des.encrypt(bos.toByteArray(), key);
                            ByteArrayInputStream bis = new ByteArrayInputStream(encryptedBytes);
                            ImageIO.write(ImageIO.read(bis), "jpg", file);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            byte[] decryptedBytes = des.decrypt(bos.toByteArray(), key);
                            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBytes);
                            ImageIO.write(ImageIO.read(bis), "jpg", file);
                        }
                    } else {
                        byte[] textEncrypted;
                        if (WORKING_MODE_ENCRYPT.equals("Encrypt")) {
                            textEncrypted = des.encrypt(data[0], key);
                            writeFile(file, textEncrypted);
                        } else if (WORKING_MODE_ENCRYPT.equals("Decrypt")) {
                            textEncrypted = des.decrypt(data[0], key);
                            writeFile(file, textEncrypted);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(encryptButton, 1, 4);

        ChoiceBox functionChoiceBoxEncrypt = new ChoiceBox(FXCollections.observableArrayList("Encrypt", "Decrypt"));
        functionChoiceBoxEncrypt.setValue("Encrypt");
        functionChoiceBoxEncrypt.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    encryptButton.setText(funcLabels[new_val.intValue()]);
                    WORKING_MODE_ENCRYPT = funcLabels[new_val.intValue()];
                }
        );
        grid.add(functionChoiceBoxEncrypt, 0, 3);

        return des_tab;
    }

    public byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public void writeFile(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }
}
