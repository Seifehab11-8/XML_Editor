/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.postsearchsimplified;

/**
 *
 * @author AboAmer
 */
import java.io.*;
import java.util.*;

public class PostSearchSimplified {

    // Class to represent a Post
    static class Post {
        String body;
        List<String> topics;

        Post() {
            this.body = "";
            this.topics = new ArrayList<>();
        }
    }

    // Class to represent a User
    static class User {
        String id;
        String name;
        List<Post> posts;

        User() {
            this.id = "";
            this.name = "";
            this.posts = new ArrayList<>();
        }
    }

    public static List<User> parseXML(String filePath) {
        List<User> users = new ArrayList<>();
        User currentUser = null;
        Post currentPost = null;
        boolean insideTopics = false;
        boolean insideFollowers = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean insideComment = false; // Flag to track multi-line comments

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Handle comments
                if (insideComment) {
                    if (line.contains("-->")) {
                        insideComment = false; // Comment ends here
                        line = line.substring(line.indexOf("-->") + 3).trim(); // Remove the comment part
                    } else {
                        continue; // Skip the entire line if still inside a comment
                    }
                }
                if (line.startsWith("<!--")) {
                    insideComment = true; // Comment starts here
                    if (line.contains("-->")) {
                        insideComment = false; // Comment ends on the same line
                        line = line.substring(line.indexOf("-->") + 3).trim(); // Remove the comment part
                    } else {
                        continue; // Skip the entire line if comment continues
                    }
                }

                // Detect opening tags
                if (line.startsWith("<user>")) {
                    currentUser = new User();
                } else if (line.startsWith("<id>") && currentUser != null && !insideFollowers) {
                    // Only set the user's ID if not inside followers
                    currentUser.id = extractContent(line, "id" , reader);
                } else if (line.startsWith("<name>")) {
                    currentUser.name = extractContent(line, "name" , reader);
                } else if (line.startsWith("<post>")) {
                    currentPost = new Post();
                } else if (line.startsWith("<body>")) {
                    currentPost.body = extractContent(line, "body" , reader);
                } else if (line.startsWith("<topics>")) {
                    insideTopics = true;
                } else if (line.startsWith("<topic>") && insideTopics) {
                    currentPost.topics.add(extractContent(line, "topic" , reader));
                } else if (line.startsWith("<followers>")) {
                    insideFollowers = true;
                } else if (line.startsWith("<followers>") && insideFollowers){
                   // Ignore follower IDs for now since they don't affect the search
                }

                // Detect closing tags
                else if (line.startsWith("</post>") && currentPost != null) {
                    if (currentUser != null && currentPost != null){
                    currentUser.posts.add(currentPost);
                    }
                    currentPost = null;
                } else if (line.startsWith("</user>")) {
                    if (currentUser != null){
                        users.add(currentUser);
                    }
                    currentUser = null;
                } else if (line.startsWith("</topics>")) {
                    insideTopics = false;
                } else if (line.startsWith("</followers>")){
                    insideFollowers = false;
                }
                
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return users;
    }


    private static String extractContent(String line, String tag, BufferedReader reader) throws IOException {
    StringBuilder content = new StringBuilder();
    // Find where content starts after the opening tag
    int start = line.indexOf("<" + tag + ">") + tag.length() + 2;
    int end = line.indexOf("</" + tag + ">");
    
    // If content is on the same line
    if (end != -1) {
        content.append(line, start, end);
    } else {
        // Content spans multiple lines
        content.append(line.substring(start));
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            nextLine = nextLine.trim();
            end = nextLine.indexOf("</" + tag + ">");
            if (end != -1) {
                content.append(" ").append(nextLine, 0, end);
                break;
            } else {
                content.append(" ").append(nextLine);
            }
        }
    }
    return content.toString().trim();
}

    public static void searchByWord(List<User> users, String word) {
        boolean found = false;
        for (User user : users) {
            for (Post post : user.posts) {
                if (post.body.toLowerCase().contains(word.toLowerCase())) {
                    displayPost(user, post);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No posts found with the word: " + word);
        }
    }

    public static void searchByTopic(List<User> users, String topic) {
        boolean found = false;
        for (User user : users) {
            for (Post post : user.posts) {
                if (post.topics.stream().anyMatch(t -> t.equalsIgnoreCase(topic))) {
                    displayPost(user, post);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No posts found with the topic: " + topic);
        }
    }

    private static void displayPost(User user, Post post) {
        System.out.println("User: " + user.name + " (ID: " + user.id + ")");
        System.out.println("Post: " + post.body);
        System.out.println("Topics: " + String.join(", ", post.topics));
        System.out.println("-----------------------------");
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            // Command-line mode
            if (args[0].equals("search") && args.length == 5) {
                String searchType = args[1];
                String query = args[2];
                String filePath = args[4];

                List<User> users = parseXML(filePath);

                if (searchType.equals("-w")) {
                    searchByWord(users, query);
                } else if (searchType.equals("-t")) {
                    searchByTopic(users, query);
                } else {
                    System.out.println("Invalid search type. Use -w for word or -t for topic.");
                }
            } else {
                System.out.println("Invalid command. Usage:");
                System.out.println("java PostSearchSimplified search -w <word> -i <input_file.xml>");
                System.out.println("java PostSearchSimplified search -t <topic> -i <input_file.xml>");
            }
        } else {
            // Interactive mode
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter the path to the XML file:");
            String filePath = scanner.nextLine();

            System.out.println("Do you want to search by 'word' or 'topic'? (type 'word' or 'topic')");
            String searchType = scanner.nextLine().toLowerCase();

            System.out.println("Enter the word or topic to search:");
            String query = scanner.nextLine();

            List<User> users = parseXML(filePath);

            if (searchType.equals("word")) {
                searchByWord(users, query);
            } else if (searchType.equals("topic")) {
                searchByTopic(users, query);
            } else {
                System.out.println("Invalid search type. Please enter 'word' or 'topic'.");
            }

            scanner.close();
         }
    }
}