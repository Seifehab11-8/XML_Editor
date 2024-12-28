package org.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class SecondController {
    public Button correction;
    public Button prettify;
    public Button validity;
    public Button to_JSON;
    public Button min;
    public Button comp;
    public Button Jsondecomp;
    public Button backButton;
    public AnchorPane MainAnchorPane;
    public Button Graph;
    public String filePath;
    public Button mostActiveButton;
    public Button most_Influencer;
    public Button mutualFollowers;
    public Button suggest;
    public Button DecomXML;
    public Button JsonCompressor;
    public Button PostSearch;
    @FXML TextArea contentArea;
    private final String[] tooltipsText = {
            " Correct ",
            " Prettify ",
            " Validate ",
            " Convert to JSON format ",
            " Minimize ",
            " Decompress JSON ",
            "Decompress XML",
            " Compress XML ",
            " Go back ",
            " Graph Visualization ",
            "Compress JSON"
    };
    String[] network_command ={"most_active","most_influencer","mutual","suggest"};


    Button[] buttonsList;

    @FXML
    private void initialize() {
        buttonsList = new Button[]{correction, prettify, validity, to_JSON, min, Jsondecomp,DecomXML, comp, backButton,Graph,JsonCompressor};
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
                contentArea.setPromptText("Error loading file: \"" +e.getMessage());
            }
        }
    }

    private void loadIcons() {
        Image correct = loadImage("/icons/check.png");
        Image prettify = loadImage("/icons/hair-wash_16463615.png");
        Image validate = loadImage("/icons/validation.png");
        Image compress = loadImage("/icons/compression.png");
        Image decompress = loadImage("/icons/download (1).png");
        Image decompress_2 = loadImage("/icons/download (1).png");
        Image back = loadImage("/icons/left_arrow.png");
        Image mini = loadImage("/icons/minimize.png");
        Image JSON = loadImage("/icons/file.png");
        Image graph = loadImage("/icons/connection.png");
        Image compJson = loadImage("/icons/compression.png");
        ImageView[] iconViews = {
                createImageView(correct),
                createImageView(prettify),
                createImageView(validate),
                createImageView(compress),
                createImageView(decompress),
                createImageView(back),
                createImageView(mini),
                createImageView(JSON),
                createImageView(graph),
                createImageView(decompress_2),
                createImageView(compJson)
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
        prettify.setGraphic(iconViews[1]);
        validity.setGraphic(iconViews[2]);
        to_JSON.setGraphic(iconViews[7]);
        min.setGraphic(iconViews[6]);
        Jsondecomp.setGraphic(iconViews[4]);
        DecomXML.setGraphic(iconViews[9]);
        comp.setGraphic(iconViews[3]);
        backButton.setGraphic(iconViews[5]);
        Graph.setGraphic(iconViews[8]);
        JsonCompressor.setGraphic(iconViews[10]);
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
        String minifiedFilePath="src/main/resources/Minified.xml";
        XMLMinifier xmlMinifier = new XMLMinifier();
        xmlMinifier.minifyFile(filePath,minifiedFilePath);
        loadFileContent(minifiedFilePath);
        contentArea.setWrapText(false);
    }
    boolean  validationResult;
    @FXML
    private void CallValidator() {
        XMLValidator xmlValidator = new XMLValidator();
        validationResult = xmlValidator.validateXML(filePath);
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
        if(!validationResult){
            XMLCorrection xmlCorrection = new XMLCorrection();
            try {
                String correctedFilePath = "src/main/resources/correctedfile.xml";
                xmlCorrection.CorrectXML_Helper(filePath, correctedFilePath);
                loadFileContent(correctedFilePath);
                this.filePath=correctedFilePath;
            } catch (IOException e) {
                System.err.println("Failed to correct or load XML: \""+e.getMessage());
                throw e;
            }
        }else {
        CallValidator();
        }

    }
    @FXML
    private void CallJSON() throws IOException {
       XmlToJsonConverter xmlToJsonConverter =new XmlToJsonConverter();
        String jsonfile =  "src/main/resources/output.json";
        xmlToJsonConverter.JSONMain(filePath,jsonfile);
        loadFileContent(jsonfile);
    }
    @FXML
    private void Call_XMLCompression(){
        boolean check;
        Compression compression =new Compression();
        check =compression.compressXML(filePath);
        if(!check)
        {
            System.err.println("F");
        }
        else{
            compression.printHuffmanCodes();
            loadFileContent("src/main/resourcesXMLXML.comp");
        }

    }
    @FXML
    private void Call_JsonCompression(){
        boolean check;
        Compression compression =new Compression();
        check =compression.compressJSON(filePath);
        if(!check)
        {
            System.err.println("F");
        }
        else{
            compression.printHuffmanCodes();
            loadFileContent("src/main/resources/JsonJSON.comp");
        }

    }
    @FXML
    private void Call_JsonDecompressor()  {
        JsonDecompressor jsonDecompressor = new JsonDecompressor();
        String compressedPath = "src/main/resources/JsonJSON.comp";
        String DecompressedPath = "src/main/resources/JsonDecompressed.json";
        String KeyInputFile ="src/main/resources/Json_keyJSON.comp";
        jsonDecompressor.processDecompression(KeyInputFile,compressedPath,DecompressedPath);
        loadFileContent(DecompressedPath);
    }
    @FXML
    private void Call_XmlDecompression() throws IOException {
        String compressedPath = "src/main/resourcesXMLXML.comp";
        String DecompressedPath = "src/main/resources/XMLDecompressed.xml";
        String KeyInputFile ="src/main/resourcesXML_keyXML.comp";
        try {
            // Create an instance of the decompressor class
            XMLDecompressor decompressor = new XMLDecompressor();

            // Load the mappings from the KeyFileXML.txt
            decompressor.loadKeyFile(KeyInputFile);

            // Read the compressed file
            String compressedXML = decompressor.readFile(compressedPath);

            // Decompress the XML content
            String decompressedXML = decompressor.decompress(compressedXML);

            // Write the decompressed XML to the output file
            decompressor.writeFile(DecompressedPath, decompressedXML);

            // Notify the user that the decompression was successful
            System.out.println(STR."Decompressed XML written to: \{DecompressedPath}");

        } catch (IOException e) {
            System.err.println(STR."Error reading or writing the file: \{e.getMessage()}");
        } catch (Exception e) {
            System.err.println(STR."Error decompressing the file: \{e.getMessage()}");
        }
        loadFileContent(DecompressedPath);
    }


    @FXML
    private void Call_Prettier(){
        Prettifies prettifies =new Prettifies();
        String prettyOut = prettifies.Pretty(filePath);
        contentArea.setText(prettyOut);
    }

    @FXML
    private void Most_Active(){
        performNetworkAnalysis(network_command[0],filePath,"");
    }
    @FXML private void Most_Influencer(){
        performNetworkAnalysis(network_command[1],filePath,"");
    }
    @FXML private void Mutual(){
        String ids =GetIDS();
        if(!(ids==null||ids.isEmpty())){
            performNetworkAnalysis(network_command[2],filePath,ids);
        }

    }
    @FXML private void Suggest(){
        String ids =GetIDS();
        if(!(ids==null||ids.isEmpty())){
            performNetworkAnalysis(network_command[3],filePath,ids);
        }
    }
    @FXML
    private void performNetworkAnalysis(String task, String inputFilePath,String ids) {
        try {
            // Redirect output to the TextArea
            StringBuilder output = new StringBuilder();
            xml_editor.startNetworkAnalysis(task, inputFilePath, output,ids);
            Stage NetworkStage = new Stage();
            Scene scene = getNetworkScene(output);
            NetworkStage.setScene(scene);
            NetworkStage.setTitle("NetworkAnalysis");
            Image icon = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/icons/networking.png")));
            NetworkStage.getIcons().add(icon);
            NetworkStage.show();
        } catch (Exception e) {
            Stage NetworkStage = new Stage();
            Scene scene = getNetworkScene(new StringBuilder("An error occurred: \""+e.getMessage()));
            NetworkStage.setScene(scene);
            NetworkStage.setTitle("NetworkAnalysis");
        }
    }
    @FXML
    private void Call_PostSearch(){
        PostSearchSimplified postSearchSimplified = new PostSearchSimplified();
        postSearchSimplified.PostSearch(filePath);
    }
    @FXML
    private void Call_GraphVisualizer() {
        try {
            GraphVisualizer graphVisualizer = new GraphVisualizer();
            graphVisualizer.Start_GraphVisualization(filePath);

            // Load the saved graph image
            File imageFile = new File("src/main/resources/graph.jpg");
            if (!imageFile.exists()) {
                System.err.println("Error: Graph image file not found at \""+imageFile.getAbsolutePath());
                return;
            }

            Image graphImage = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(graphImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(700); // Adjust image size to fit the scene

            // Create a styled VBox to display the image
            VBox graphVbox = new VBox(20, imageView);
            graphVbox.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #2b2b2b;");
            graphVbox.setSpacing(20);

            // Set up the scene and stage
            Scene graphScene = new Scene(graphVbox, 800, 600);
            Stage graphStage = new Stage();
            graphStage.setScene(graphScene);
            graphStage.setTitle("Graph Visualization");

            // Load the window icon
            try {
                Image icon = new Image(Objects.requireNonNull(
                        HelloApplication.class.getResourceAsStream("/icons/connection.png")
                ));
                graphStage.getIcons().add(icon);
            } catch (NullPointerException e) {
                System.err.println("Warning: Icon file not found. Skipping icon setup.");
            }

            graphStage.show();
        } catch (Exception e) {
            System.err.println("An error occurred while displaying the graph visualization:");
            e.printStackTrace();
        }
    }


    private static Scene getNetworkScene(StringBuilder outPut) {
        TextArea result = new TextArea();
        result.setStyle(" -fx-text-fill: black; -fx-background-color: #1e1e1e; -fx-background-radius: 10; -fx-border-color: #5B5BF0; -fx-border-radius: 10;-fx-border-width: 2; -fx-font-size: 14;-fx-padding: 10;-fx-pref-width: 600;-fx-pref-height: 400;");
        result.setWrapText(true);
        result.setEditable(false);
        result.setText(outPut.toString());
        VBox layout = new VBox(20, result);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #2b2b2b;");
        layout.setSpacing(20);
        return new Scene(layout, 800, 600);

    }
    public String GetIDS() {
        // Create a final mutable reference to store the input
        final String[] inputValue = {null};


        // Create a Stage object to control the window
        Stage idStage = new Stage();
        idStage.initModality(Modality.APPLICATION_MODAL); // Ensure modal dialog
        Image icon = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/icons/networking.png")));
        idStage.getIcons().add(icon);
        TextField inputField = new TextField();
        Platform.runLater(() -> inputField.getScene().getRoot().requestFocus());
        inputField.setPromptText("Enter IDs (comma-separated for mutual)");

        // Add key pressed event to capture Enter key
        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String input = inputField.getText().trim();
                if (!input.isEmpty()) {
                    inputValue[0] = input;
                    idStage.close();  // Close the window after capturing input
                } else {
                    inputField.setStyle("-fx-border-color: red;");
                    inputField.setPromptText("Input cannot be empty.");
                }
            }
        });

        // Add a submit button for alternative input method
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                inputValue[0] = input;
                idStage.close();
            } else {
                inputField.setStyle("-fx-border-color: red;");
                inputField.setPromptText("Input cannot be empty.");
            }
        });

        // Set up the layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(inputField, submitButton);

        // Set up the scene and show the stage
        Scene scene = new Scene(layout, 300, 150);
        idStage.setTitle("IDs");
        idStage.setScene(scene);

        // Show and wait for the dialog to close
        idStage.showAndWait();

        // Return the captured input, or null if no input was provided
        return inputValue[0];
    }



}
