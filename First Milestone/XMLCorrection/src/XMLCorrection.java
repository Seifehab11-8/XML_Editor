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

                // Skip comments
                if (line.startsWith("<!--") && line.endsWith("-->")) {
                    continue; // Ignore the line completely if it's a comment
                }

                // Process each line to find tags
                while (line.contains("<")) {
                    int start = line.indexOf("<");
                    int end = line.indexOf(">", start);

                    if (end == -1) {
                        System.out.println("Error: Missing closing '>' in line: " + line);
                        line = line.substring(0, start); // Remove invalid tag
                        break;
                    }

                    correctedXML.append(line, 0, start); // Append content before the tag
                    String tag = line.substring(start + 1, end).trim();
                    line = line.substring(end + 1); // Remaining part of the line

                    // Handle tags
                    if (tag.startsWith("/")) { // Closing tag
                        String tagName = tag.substring(1);
                        if (!tagStack.isEmpty() && tagStack.peek().equals(tagName)) {
                            correctedXML.append("<").append(tag).append(">\n");
                            tagStack.pop(); // Correctly matched
                        } else {
                            System.out.println("Error: Mismatched closing tag </" + tagName + ">");
                            // Skip mismatched tag
                        }
                    } else if (!tag.endsWith("/")) { // Opening tag (ignore self-closing tags)
                        correctedXML.append("<").append(tag).append(">\n");
                        tagStack.push(tag);
                    }
                }

                if (!line.isBlank()) {
                    correctedXML.append(line).append("\n"); // Append remaining line content with a newline
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        // Add missing closing tags
        while (!tagStack.isEmpty()) {
            correctedXML.append("</").append(tagStack.pop()).append(">\n");
        }

        return correctedXML.toString();
    }

}
