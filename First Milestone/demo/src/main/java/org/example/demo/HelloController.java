package org.example.demo;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;

public class HelloController {
    public Text circleText;
    @FXML
    private TextField filePathField; // This binds to the TextField in the FXML

    @FXML
    private Button browseButton; // This binds to the Button in the FXML
    @FXML
    private void initialize() {
        // Ensure no component gets focus at startup
        Platform.runLater(() -> {
            filePathField.getScene().getRoot().requestFocus();
        });
    }


    @FXML
    public void onBrowseButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        // Optionally add filters for the file types you want to allow
        //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(browseButton.getScene().getWindow());

        // If a file is selected, update the TextField with the file path
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }
    @FXML
    public void onStartButtonClicked() {
        try {
            // Get the file path from TextField
            String filePath = filePathField.getText();

            // Read the content of the file
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            TextArea textArea = new TextArea();
            textArea.setText(content);
            textArea.setWrapText(true);
            textArea.setPrefRowCount(50);
            textArea.setPrefColumnCount(50);

            // Create a new scene
            Scene secondScene = new Scene(new VBox(textArea), 800, 800);

            // Create a new stage (window)
            Stage newWindow = new Stage();
            newWindow.setTitle("XML Content");
            newWindow.setScene(secondScene);

            // Show the window
            newWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
