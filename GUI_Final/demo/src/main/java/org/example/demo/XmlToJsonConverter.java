package org.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class XmlToJsonConverter {
    public  void JSONMain(String InputFilePath, String outputFilePath) throws IOException {
        try {
            String xmlData = readFile(InputFilePath);
            XmlElement root = parseXml(xmlData);
            String json = xmlToJson(root);
            Files.write(Paths.get(outputFilePath), json.getBytes());
        } catch (Exception e) {
            System.err.println("Failed to correct or load XML: "+e.getMessage());
            throw e;
        }
    }


    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        // create bufferreader object to read file with given path
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            //make loop to itrates to all the lines in the file
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    //  represent an XML element
    static class XmlElement {
        String tagName;
        String textContent;  // value inbetween the tags
        List<XmlElement> children; // as in the tree (parent,child)

        public XmlElement(String tagName) {
            this.tagName = tagName;
            this.textContent = "";
            this.children = new ArrayList<>();
        }
    }

    // Function to parse XML to return xml elemnt of root with all its children
    private static XmlElement parseXml(String xmlData) {
        Stack<XmlElement> stack = new Stack<>();
        XmlElement root = null;

        // Split the XML into tokens (tags and text content)
        String[] tokens = xmlData.split("(?=<)|(?<=>)");

        for (String token : tokens) {
            token = token.trim(); //to remove extra spaces
            if (token.isEmpty()) continue;

            if (token.startsWith("<") && token.endsWith(">")) {
                if (token.startsWith("</")) {
                    // Closing tag
                    XmlElement element = stack.pop();
                    if (stack.isEmpty()) {
                        root = element; // This is the root element
                    } else {
                        stack.peek().children.add(element); // Add to parent's children
                    }
                }
                else {
                    // Opening tag
                    String tagName = token.substring(1, token.length() - 1).trim(); // to contain tag name from the token
                    XmlElement element = new XmlElement(tagName);
                    stack.push(element);
                }
            }
            else {
                // Text content
                if (!stack.isEmpty()) {
                    stack.peek().textContent += token.trim();
                }
            }
        }

        return root;
    }

    // Function to convert the xml to JSON by passing only the root of tree
    private static String xmlToJson(XmlElement element) {
        StringBuilder json = new StringBuilder(); //to append the json format in it
        json.append("{\n"); // the start of the output

        boolean[] processed = new boolean[element.children.size()]; // to make sure we itrated on all childerns

        boolean first = true;

        for (int i = 0; i < element.children.size(); i++) {
            if (processed[i]) continue;

            XmlElement child = element.children.get(i);

            // Find repeated children
            List<XmlElement> repeatedChildren = new ArrayList<>();
            for (int j = i; j < element.children.size(); j++) {
                //check if the child is repeated or no to add it in the repeatedchildern list
                if (!processed[j] && element.children.get(j).tagName.equals(child.tagName)) {
                    repeatedChildren.add(element.children.get(j));
                    processed[j] = true;
                }
            }

            if (!first) json.append(",\n");
            first = false;

            if (repeatedChildren.size() > 1) {
                // Handle repeated elements as an array
                json.append("  \"").append(child.tagName).append("\": [\n");
                for (int k = 0; k < repeatedChildren.size(); k++) {
                    if (k > 0) json.append(",\n");
                    json.append(xmlToJson(repeatedChildren.get(k)));
                }
                json.append("\n  ]");
            } else {
                // Single element
                XmlElement singleElement = repeatedChildren.get(0);
                if (singleElement.children.isEmpty()) {
                    // Direct value (no children)
                    json.append("  \"").append(singleElement.tagName).append("\": \"")
                            .append(singleElement.textContent.replace("\"", "\\\""))
                            .append("\"");
                } else {
                    // Nested object repeat with recurion
                    json.append("  \"").append(singleElement.tagName).append("\": ").append(xmlToJson(singleElement));
                }
            }
        }

        // Add text content directly if only thr root exist with no childs and grandchilds
        if (!element.textContent.isEmpty() && element.children.isEmpty()) {
            if (!first) json.append(",\n");
            json.append("  \"").append(element.tagName).append("\": \"")
                    .append(element.textContent.replace("\"", "\\\""))
                    .append("\"");
        }

        json.append("\n}"); // end of the output
        return json.toString();
    }
}


