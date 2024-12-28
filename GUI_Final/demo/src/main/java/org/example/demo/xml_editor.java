package org.example.demo;

import java.io.IOException;
import java.util.ArrayList;

public class xml_editor {
    static myHashMap graph;
    public static void startNetworkAnalysis(String task, String inputFilePath, StringBuilder output,String Id) {

        ArrayList<String> users;
        ArrayList<String> keys = new ArrayList<>();
        String keyy;

        // Validate the task
        if (!task.equals("most_active") && !task.equals("most_influencer") && !task.equals("mutual") && !task.equals("suggest")) {
            output.append("Unknown command. Only 'most_active', 'most_influencer', 'mutual', 'suggest' are supported.\n");
            return;
        }

        try {
            // Load graph
            graph = NetworkAnalysis.file_to_graph(inputFilePath);
        } catch (IOException e) {
            output.append("An error occurred during file processing: ").append(e.getMessage()).append("\n");
            return;
        }

        switch (task) {
            case "most_active":
                users = XMLMostActive.MostActive(graph);
                output.append("Most active user(s):\n");
                for (String user : users) {
                    user = getKeyById(graph, user);
                    if (user != null && user.contains("-")) {
                        String[] parts = user.split("-");
                        if (parts.length == 2) {
                            output.append("Name: ").append(parts[0]).append(", ID: ").append(parts[1]).append("\n");
                        }
                    }
                }
                break;

            case "most_influencer":
                users = XMLMostInfluencer.MostInfluencer(graph);
                output.append("Most influencer user(s):\n");
                for (String user : users) {
                    if (user != null && user.contains("-")) {
                        String[] parts = user.split("-");
                        if (parts.length == 2) {
                            output.append("Name: ").append(parts[0]).append(", ID: ").append(parts[1]).append("\n");
                        }
                    }
                }
                break;

            case "mutual":
                if (Id != null) {
                    String[] keysStr = Id.split(",");
                    for (String key : keysStr) {
                        keys.add(getKeyById(graph, key));
                    }
                    users = XMLMutualFollowers.MutualFollowers(graph, keys);
                    if (users.isEmpty()) {
                        output.append("No Mutual followers between given ids\n");
                    } else {
                        output.append("Mutual follower(s):\n");
                        for (String user : users) {
                            user = getKeyById(graph, user);
                            if (user != null && user.contains("-")) {
                                String[] parts = user.split("-");
                                if (parts.length == 2) {
                                    output.append("Name: ").append(parts[0]).append(", ID: ").append(parts[1]).append("\n");
                                }
                            }
                        }
                    }
                }
                break;

            case "suggest":
                if (Id != null) {
                    keyy = getKeyById(graph, Id);
                    users = XMLSuggestFollowers.MutualFollowers(graph, keyy);
                    if (users.isEmpty()) {
                        output.append("No Suggested followers\n");
                    } else {
                        output.append("Suggested follower(s):\n");
                        for (String user : users) {
                            user = getKeyById(graph, user);
                            if (user != null && user.contains("-")) {
                                String[] parts = user.split("-");
                                if (parts.length == 2) {
                                    output.append("Name: ").append(parts[0]).append(", ID: ").append(parts[1]).append("\n");
                                }
                            }
                        }
                    }
                }
                break;
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
