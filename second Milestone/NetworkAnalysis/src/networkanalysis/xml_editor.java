package networkanalysis;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Moham
 */
import java.io.IOException;
import java.util.ArrayList;

public class xml_editor {

    public static void main(String[] args) {
        myHashMap graph = new myHashMap();
        ArrayList<String> Users = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        String keyy;
        String inputFilePath = null;
        String task = args[0];
        String id = null;
        String ids = null;
        // Parse the command-line arguments
        if (!task.equals("most_active") && !task.equals("most_influencer") && !task.equals("mutual") && !task.equals("suggest")) {
            System.out.println("Unknown command. Only 'most_active,most_influencer,mutual,suggest' is supported.");
            return;
        }
        if (args[1].equals("-i") && 2 < args.length) {
            inputFilePath = args[2];
        }
        try {
            // Call the file_to_graph method from the NetworkAnalysis class
            graph = NetworkAnalysis.file_to_graph(inputFilePath);
            printGraph(graph);
        } catch (IOException e) {
            System.err.println("An error occurred during file processing: " + e.getMessage());
        }
        if (task.equals("most_active")) {
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
        } else if (task.equals("most_influencer")) {
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
        } else if (task.equals("mutual")) {
            if (args[3].equals("-ids") && 4 < args.length) {
                ids = args[4];
            }
            String[] keys_str = ids.split(",");
            for (int i = 0; i < keys_str.length; i++) {
                keys.add(getKeyById(graph, keys_str[i]));
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

        } else if (task.equals("suggest")) {
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
        for (String key : graph.keys) { // Iterate through all keys in the graph
            String[] parts = key.split("-"); // Split the key into name and id
            if (parts.length == 2 && parts[1].equals(id)) { // Check if the ID matches
                return key; // Return the matching name-id
            }
        }
        return null; // Return null if no match is found
    }

}
