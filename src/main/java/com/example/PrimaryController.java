package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML Button loadTextFileButton;
    @FXML Label showRecentFileUsed;

    @FXML
    private void handleCreateNewFile() throws IOException {
        App.setRoot("editorScreen");
    }

    @FXML
    private void handleLoadTextFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = (Stage) loadTextFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("editorScreen.fxml"));
            Parent root = loader.load();
            
            SecondaryController controller = loader.getController();
            String fileContent = Files.readString(selectedFile.toPath()); 
            controller.loadTextContent(fileContent); 

            stage.setScene(new Scene(root));
        }
    }

    @FXML
    private void handleQuit() throws IOException {
        Platform.exit();
    }

    @FXML
    private void initialize(){
        File recentFile = new File("mostRecentFile.txt");
        if(recentFile.exists()){
            try {
                String filePath = Files.readString(recentFile.toPath());
                showRecentFileUsed.setText(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
