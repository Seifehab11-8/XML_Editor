import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class XMLDecompressor {

    private Map<String, String> tagMappings;

    // Constructor to initialize the tag mappings
    public XMLDecompressor() {
        this.tagMappings = new HashMap<>();
    }

    // Format the XML with proper indentation for better readability
    public String formatXML(String xml) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        String indent = "  "; // Indentation with 4 spaces

        String[] lines = xml.split("(?=<)|(?<=</?)>");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            if (line.isEmpty()) {
                continue;
            }

            // Decrease indent for closing or self-closing tags
            if (line.startsWith("</") || line.startsWith("/>")) {
                indentLevel--;
            }

            // Add the current indentation level before the line
            for (int j = 0; j < indentLevel; j++) {
                formatted.append(indent);
            }

            // Add the line to the formatted string
            formatted.append(line).append("\n");

            // Increase indent for opening tags
            if (line.startsWith("<") && !line.startsWith("</") && !line.startsWith("<!")) {
                indentLevel++;
            }
        }

        return formatted.toString();
    }

    // Load tag mappings from the key file
    public void loadKeyFile(String keyFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(keyFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() >= 3) {
                    String shortTag = line.substring(0, 2); // Short tag is the first two characters
                    String fullTag = line.substring(2); // The rest of the line is the full tag
                    tagMappings.put(shortTag, fullTag);
                }
            }
        }
    }

    // Decompress the XML by replacing short tags and expanding text
    public String decompress(String compressedXML) {
        if (compressedXML == null || compressedXML.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Replace short tags with full tags
        String decompressed = replaceTags(compressedXML);


        // Format the decompressed XML
        decompressed = formatXML(decompressed);

        return decompressed;
    }

    // Replace short tags with their corresponding full tags
    private String replaceTags(String input) {
        for (Map.Entry<String, String> entry : tagMappings.entrySet()) {
            String shortTag = entry.getKey();
            String fullTag = entry.getValue();
            input = input.replace(shortTag, fullTag);
        }
        return input;
    }

    // Read the content of the compressed XML file
    public String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    // Write the decompressed XML to a file
    public void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
