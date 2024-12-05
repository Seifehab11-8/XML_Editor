package org.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private String filePath;
    public void SetFilePath(String filePath){
         this.filePath= filePath;
         loadFileContent();

    }
    @FXML TextArea contentArea;
    @FXML ToggleButton NightMode;

    private void loadFileContent(){
        if (filePath != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                contentArea.setText(content);
            } catch (IOException e) {
                contentArea.setText(STR."Error loading file: \{e.getMessage()}");
            }
        }
    }

    @FXML private void setOnhover(){
        DropShadow shadow = new DropShadow();
        NightMode.addEventHandler(MouseEvent.MOUSE_ENTERED,
                _ -> NightMode.setEffect(shadow));
        NightMode.addEventHandler(MouseEvent.MOUSE_EXITED,
                _ -> NightMode.setEffect(null));

    }
    @FXML
    private void initialize() {
        loadFileContent();
        Platform.runLater(() -> contentArea.getScene().getRoot().requestFocus());
        setOnhover();
        Image correct = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/check.png")));
        Image detect = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/detection.png")));
        Image validate = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/validation.png")));
        Image compress = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/compression.png")));
        Image decompress = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/download (1).png")));
        Image night = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/night_mode.png")));
        Image back = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/left_arrow.png")));
        Image mini = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/minimize.png")));
        Image JSON = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/file.png")));


        ImageView viewCorrection = new ImageView(correct);
        ImageView viewdetect = new ImageView(detect);
        ImageView viewvalidate = new ImageView(validate);
        ImageView viewcomp= new ImageView(compress);
        ImageView viewdecomp = new ImageView(decompress);
        ImageView viewnight = new ImageView(night);
        ImageView viewback = new ImageView(back);
        ImageView viewmini = new ImageView(mini);
        ImageView viewJSON = new ImageView(JSON);

        Veiws(viewCorrection, viewdetect, viewvalidate, viewcomp);
        Veiws(viewdecomp, viewJSON, viewmini, viewmini);
        viewnight.setFitWidth(20);
        viewnight.setFitHeight(20);
        viewnight.setPreserveRatio(true);
        viewback.setFitWidth(20);
        viewback.setFitHeight(20);
        viewback.setPreserveRatio(true);


      correction.setGraphic(viewCorrection);
      detection.setGraphic(viewdetect);
      validity.setGraphic(viewvalidate);
      to_JSON.setGraphic(viewJSON);
      min.setGraphic(viewmini);
      NightMode.setGraphic(viewnight);
      decomp.setGraphic(viewdecomp);
      comp.setGraphic(viewcomp);
      backButton.setGraphic(viewback);
      contentArea.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 25 ;-fx-border-color: rgb(91,91,240) ");
      Tooltips();
    }

    private void Veiws(ImageView viewdecomp, ImageView viewJSON, ImageView viewback, ImageView viewmini) {
        viewdecomp.setFitWidth(50);
        viewdecomp.setFitHeight(50);
        viewdecomp.setPreserveRatio(true);
        viewJSON.setFitWidth(50);
        viewJSON.setFitHeight(50);
        viewJSON.setPreserveRatio(true);
        viewback.setFitWidth(50);
        viewback.setFitHeight(50);
        viewback.setPreserveRatio(true);
        viewmini.setFitWidth(50);
        viewmini.setFitHeight(50);
        viewmini.setPreserveRatio(true);
    }
    private void Tooltips(){
        Tooltip tooltip1 = new Tooltip("Click to correct the current input or selection");
        Tooltip tooltip2 = new Tooltip("Click to detect errors or anomalies");
        Tooltip tooltip3 = new Tooltip("Validate the input data for accuracy");
        Tooltip tooltip4 = new Tooltip("Convert the current data to a JSON format");
        Tooltip tooltip5 = new Tooltip("Minimize Your XML file");
        Tooltip tooltip6 = new Tooltip("Toggle night mode for a darker theme");
        Tooltip tooltip7 = new Tooltip("Decompress the selected file or data");
        Tooltip tooltip8 = new Tooltip("Compress the selected file or data");
        Tooltip tooltip9 = new Tooltip("Go back to the previous screen");
        Tooltip.install(correction, tooltip1);
        Tooltip.install(detection, tooltip2);
        Tooltip.install(validity, tooltip3);
        Tooltip.install(to_JSON, tooltip4);
        Tooltip.install(min, tooltip5);
        Tooltip.install(NightMode, tooltip6);
        Tooltip.install(decomp, tooltip7);
        Tooltip.install(comp, tooltip8);
        Tooltip.install(backButton, tooltip9);


    }
    @FXML
    private void ReturnToMainScene() throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/hello-view.fxml")));
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setWidth(stage.getWidth());
        stage.setHeight(stage.getWidth());
        Scene newScene= new Scene(root);
        stage.setScene(newScene);

        stage.show();
    }
}
