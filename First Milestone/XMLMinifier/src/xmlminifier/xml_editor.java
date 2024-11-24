/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ini;
/**
 *
 * @author Moham
 */
import java.io.IOException;

public class xml_editor  {

    public static void main(String[] args) {
        String inputFilePath = null;
        String outputFilePath = null;
        // Parse the command-line arguments
        if (!args[0].equals("mini")) {
            System.out.println("Unknown command. Only 'mini' is supported.");
            return;
        }
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-i") && i + 1 < args.length) {
                inputFilePath = args[i + 1];
            } else if (args[i].equals("-o") && i + 1 < args.length) {
                outputFilePath = args[i + 1];
            }
        }
        try {
            // Call the minifyFile method from the XMLMinifier class
            XMLMinifier.minifyFile(inputFilePath, outputFilePath);
            System.out.println("File has been minified successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred during file processing: " + e.getMessage());
        }
    }
}

