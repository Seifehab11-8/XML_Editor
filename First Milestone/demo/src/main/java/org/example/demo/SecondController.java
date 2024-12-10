package org.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class SecondController {
    public Button correction;
    public Button detection;
    public Button validity;
    public Button to_JSON;
    public Button min;
    public Button comp;
    public Button decomp;
    public Button backButton;
    public AnchorPane MainAnchorPane;
    private String filePath;
    @FXML TextArea contentArea;
    private final String[] tooltipsText = {
            "Click to correct the current input or selection",
            "Click to detect errors or anomalies",
            "Validate the input data for accuracy",
            "Convert the current data to a JSON format",
            "Minimize Your XML file",
            "Decompress the selected file or data",
            "Compress the selected file or data",
            "Go back to the previous screen"
    };

    Button[] buttonsList;

    @FXML
    private void initialize() {
        buttonsList = new Button[]{correction, detection, validity, to_JSON, min, decomp, comp, backButton};
        SetFilePath(null); // Assuming filePath is set before
        Platform.runLater(() -> contentArea.getScene().getRoot().requestFocus());
        loadIcons();
        applyTooltips();
        styleContentArea();
    }

    public void SetFilePath(String filePath) {
        this.filePath = filePath;
        loadFileContent();
    }

    private void loadFileContent() {
        if (filePath != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                contentArea.setText(content);
            } catch (IOException e) {
                contentArea.setPromptText(STR."Error loading file: \{e.getMessage()}");
            }
        }
    }

    private void loadIcons() {
        Image correct = loadImage("/icons/check.png");
        Image detect = loadImage("/icons/detection.png");
        Image validate = loadImage("/icons/validation.png");
        Image compress = loadImage("/icons/compression.png");
        Image decompress = loadImage("/icons/download (1).png");
        Image back = loadImage("/icons/left_arrow.png");
        Image mini = loadImage("/icons/minimize.png");
        Image JSON = loadImage("/icons/file.png");

        ImageView[] iconViews = {
                createImageView(correct,50,50),
                createImageView(detect,50,50),
                createImageView(validate,50,50),
                createImageView(compress,50,50),
                createImageView(decompress,50,50),
                createImageView(back,20,20),
                createImageView(mini,50,50),
                createImageView(JSON,50,50)
        };

        setButtonIcons(iconViews);
    }

    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    private ImageView createImageView(Image image,double width,double height ) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private void setButtonIcons(ImageView[] iconViews) {
        correction.setGraphic(iconViews[0]);
        detection.setGraphic(iconViews[1]);
        validity.setGraphic(iconViews[2]);
        to_JSON.setGraphic(iconViews[7]);
        min.setGraphic(iconViews[6]);
        decomp.setGraphic(iconViews[4]);
        comp.setGraphic(iconViews[3]);
        backButton.setGraphic(iconViews[5]);
    }

    private void applyTooltips() {
        for (int i = 0; i < tooltipsText.length; i++) {
            setTooltip(buttonsList[i], tooltipsText[i]);
        }
    }

    private void setTooltip(Button button, String text) {
        Tooltip tooltip = new Tooltip(text);
        Tooltip.install(button, tooltip);
    }

    private void styleContentArea() {
        contentArea.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 25; -fx-border-color: rgb(91,91,240)");
    }

    @FXML
    private void ReturnToMainScene() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/hello-view.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setWidth(stage.getWidth());
        stage.setHeight(stage.getWidth());
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.show();
    }
}
