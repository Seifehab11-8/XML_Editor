import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLDecompressor {

    // Method to decompress the compressed XML content
    public String decompress(String compressedXML) {
        // Check if the input is valid (not null or empty)
        if (compressedXML == null || compressedXML.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Step 1: Replace short tags with full tags
        String decompressed = replaceTags(compressedXML);

        // Step 2: Expand compressed text (e.g., a4 -> aaaa)
        decompressed = expandCompressedText(decompressed);

        // Return the fully decompressed content
        return decompressed;
    }

    // Replace abbreviated tags (like <u>) with original ones (like <user>)
    private String replaceTags(String input) {
        return input
            .replace("<u>", "<user>")
            .replace("</u>", "</user>")
            .replace("<i>", "<id>")
            .replace("</i>", "</id>")
            .replace("<p>", "<post>")
            .replace("</p>", "</post>")
            .replace("<f>", "<follower>")
            .replace("</f>", "</follower>");
    }

    // Expand repeated characters in the text (e.g., a4 -> aaaa)
    private String expandCompressedText(String input) {
        // Use a regular expression to find patterns like a4
        Pattern pattern = Pattern.compile("([a-zA-Z])\\d+");
        Matcher matcher = pattern.matcher(input);
        StringBuffer result = new StringBuffer();

        // Process each match found by the regex
        while (matcher.find()) {
            String charToRepeat = matcher.group(1); // Get the character to repeat
            int count = Integer.parseInt(matcher.group().substring(1)); // Get the number after the character
            matcher.appendReplacement(result, charToRepeat.repeat(count)); // Replace with repeated characters
        }

        // Append the rest of the text that was not matched
        matcher.appendTail(result);
        return result.toString();
    }

    // Read the content of a file from the given file path
    public String readFile(String filePath) throws IOException {
        // Read all bytes from the file and convert to a string
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    // Write the decompressed content to a file
    public void writeFile(String filePath, String content) throws IOException {
        // Write the content to the specified file, overwriting if it exists
        Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}

    public static void main(String[] args) {
        // Check if the user provided the input and output file paths
        if (args.length < 2) {
            System.out.println("Usage: java XMLDecompressor <path_to_compressed_xml_file> <path_to_decompressed_output_file>");
            return;
        }

        // Get the input and output file paths from the arguments
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        try {
            // Create an instance of the decompressor
            XMLDecompressor decompressor = new XMLDecompressor();

            // Read the compressed file
            String compressedXML = decompressor.readFile(inputFilePath);

            // Decompress the file content
            String decompressedXML = decompressor.decompress(compressedXML);

            // Write the decompressed content to the output file
            decompressor.writeFile(outputFilePath, decompressedXML);

            // Notify the user of success
            System.out.println("Decompressed XML written to: " + outputFilePath);
        } catch (IOException e) {
            // Handle file reading/writing errors
            System.err.println("Error reading or writing the file: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other errors
            System.err.println("Error decompressing the file: " + e.getMessage());
        }
    }
}
