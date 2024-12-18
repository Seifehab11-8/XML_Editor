package org.example.demo;

import javafx.application.Platform;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class HelloController {
    @FXML
    public Button Start; // Start button
    @FXML
    public VBox fileUploadContainer;
    public ImageView dragimige;
    @FXML
    private TextField filePathField; // TextField for file path

    @FXML
    private Button browseButton; // Browse button

    @FXML
    private void initialize() {
        Platform.runLater(() -> filePathField.getScene().getRoot().requestFocus());

        // Add scale transition for the browse button hover effect
        addHoverEffect(browseButton);

        // Add scale transition for the start button hover effect
        addHoverEffect(Start);

        // Set up drag-and-drop for the file path field
        setupDragAndDrop(fileUploadContainer);
    }

    private void addHoverEffect(Button button) {
        // Scale transition for mouse entered event
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.02); // Scale up by 2%
            st.setToY(1.02);
            st.play();
        });

        // Scale transition for mouse exited event
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0); // Scale back to original size
            st.setToY(1.0);
            st.play();
        });
    }

    private void setupDragAndDrop(VBox fileUploadContainer) {
        // Allow drag over event
        fileUploadContainer.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && db.getFiles().stream().allMatch(file -> (file.getName().endsWith(".xml")||file.getName().endsWith(".json")))) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Handle drop event
        fileUploadContainer.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                for (File file : db.getFiles()) {
                    if (file.getName().toLowerCase().endsWith(".xml") ||file.getName().toLowerCase().endsWith(".json")) {
                        if (file.exists()) {
                            if (file.getName().toLowerCase().endsWith(".xml") ||file.getName().toLowerCase().endsWith(".json")) {
                                filePathField.setText(file.getAbsolutePath());
                                onStartButtonClicked();
                            } else {
                                fileUploadContainer.getChildren().clear();
                                TextField textField =new TextField();
                                textField.setText("Not an XML/JSON File");
                            }
                        }
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    public void onBrowseButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        // Optionally add filters for the file types you want to allow
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter( "XML and JSON Files", "*.xml", "*.json"));

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
            FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("/second-scene.fxml"));
            Parent root = fxmlLoader.load();
            SecondController secondController = fxmlLoader.getController();
            secondController.SetFilePath(filePathField.getText());
            Stage stage = (Stage) Start.getScene().getWindow();
            stage.setWidth(stage.getWidth());
            stage.setHeight(stage.getHeight());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}