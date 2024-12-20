import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class Decompression {

    private HashMap<Character, String> huffmanCodeMap;
    private HashMap<String, Character> reverseHuffmanCodeMap;
    private List<BitSet> compressedData;
    private List<String> decodedData;

    public Decompression() {
        huffmanCodeMap = new HashMap<>();
        reverseHuffmanCodeMap = new HashMap<>();
        compressedData = new ArrayList<>();
        decodedData = new ArrayList<>();
    }

    // Load Huffman codes from a file
    public boolean loadHuffmanCodes(String keyFilePath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(keyFilePath))) {
            int character;
            while ((character = bis.read()) != -1) {
                char key = (char) character;
                int length = bis.read();
                byte[] bytes = new byte[length];
                bis.read(bytes);
                bis.read(); // Get rid of '\n'
                BitSet bitSet = BitSet.valueOf(bytes);
                StringBuilder code = new StringBuilder();
                for (int i = 0; i < bitSet.length() - 1; i++) {
                    code.append(bitSet.get(i) ? '1' : '0');
                }

                huffmanCodeMap.put(key, code.toString());
                reverseHuffmanCodeMap.put(code.toString(), key);
            }
        } catch (IOException e) {
            System.out.println("Error reading Huffman code file: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Load compressed data from a file
    public boolean loadCompressedData(String compressedFilePath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(compressedFilePath))) {
            int length;
            while ((length = bis.read()) != -1) {
                byte[] bytes = new byte[length];
                bis.read(bytes);
                bis.read(); // Get rid of '\n'              
                BitSet bitSet = BitSet.valueOf(bytes);
                compressedData.add(bitSet);
            }
        } catch (IOException e) {
            System.out.println("Error reading compressed file: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Decode the data using Huffman codes
    public boolean decodeData() {
        for (BitSet bitSet : compressedData) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bitSet.length() - 1; i++) {
                sb.append(bitSet.get(i) ? '1' : '0');
            }
            String decodedString = decodeHuffman(sb.toString());
            decodedData.add(decodedString);
        }
        return true;
    }

    // Decode a Huffman encoded string
    private String decodeHuffman(String encodedString) {
        StringBuilder decodedString = new StringBuilder();
        StringBuilder tempCode = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            tempCode.append(encodedString.charAt(i));
            if (reverseHuffmanCodeMap.containsKey(tempCode.toString())) {
                decodedString.append(reverseHuffmanCodeMap.get(tempCode.toString()));
                tempCode.setLength(0);  // Reset the temporary code builder
            }
        }
        return decodedString.toString();
    }

    // Save the decoded data to a file
    public boolean saveDecodedData(String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String line : decodedData) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing decoded data: " + e.getMessage());
            return false;
        }
        return true;
    }
}

