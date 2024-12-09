
import java.io.*;
import java.util.*;
public class XMLCorrection {

    // Method to correct the XML file
    public static String correctXML(String filePath) {
        Stack<String> tagStack = new Stack<>();
        StringBuilder correctedXML = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove unnecessary spaces

                // Process each line to find tags
                while (line.contains("<")) {
                    int start = line.indexOf("<");
                    int end = line.indexOf(">", start);

                    if (end == -1) {
                        throw new IllegalArgumentException("Invalid XML: Missing closing '>'");
                    }

                    String tag = line.substring(start + 1, end).trim();
                    line = line.substring(end + 1); // Move to the rest of the line

                    correctedXML.append(line, 0, start); // Append content before the tag
                    correctedXML.append("<").append(tag).append(">");

                    // Handle closing tags
                    if (tag.startsWith("/")) {
                        String tagName = tag.substring(1); // Remove '/'
                        if (tagStack.isEmpty() || !tagStack.peek().equals(tagName)) {
                            // Fix mismatched or missing closing tag
                            correctedXML.append("</").append(tagStack.pop()).append(">");
                        } else {
                            tagStack.pop();
                        }
                    }
                    // Handle opening tags
                    else if (!tag.endsWith("/")) { // Ignore self-closing tags
                        tagStack.push(tag);
                    }
                }

                correctedXML.append(line); // Append remaining line content
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        // Add missing closing tags
        while (!tagStack.isEmpty()) {
            correctedXML.append("</").append(tagStack.pop()).append(">");
        }

        return correctedXML.toString();
    }
}