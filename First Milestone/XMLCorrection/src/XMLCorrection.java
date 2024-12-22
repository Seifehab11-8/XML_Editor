import java.io.*;
import java.util.*;
import java.util.regex.*;

public class XMLCorrection {

    public static String correctXML(String filePath) {
        StringBuilder correctedXML = new StringBuilder();
        StringBuilder xmlContent = new StringBuilder();

        // Read the entire XML file into a string
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "";
        }

        String xmlString = xmlContent.toString();
        Pattern openingTagPattern = Pattern.compile("<([a-zA-Z0-9]+)[^/>]*>");
        Pattern closingTagPattern = Pattern.compile("</([a-zA-Z0-9]+)>");

        Matcher openingMatcher;
        Matcher closingMatcher;

        Map<String, Integer> tagCounts = new HashMap<>();
        Map<String, Integer> closingTagCounts = new HashMap<>();

        // Count opening and closing tags
        openingMatcher = openingTagPattern.matcher(xmlString);
        while (openingMatcher.find()) {
            String tag = openingMatcher.group(1);
            tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
        }

        closingMatcher = closingTagPattern.matcher(xmlString);
        while (closingMatcher.find()) {
            String tag = closingMatcher.group(1);
            closingTagCounts.put(tag, closingTagCounts.getOrDefault(tag, 0) + 1);
        }

        // Parse XML and correct missing closing tags
        int index = 0;
        while (index < xmlString.length()) {
            openingMatcher.region(index, xmlString.length());

            if (openingMatcher.find()) {
                String openingTag = openingMatcher.group(1);
                correctedXML.append(xmlString, index, openingMatcher.start()).append("\n"); // Add content before the opening tag
                correctedXML.append("<").append(openingTag).append(">\n"); // Add the opening tag
                index = openingMatcher.end();

                // Check if this tag is missing a closing tag
                int openingCount = tagCounts.getOrDefault(openingTag, 0);
                int closingCount = closingTagCounts.getOrDefault(openingTag, 0);

                if (closingCount < openingCount) {
                    // Check for the next opening tag or EOF
                    int nextOpeningTagIndex = xmlString.indexOf('<', index);

                    if (nextOpeningTagIndex == -1) {
                        // No more tags, close at the end
                        correctedXML.append(xmlString.substring(index).trim()).append("\n");
                        correctedXML.append("</").append(openingTag).append(">\n");
                        index = xmlString.length();
                    } else {
                        // Add content between and close the tag
                        correctedXML.append(xmlString, index, nextOpeningTagIndex).append("\n");
                        correctedXML.append("</").append(openingTag).append(">\n");

                        // Update the count
                        closingTagCounts.put(openingTag, closingTagCounts.getOrDefault(openingTag, 0) + 1);
                        index = nextOpeningTagIndex;
                    }
                }
            } else {
                // Append any remaining content after the last tag
                correctedXML.append(xmlString.substring(index));
                break;
            }
        }

        // Add any remaining missing closing tags
        for (String tag : tagCounts.keySet()) {
            int openingCount = tagCounts.getOrDefault(tag, 0);
            int closingCount = closingTagCounts.getOrDefault(tag, 0);

            while (closingCount < openingCount) {
                correctedXML.append("</").append(tag).append(">\n");
                closingCount++;
            }
        }

        return correctedXML.toString();
    }

    public static void main(String[] args) {

        String inputFilePath = null;
        String outputFilePath = null;
        boolean fixErrors = false;

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i])) {
                inputFilePath = args[i + 1];
                i++; // Skip the next argument
            } else if ("-o".equals(args[i])) {
                outputFilePath = args[i + 1];
                i++; // Skip the next argument
            } else if ("-f".equals(args[i])) {
                fixErrors = true;
            }
        }

        if (inputFilePath == null || outputFilePath == null) {
            System.err.println("Input and output file paths are required.");
            return;
        }

        // Only correct XML if the -f flag is set
        if (fixErrors) {
            String correctedXML = correctXML(inputFilePath);
            if (!correctedXML.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                    // Write the corrected XML to the output file, one line at a time
                    String[] lines = correctedXML.split("\n");
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine(); // Ensure each line is written on a new line
                    }
                    System.out.println("Corrected XML written to " + outputFilePath);
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            } else {
                System.err.println("Failed to correct the XML. Please check the input file.");
            }
        } else {
            System.out.println("No changes made. Use the -f option to correct XML.");
        }
    }
}
