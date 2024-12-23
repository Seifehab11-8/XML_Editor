package org.example.demo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XMLDecompressor {
    public  void StartXmlDecompression(String inputFilePath,String outputFilePath) {

        try {
            // Create an instance of the decompressor class
            XMLDecompressor decompressor = new XMLDecompressor();

            // Read the compressed file
            String compressedXML = decompressor.readFile(inputFilePath);

            // Decompress the XML content
            String decompressedXML = decompressor.decompress(compressedXML);

            // Write the decompressed XML to the output file
            decompressor.writeFile(outputFilePath, decompressedXML);

            // Notify the user that the decompression was successful
            System.out.println("Decompressed XML written to: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Error reading or writing the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error decompressing the file: " + e.getMessage());
 }
}


    /* Method to decompress the compressed file */
    public String decompress(String compressedXML) {
        if (compressedXML == null || compressedXML.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Step 1: Replace short tags with full tags
        String decompressed = replaceTags(compressedXML);

        // Step 2: Expand compressed text (e.g., a4 -> aaaa)
        decompressed = expandCompressedText(decompressed);

        /* Return decompressed as a String */
        return decompressed;
    }

    /* Replace Tags Method
       Takes the input compressed file
       as parameter string
       and replaces the short tags
       with full tags
     */
    private String replaceTags(String input) {
        String[] shortTags = {"<u>", "</u>", "<us>", "</us>", "<i>", "</i>", "<p>", "</p>",
                "<f>", "</f>", "<fs>", "</fs>", "<t>", "</t>", "<ts>", "</ts>",
                "<b>", "</b>", "<ps>", "</ps>", "<name>", "</name>"};

        String[] fullTags = {"<user>", "</user>", "<users>", "</users>", "<id>", "</id>", "<post>", "</post>",
                "<follower>", "</follower>", "<followers>", "</followers>", "<topic>", "</topic>",
                "<topics>", "</topics>", "<body>", "</body>", "<posts>", "</posts>", "<name>", "</name>"};

        for (int i = 0; i < shortTags.length; i++) {
            input = input.replace(shortTags[i], fullTags[i]);
        }

        return input;
    }

    /* This Method Expand Text in the Compressed file
     Parameter is a string
     Ex: a4 = aaaa  */
    private String expandCompressedText(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];

            if (i + 1 < chars.length && Character.isDigit(chars[i + 1])) {
                int count = chars[i + 1] - '0'; // Convert char digit to integer

                for (int j = 0; j < count; j++) {
                    result.append(currentChar);
                }

                i++; // Skip the digit character
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    /* Read The content of the file [Compressed file] */
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

    /* Write the content of the file [DeCompressed file] */
    public void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
