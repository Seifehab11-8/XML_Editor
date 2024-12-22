import java.io.*;
import java.util.*;

public class PostSearchSimplified {

    // Class to represent a Post
    static class Post {
        String text;
        List<String> topics;

        Post() {
            this.text = "";
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
        boolean insideBody = false;
        StringBuilder bodyContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip comment lines
                if (line.startsWith("<!--")) {
                    while (line != null && !line.contains("-->")) {
                        line = reader.readLine(); // Skip comment block
                    }
                    if (line != null) {
                        line = line.substring(line.indexOf("-->") + 3).trim();
                    }
                    if (line.isEmpty()) {
                        continue; // Skip to the next line if no content remains
                    }
                }

                // Detect opening tags
                if (line.startsWith("<user>")) {
                    currentUser = new User();
                } else if (line.startsWith("<id>")) {
                    currentUser.id = extractContent(line, "id");
                } else if (line.startsWith("<name>")) {
                    currentUser.name = extractContent(line, "name");
                } else if (line.startsWith("<post>")) {
                    currentPost = new Post();
                } else if (line.startsWith("<body>")) {
                    insideBody = true;
                    bodyContent.setLength(0); // Clear previous content
                } else if (line.startsWith("<topics>")) {
                    insideTopics = true;
                } else if (line.startsWith("<topic>")) {
                    currentPost.topics.add(extractContent(line, "topic"));
                }

                // Handle multiline body content
                if (insideBody) {
                    if (line.contains("</body>")) {
                        bodyContent.append(line, 0, line.indexOf("</body>")).append(" ");
                        currentPost.text = bodyContent.toString().trim();
                        insideBody = false;
                    } else {
                        bodyContent.append(line).append(" ");
                    }
                    continue;
                }

                // Detect closing tags
                if (line.startsWith("</post>")) {
                    currentUser.posts.add(currentPost);
                    currentPost = null;
                } else if (line.startsWith("</user>")) {
                    users.add(currentUser);
                    currentUser = null;
                } else if (line.startsWith("</topics>")) {
                    insideTopics = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return users;
    }

    private static String extractContent(String line, String tag) {
        return line.replace("<" + tag + ">", "")
                .replace("</" + tag + ">", "")
                .trim();
    }

    public static void searchByWord(List<User> users, String word) {
        boolean found = false;
        for (User user : users) {
            for (Post post : user.posts) {
                if (post.text.toLowerCase().contains(word.toLowerCase())) {
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
        System.out.println("Post: " + post.text);
        System.out.println("Topics: " + String.join(", ", post.topics));
        System.out.println("-----------------------------");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get XML file path from user
        System.out.println("Enter the path to the XML file:");
        String filePath = scanner.nextLine();

        // Get search type (word or topic)
        System.out.println("Do you want to search by 'word' or 'topic'?");
        String searchType = scanner.nextLine().toLowerCase();

        // Get search query
        System.out.println("Enter the word or topic to search:");
        String query = scanner.nextLine();

        // Parse the XML file
        List<User> users = parseXML(filePath);

        // Perform the search
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
