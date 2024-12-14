package org.example.demo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class XMLMinifier {
    public  void minifyFile(String inputFilePath, String outputFilePath) throws IOException {
        FileInputStream input_file = new FileInputStream(inputFilePath);
        List<Byte> byteList = new ArrayList<>(); // Dynamic array to store bytes
        int data;
        String start_comment = "<!--";
        String end_comment = "-->";
        int start_counter = 0, end_counter = 0;
        boolean start_comment_removed = false;

        while ((data = input_file.read()) != -1) {
            // Started comment, wait until finding the end comment
            if (start_counter == 4) {
                // Remove the start of the comment saved in the list
                if (!start_comment_removed) {
                    for (int i = 0; i < 4; i++) {
                        byteList.removeLast(); // Remove the last element
                    }
                    start_comment_removed = true;
                }
                // Searching for the end of the comment
                if (data == end_comment.charAt(end_counter)) {
                    end_counter++;
                } else {
                    end_counter = 0;
                }
            } else {
                // Search for the start comment
                if (data == start_comment.charAt(start_counter)) {
                    start_counter++;
                } else {
                    start_counter = 0;
                }
                // Save data to the list (excluding spaces, newlines, and carriage returns)
                if (data != 32 && data != 10 && data != 13) { // 32 is ASCII for space, 10 and 13 are ASCII for newline
                    byteList.add((byte) data);
                }
            }

            // End comment reached; continue saving file to the list
            if (end_counter == 3) {
                start_counter = 0;
                end_counter = 0;
                start_comment_removed = false;
            }
        }

        // Close the input file stream
        input_file.close();

        // Write the processed data to the output file
        FileOutputStream output_file = new FileOutputStream(outputFilePath);
        for (Byte aByte : byteList) {
            output_file.write(aByte);
        }
        output_file.flush(); // Ensure the stream is emptied
        output_file.close();
    }
}