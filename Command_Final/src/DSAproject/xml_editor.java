/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DSAproject;

/**
 *
 * @author Moham
 */
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.regex.*;
import java.io.*;
import java.util.*;

public class xml_editor {

    public static void main(String[] args) {
        File file = null; //input file
        File fileout = null; //output file
        String inputFilePath = null;
        String outputFilePath = null;
        String keyFilePath = null;
        String compressedFilePath = null;
        Compression c1 = new Compression();
        String data = null;
        myHashMap graph = new myHashMap();
        ArrayList<String> Users = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        String keyy;
        String task = args[0];
        String id = null;
        String ids = null;
        boolean fixErrors = false;
        // Parse the command-line arguments
        if (args[0].equals("mini")) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-i") && i + 1 < args.length) {
                    inputFilePath = args[i + 1];
                } else if (args[i].equals("-o") && i + 1 < args.length) {
                    outputFilePath = args[i + 1];
                }
            }
            try {
                // Call the minifyFile method from the XMLMinifier class
                XMLMinifier.minifyFile(inputFilePath, outputFilePath);
                System.out.println("File has been minified successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred during file processing: " + e.getMessage());
            }
        } 
        else if (args[0].equals("compress")) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-i") && i + 1 < args.length) {
                    inputFilePath = args[i + 1];
                } else if (args[i].equals("-o") && i + 1 < args.length) {
                    outputFilePath = args[i + 1];
                }
            }

            if (inputFilePath.contains("xml") && !inputFilePath.contains("json")) {
                c1.setOutputPath(outputFilePath);
                c1.compressXML(inputFilePath);
            } else {
                System.out.println("JSON compression");
                c1.setOutputPath(outputFilePath);
                c1.compressJSON(inputFilePath);
            }
            System.out.println("File has been compressed successfully.");
        }
        else if (args[0].equals("json")) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-i") && i + 1 < args.length) {
                    inputFilePath = args[i + 1];
                } else if (args[i].equals("-o") && i + 1 < args.length) {
                    outputFilePath = args[i + 1];
                }
            }

            try {
                data = XmlToJsonConverter.xmlToJson(
                    XmlToJsonConverter.parseXml(
                        XmlToJsonConverter.readFile(inputFilePath)
                    )
                );

                System.out.println("File has been converted successfully.");

                if (outputFilePath != null) {
                    try (FileWriter writer = new FileWriter(outputFilePath)) {
                        writer.write(data);
                        System.out.println("Output written to file: " + outputFilePath);
                    }
                } else {
                    System.out.println("Output file path not specified.");
                }
            } catch (IOException e) {
                System.err.println("An error occurred during file processing: " + e.getMessage());
            }
        } 
        else if (args[0].equals("most_active")) {
            if (args[1].equals("-i") && 2 < args.length) {
                inputFilePath = args[2];
            }
            try {
                graph = NetworkAnalysis.file_to_graph(inputFilePath);
                printGraph(graph);
            } catch (IOException e) {
                System.err.println("An error occurred during file processing: " + e.getMessage());
            }

            Users = XMLMostActive.MostActive(graph);
            System.out.println("Most active user(s):-");
            for (String user : Users) {
                user = getKeyById(graph, user);
                if (user != null && user.contains("-")) {
                    String[] parts = user.split("-");
                    if (parts.length == 2) {
                        System.out.println("Name: " + parts[0] + ", ID: " + parts[1]);
                    }
                }
            }
        } 
        else if (args[0].equals("most_influencer")) {
            if (args[1].equals("-i") && 2 < args.length) {
                inputFilePath = args[2];
            }
            try {
                graph = NetworkAnalysis.file_to_graph(inputFilePath);
                printGraph(graph);
            } catch (IOException e) {
                System.err.println("An error occurred during file processing: " + e.getMessage());
            }

            Users = XMLMostInfluencer.MostInfluencer(graph);
            System.out.println("Most influencer user(s):-");
            for (String user : Users) {
                if (user != null && user.contains("-")) {
                    String[] parts = user.split("-");
                    if (parts.length == 2) {
                        System.out.println("Name: " + parts[0] + ", ID: " + parts[1]);
                    }
                }
            }
        } 
        else if (args[0].equals("mutual")) {
            if (args[1].equals("-i") && 2 < args.length) {
                inputFilePath = args[2];
            }
            try {
                graph = NetworkAnalysis.file_to_graph(inputFilePath);
                printGraph(graph);
            } catch (IOException e) {
                System.err.println("An error occurred during file processing: " + e.getMessage());
            }

            if (args[3].equals("-ids") && 4 < args.length) {
                ids = args[4];
            }

            String[] keys_str = ids.split(",");
            for (String key : keys_str) {
                keys.add(getKeyById(graph, key));
            }

            Users = XMLMutualFollowers.MutualFollowers(graph, keys);
            if (Users.isEmpty()) {
                System.out.println("No Mutual followers between given ids");
            } else {
                System.out.println("Mutual follower(s):-");
                for (String user : Users) {
                    user = getKeyById(graph, user);
                    if (user != null && user.contains("-")) {
                        String[] parts = user.split("-");
                        if (parts.length == 2) {
                            System.out.println("Name: " + parts[0] + ", ID: " + parts[1]);
                        }
                    }
                }
            }
        } 
        else if (args[0].equals("suggest")) {
            if (args[1].equals("-i") && 2 < args.length) {
                inputFilePath = args[2];
            }
            try {
                graph = NetworkAnalysis.file_to_graph(inputFilePath);
                printGraph(graph);
            } catch (IOException e) {
                System.err.println("An error occurred during file processing: " + e.getMessage());
            }
            if (args[3].equals("-id") && 4 < args.length) {
                id = args[4];
            }
            keyy = getKeyById(graph, id);
            Users = XMLSuggestFollowers.MutualFollowers(graph, keyy);
            if (Users.isEmpty()) {
                System.out.println("No Suggested followers");
            } else {
                System.out.println("Suggested follower(s):-");
                for (String user : Users) {
                    user = getKeyById(graph, user);
                    if (user != null && user.contains("-")) {
                        String[] parts = user.split("-");
                        if (parts.length == 2) {
                            System.out.println("Name: " + parts[0] + ", ID: " + parts[1]);
                        }
                    }
                }
            }
        } 
        else if (args[0].equals("decompressJSON")) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-k") && i + 1 < args.length) {
                    keyFilePath = args[i + 1];
                } else if (args[i].equals("-c") && i + 1 < args.length) {
                    compressedFilePath = args[i + 1];
                } else if (args[i].equals("-o") && i + 1 < args.length) {
                    outputFilePath = args[i + 1];
                }
            }
            // Validate required arguments
            if (keyFilePath == null || compressedFilePath == null || outputFilePath == null) {
                System.out.println("Usage: decompressXML -k <keyFilePath> -c <compressedFilePath> -o <outputFilePath>");
                return;
            }

            try {
                // Create an instance of the Decompression class
                Decompression decompression = new Decompression();

                // Load Huffman codes and compressed data
                if (decompression.loadHuffmanCodes(keyFilePath) && decompression.loadCompressedData(compressedFilePath)) {
                    // Decode the data
                    decompression.decodeData();

                    // Save the decompressed data
                    decompression.saveDecodedData(outputFilePath);

                    // Notify the user that the decompression was successful
                    System.out.println("Decompression completed successfully.");
                } else {
                    System.out.println("Decompression failed.");
                }

            } catch (Exception e) {
                System.err.println("Error during decompression: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else if (args[0].equals("decompressXML")) {
            // Parse the command-line arguments
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-k") && i + 1 < args.length) {
                    keyFilePath = args[i + 1];
                    i++;
                } else if (args[i].equals("-c") && i + 1 < args.length) {
                    compressedFilePath = args[i + 1];
                    i++;
                } else if (args[i].equals("-o") && i + 1 < args.length) {
                    outputFilePath = args[i + 1];
                    i++;
                } else {
                    System.err.println("Invalid argument: " + args[i]);
                    return;
                }
            }

            // Validate required arguments
            if (keyFilePath == null || compressedFilePath == null || outputFilePath == null) {
                System.err.println("Usage: java Main decompressXML -k <keyFilePath> -c <compressedFilePath> -o <outputFilePath>");
                return;
            }

            try {
                // Create an instance of the decompressor class
                XMLDecompressor decompressor = new XMLDecompressor();

                // Load the mappings from the KeyFileXML.txt
                decompressor.loadKeyFile(keyFilePath);

                // Read the compressed file
                String compressedXML = decompressor.readFile(compressedFilePath);

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
        else if (args[0].equals("prettify")) {
            prettify pretty = new prettify();
            System.out.println("Requested prettifying!" + args[0]);
            // Ensure arguments order
            // prettify -i in -o out
            if (args[1].equals("-i") && args[3].equals("-o")) {
                try {
                    pretty.prettifyFile(args[2], args[4]);
                } catch (IOException e) {
                    System.out.println("prettifier err!");
                    return;
                }
            }
            // prettify -o out -i in 
            else if (args[3].equals("-i") && args[1].equals("-o")) {
                try {
                    pretty.prettifyFile(args[4], args[2]);
                } catch (IOException e) {
                    System.out.println("prettifier err!");
                    return;
                }
            } else {
                System.out.println("parameters not met!");
                return;
            }
        }
        else if (args[0].equals("verify")) {
            boolean valid;
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
         if (fixErrors) {

                // Validate required arguments
                if (inputFilePath == null || outputFilePath == null) {
                    System.err.println("Usage: xml_editor verify -i input_file.xml -f -o output_file.xml");
                    return;
                }

                // Fix XML if the -f flag is set

                    String correctedXML = XMLCorrection.correctXML(inputFilePath);
                    if (!correctedXML.isEmpty()) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                            // Write the corrected XML to the output file with formatting
                            for (char c : correctedXML.toCharArray()) {
                                if (c == '<') {
                                    writer.newLine(); // Add a newline before the tag
                                    writer.write(c);
                                }  else {
                                    writer.write(c);
                                }
                            }
                            System.out.println("Corrected XML written to " + outputFilePath);
                        } catch (IOException e) {
                            System.err.println("Error writing to file: " + e.getMessage());
                        }
                    } else {
                        System.err.println("Failed to correct the XML. Please check the input file.");
                    }
         }
         else{
            valid=XMLValidator.validateXML(inputFilePath);
            if(valid)
            {
                 System.out.println("The XML file is valid.");
            }
            else{
                 System.out.println("The XML file is not valid.");
            }
         }
        }
        else if (args[0].equals("search") ){
            String searchType = args[1];
                String query = args[2];
                String filePath = args[4];

                List<PostSearchSimplified.User> users = PostSearchSimplified.parseXML(filePath);

                if (searchType.equals("-w")) {
                   PostSearchSimplified.searchByWord(users, query);
                } else if (searchType.equals("-t")) {
                    PostSearchSimplified.searchByTopic(users, query);
                } else {
                    System.out.println("Invalid search type. Use -w for word or -t for topic.");
                }
        }
            else {
                System.out.println("Unknown command, not supported.");
            }
    }
        public static void printGraph(myHashMap graph) {
        System.out.println("Graph Representation (Adjacency List):");

        for (String key : graph.keys) { // Iterate through all keys
            System.out.print(key + " -> ");
            ArrayList<String> followers = graph.getValue(key); // Get the followers list
            if (followers != null) {
                for (String follower : followers) { // Iterate and print each follower
                    System.out.print(follower + " ");
                }
            }
            System.out.println(); // New line after each key's followers
        }
    }

    public static String getKeyById(myHashMap graph, String id) {
        for (String key : graph.keys) {
            String[] parts = key.split("-");
            if (parts.length == 2 && parts[1].equals(id)) {
                return key;
            }
        }
        return null;
    }
}
