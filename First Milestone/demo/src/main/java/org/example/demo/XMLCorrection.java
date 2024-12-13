package org.example.demo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class XMLCorrection {
    public void correctXML(String filePath, String outputFilePath) throws IOException {
        Stack<String> tagStack = new Stack<>();
        StringBuilder correctedXML = new StringBuilder();

        // Validate input file
        if (!Files.exists(Paths.get(filePath))) {
            throw new FileNotFoundException(STR."Input XML file not found: \{filePath}");
        }

        // Ensure output directory exists
        File outputFile = new File(outputFilePath);
        outputFile.getParentFile().mkdirs();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove unnecessary spaces

                // Process each line to find and handle tags
                while (line.contains("<")) {
                    int start = line.indexOf("<");
                    int end = line.indexOf(">", start);

                    if (end == -1) {
                        throw new IllegalArgumentException(STR."Invalid XML: Missing closing '>' in line: \{line}");
                    }

                    String tag = line.substring(start + 1, end).trim();
                    correctedXML.append(line, 0, start); // Append content before the tag
                    correctedXML.append("<").append(tag).append(">");

                    line = line.substring(end + 1); // Move to the rest of the line

                    // Handle closing tags
                    if (tag.startsWith("/")) {
                        String tagName = tag.substring(1); // Remove '/'
                        if (tagStack.isEmpty() || !tagStack.peek().equals(tagName)) {
                            // Fix mismatched or missing closing tag
                            if (!tagStack.isEmpty()) {
                                correctedXML.append("</").append(tagStack.pop()).append(">");
                            }
                        } else {
                            tagStack.pop();
                        }
                    }
                    // Handle opening tags
                    else if (!tag.endsWith("/")) { // Ignore self-closing tags
                        tagStack.push(tag);
                    }
                }

                correctedXML.append(line); // Append remaining line content after the last tag
            }
        } catch (IOException | IllegalArgumentException e) {
            // Log the error and rethrow for visibility
            System.err.println(STR."Error processing XML: \{e.getMessage()}");
            throw e;
        }

        // Add missing closing tags at the end
        while (!tagStack.isEmpty()) {
            correctedXML.append("</").append(tagStack.pop()).append(">");
        }

        // Write the corrected XML to the output file
        Files.write(Paths.get(outputFilePath), correctedXML.toString().getBytes());
    }



}
