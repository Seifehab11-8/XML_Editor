package DSAproject;

import java.io.*;
import java.util.*;


public class XMLValidator {

    // Method to validate the XML file
    public static boolean validateXML(String filePath) {
        Stack<String> tagStack = new Stack<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove unneeded spaces

                // Skip comment lines
                if (line.startsWith("<!--")) {
                    while (line != null && !line.contains("-->")) {
                        line = reader.readLine(); // Skip comment block
                    }
                    if (line != null) {
                        line = line.substring(line.indexOf("-->") + 3).trim();
                    }
                    if (line.isEmpty()) {
                        continue; // Skip to the next line if no content remains
                    }
                }

                // Process each line to find tags
                while (line.contains("<")) {
                    int start = line.indexOf("<");
                    int end = line.indexOf(">", start);

                    if (end == -1) {
                        System.out.println("Invalid: Missing closing '>'");
                        return false; // Missing closing tag
                    }

                    String tag = line.substring(start + 1, end).trim();
                    line = line.substring(end + 1); // Move to the rest of the line

                    // Handle closing tags
                    if (tag.startsWith("/")) {
                        String tagName = tag.substring(1); // Remove '/'
                        if (tagStack.isEmpty() || !tagStack.peek().equals(tagName)) {
                            System.out.println("Invalid: Mismatched closing tag </" + tagName + ">");
                            return false; // Mismatched closing tag
                        }
                        tagStack.pop(); // Pop the matched opening tag
                    }
                    // Handle opening tags
                    else if (!tag.endsWith("/")) { // Ignore self-closing tags
                        tagStack.push(tag); // Push opening tag onto the stack
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return false;
        }

        // If the stack is not empty, there are unmatched opening tags
        if (!tagStack.isEmpty()) {
            System.out.println("Invalid: Unmatched opening tag <" + tagStack.peek() + ">");
            return false;
        }

        return true; // XML is valid
    }

}
