package com.example;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SecondaryController {


    @FXML private Button saveFileButton;
    @FXML private Button handleCancelEditor;
    @FXML private TextArea textArea;

    @FXML private TextField fontSizeTextField;
    @FXML private TextField colorTextField;
    @FXML private CheckBox boldCheckBox;
    @FXML private CheckBox italicCheckBox;
    @FXML private ComboBox<String> fontStyleComboBox; 
    
    
    private String originalContent;
    
    public void initialize() {
        originalContent = textArea.getText();  
        fontSizeTextField.setText("14"); 
        colorTextField.setText("black");
        fontStyleComboBox.setItems(FXCollections.observableArrayList("Times New Roman", "Helvetica", "Century Gothic", "Sans Serif", "Slab Serif", "Arial Black", "Bookman Old Style", "Arial"));
        fontStyleComboBox.getSelectionModel().selectFirst(); 

        textArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                event.consume(); // Consume the event to prevent further handling
                try {
                    handleSaveFile(); // Call the save method
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleCancelEditor() throws IOException {
        if(textArea.getText().equals(originalContent)) {
            Platform.exit();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Changes");
            alert.setHeaderText("Do you want to save changes to your text file?");
            alert.setContentText("Your changes will be lost if you do not save them.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        handleSaveFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Platform.exit();
                }
            });
        }
    }

    @FXML
    private void handleSaveFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = (Stage) saveFileButton.getScene().getWindow();  
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            originalContent = textArea.getText();
            saveTextToFile(file);
            storeFileLocation(file);
            Platform.exit();
        }
    }

    @FXML
    private void saveTextToFile(File file) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(textArea.getText());  
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    @FXML
    public void loadTextContent(String content) {
        textArea.setText(content); 
        originalContent = content;
    }

    @FXML
    public void storeFileLocation(File file) {
        try (FileWriter fileWriter = new FileWriter("mostRecentFile.txt", false)) {
            fileWriter.append(file.getAbsolutePath() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStyleChange() {
        try {
            String selectedSize = fontSizeTextField.getText();
            String selectedColor = colorTextField.getText();
            String selectedFont = fontStyleComboBox.getValue(); 
            StringBuilder style = new StringBuilder("-fx-font-size: " + selectedSize + "px; -fx-text-fill: " + selectedColor + "; -fx-font-family: '" + selectedFont + "';");

            if (boldCheckBox.isSelected()) {
                style.append("-fx-font-weight: bold; ");
            }
            if (italicCheckBox.isSelected()) {
                style.append("-fx-font-style: italic; ");
            }
            
            textArea.setStyle(style.toString());
        } catch (NumberFormatException e) {
            System.err.println("Invalid font size input: " + fontSizeTextField.getText());
        }
    }
}

