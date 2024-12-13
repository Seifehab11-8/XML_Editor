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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import javafx.scene.control.Label;

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
    public Button NetworkAnalysis;
    public Button Graph;
    public String filePath;
    @FXML TextArea contentArea;
    private final String[] tooltipsText = {
            " Correct ",
            " Detect errors ",
            " Validate ",
            " Convert to JSON format ",
            " Minimize ",
            " Decompress ",
            " Compress ",
            " Go back ",
            " Network Operations ",
            " Graph Operation "
    };

    Button[] buttonsList;

    @FXML
    private void initialize() {
        buttonsList = new Button[]{correction, detection, validity, to_JSON, min, decomp, comp, backButton,NetworkAnalysis,Graph};
        SetFilePath(null); // Assuming filePath is set before
        Platform.runLater(() -> contentArea.getScene().getRoot().requestFocus());
        loadIcons();
        applyTooltips();
        styleContentArea();
    }

    public void SetFilePath(String filePath) {
        this.filePath = filePath;
        loadFileContent(filePath);
    }

    private void loadFileContent(String file_path) {
        if (file_path != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(file_path)));
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
        Image network = loadImage("/icons/networking.png");
        Image graph = loadImage("/icons/connection.png");
        ImageView[] iconViews = {
                createImageView(correct),
                createImageView(detect),
                createImageView(validate),
                createImageView(compress),
                createImageView(decompress),
                createImageView(back),
                createImageView(mini),
                createImageView(JSON),
                createImageView(network),
                createImageView(graph)
        };

        setButtonIcons(iconViews);
    }

    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    private ImageView createImageView(Image image ) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
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
        NetworkAnalysis.setGraphic(iconViews[8]);
        Graph.setGraphic(iconViews[9]);
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
        contentArea.setStyle("-fx-border-radius: 25;-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 25; -fx-border-color: rgb(91,91,240)");
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
    @FXML
    private void CallMinifier() throws IOException {
        String minifiedFilePath="src/main/resources/outPut.xml";
        XMLMinifier xmlMinifier = new XMLMinifier();
        xmlMinifier.minifyFile(filePath,minifiedFilePath);
        loadFileContent(minifiedFilePath);
        this.filePath=minifiedFilePath;
        contentArea.setWrapText(false);
    }
    @FXML
    private void CallValidator() {

        XMLValidator xmlValidator = new XMLValidator();
        boolean validationResult = xmlValidator.validateXML(filePath);
        Stage validationMessageStage = new Stage();
        Scene scene = getScene(validationResult);
        validationMessageStage.setScene(scene);
        validationMessageStage.setTitle("Validation Result");
        Image icon = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/icons/validation.png")));
        validationMessageStage.getIcons().add(icon);
        validationMessageStage.show();
    }

    private static Scene getScene(boolean validationResult) {

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-alignment: center;-fx-text-alignment: center;-fx-background-radius:25 ;-fx-background-color:black ;-fx-text-fill: white; -fx-font-size: 12; -fx-border-color: #5B5BF0;-fx-border-radius: 25;-fx-border-width: 3;-fx-pref-width: 250;-fx-pref-height: 50");

        if (validationResult) {
            resultLabel.setText("The XML file is valid");
        } else {
            resultLabel.setText("The XML file is not valid");
        }

        VBox layout = new VBox(20, resultLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        return new Scene(layout, 300, 200);
    }
    @FXML
    private void CallCorrection() throws IOException {
        XMLCorrection xmlCorrection = new XMLCorrection();
        try {
            String correctedFilePath = "src/main/resources/correctedfile.xml";
            xmlCorrection.correctXML(filePath, correctedFilePath);
            loadFileContent(correctedFilePath);
            this.filePath=correctedFilePath;
        } catch (IOException e) {
            System.err.println(STR."Failed to correct or load XML: \{e.getMessage()}");
            throw e;
        }

    }
    @FXML
    private void CallJSON() throws IOException {
       XmlToJsonConverter xmlToJsonConverter =new XmlToJsonConverter();
        String jsonfile =  "src/main/resources/outPut.xml";
        xmlToJsonConverter.JSONMain(filePath,jsonfile);
        this.filePath = jsonfile;
        loadFileContent(jsonfile);
    }

}
