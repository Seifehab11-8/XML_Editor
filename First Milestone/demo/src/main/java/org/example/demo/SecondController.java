package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.io.IOException;


public class SecondController1 {
    HelloController objectController = new HelloController();
    @FXML
    private Button runButton;

    @FXML
    private Button stopButton;

    @FXML
    private void onRunButtonClick() {
        // Your run code here
    }

    @FXML
    private void onStopButtonClick() {
        // Your stop code here
    }
    @FXML
    private  void initialize() throws IOException {
       try {
           TextArea textArea = new TextArea();
           textArea.setText(objectController.getContentofFile());
           textArea.setWrapText(true);
           textArea.setPrefRowCount(50);
           textArea.setPrefColumnCount(50);
       }catch (IOException e){
           e.printStackTrace();;

       }
    }
}
