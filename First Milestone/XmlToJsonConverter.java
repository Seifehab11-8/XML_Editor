package df;

import java.io.*;
import java.util.*;

public class XmlToJsonConverter {
    public static void main(String[] args) {
        // File path to the XML text file
        String textFilePath = "C:/mine/dsa labs/test.txt";

        try {
            // Read the XML content from the file
            String xmlContent = readFileAsString(textFilePath);

            // Convert XML to JSON
            String jsonOutput = convertXmlToJson(xmlContent);

            // Print the JSON output
            System.out.println("JSON Output:");
            System.out.println(jsonOutput);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    
    private static String readFileAsString(String filePath) throws IOException {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line.trim());
            }
        }
        return builder.toString();
    }

   
    private static String convertXmlToJson(String xml) {
        if (xml.startsWith("<?xml")) {
            xml = xml.substring(xml.indexOf("?>") + 2).trim(); // Remove XML declaration
        }

        Stack<String> openTags = new Stack<>();  // To keep track of open tags
        List<String> tags = new ArrayList<>();   // To store tags
        List<String> values = new ArrayList<>(); // To store values
        List<List<String>> nestedTags = new ArrayList<>(); // To store nested tags (arrays)
        StringBuilder currentText = new StringBuilder();  
        int i = 0;

        while (i < xml.length()) {
            if (xml.charAt(i) == '<') {
                // Handle closing tag
                if (xml.charAt(i + 1) == '/') {
                    int end = xml.indexOf('>', i);
                    String closingTag = xml.substring(i + 2, end).trim();

                    if (!openTags.isEmpty() && closingTag.equals(openTags.peek())) {
                        if (currentText.length() > 0) {
                            
                            tags.add(openTags.peek());
                            values.add(currentText.toString().trim());
                        }
                        currentText.setLength(0); 
                        openTags.pop(); // Remove the tag from the stack
                    }
                    i = end + 1;
                }
                // Handle opening tag
                else {
                    int end = xml.indexOf('>', i);
                    String openingTag = xml.substring(i + 1, end).trim();
                    openTags.push(openingTag); 
                    nestedTags.add(new ArrayList<>());  
                    i = end + 1;

                    
                    if (xml.charAt(end - 1) == '/') {
                        tags.add(openingTag);
                        values.add("");
                    }
                }
            } else {
                // Collect text content
                currentText.append(xml.charAt(i));
                i++;
            }
        }


        return formatJson(tags, values, nestedTags);
    }

    
    private static String formatJson(List<String> tags, List<String> values, List<List<String>> nestedTags) {
        StringBuilder json = new StringBuilder("{\n");

        int i = 0;
        while (i < tags.size()) {
            json.append("  \"").append(tags.get(i)).append("\": ");

            
            if (nestedTags.get(i).size() > 0) {
                json.append("[\n");
                for (String nestedTag : nestedTags.get(i)) {
                    json.append("    {\n");
                    json.append("      \"").append(nestedTag).append("\": \"").append(values.get(i)).append("\"\n");
                    json.append("    },\n");
                }
                json.deleteCharAt(json.length() - 2); 
                json.append("  ]");
            } else {
                json.append("\"").append(values.get(i)).append("\"");
            }

            if (i < tags.size() - 1) {
                json.append(",");
            }
            json.append("\n");
            i++;
        }

        json.append("}");
        return json.toString();
    }
}
