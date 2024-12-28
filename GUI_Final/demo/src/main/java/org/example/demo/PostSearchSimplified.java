package org.example.demo;
/**
 *
 * @author AboAmer
 */
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
        String name;
        List<Post> posts;

        User() {
            this.name = "";
            this.posts = new ArrayList<>();
        }
    }

    public static List<User> parseXML(String filePath) {
        List<User> users = new ArrayList<>();
        User currentUser = null;
        Post currentPost = null;
        boolean insideTopics = false;

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
                } else if (line.startsWith("<name>")) {
                    assert currentUser != null;
                    currentUser.name = extractContent(line, "name");
                } else if (line.startsWith("<post>")) {
                    currentPost = new Post();
                } else if (line.startsWith("<body>")) {
                    assert currentPost != null;
                    currentPost.body = extractContent(line, "body");
                } else if (line.startsWith("<topics>")) {
                    insideTopics = true;
                } else if (line.startsWith("<topic>") && insideTopics) {
                    assert currentPost != null;
                    currentPost.topics.add(extractContent(line, "topic"));
                }

                // Detect closing tags
                else if (line.startsWith("</post>")) {
                    assert currentUser != null;
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
            showErrorWindow(STR."Error reading file: \{e.getMessage()}");
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
                if (post.body.toLowerCase().contains(word.toLowerCase())) {
                    displayPost(user, post);
                    found = true;
                }
            }
        }
        if (!found) {
            showErrorWindow("No posts found with the word: " + word);
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
            showErrorWindow("No posts found with the topic: " + topic);
        }
    }
    public static void displayPost(User user, Post post) {
        // Create a new Stage for displaying the post
        Stage secondaryStage = new Stage();

        // Create labels for user and post details
        Label userLabel = new Label("User: " + user.name  );
        Label postLabel = new Label("Post: " + post.body);
        Label topicsLabel = new Label("Topics: " + String.join(", ", post.topics));

        // Create a VBox layout and add labels to it
        userLabel.setStyle("-fx-alignment: center");
        postLabel.setStyle("-fx-alignment: center");
        topicsLabel.setStyle("-fx-alignment: center");
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center");
        vbox.getChildren().addAll(userLabel, postLabel, topicsLabel);

        // Create a scene and set it on the stage
        Scene scene = new Scene(vbox, 400, 200);
        secondaryStage.setTitle("Post Viewer");
        Image icon = new Image(Objects.requireNonNull(PostSearchSimplified.class.getResourceAsStream("/icons/find.png")));
        secondaryStage.getIcons().add(icon);
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }
    private String getUserInput(String txt) {
        // Create a modal window
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.APPLICATION_MODAL);
        inputStage.setTitle("PostSearch");
        Image icon = new Image(Objects.requireNonNull(PostSearchSimplified.class.getResourceAsStream("/icons/find.png")));
        inputStage.getIcons().add(icon);
        // Input field
        TextField inputField = new TextField();
        inputField.setPromptText(txt);

        // Set action on Enter key press
        inputField.setOnAction(event -> {
            inputStage.close(); // Close window when Enter is pressed
        });

        // Layout
        Label TextLabel = new Label(txt);
        VBox layout = new VBox(10, TextLabel,inputField);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");

        // Set Scene and show
        Scene scene = new Scene(layout, 300, 100);
        inputStage.setScene(scene);
        inputStage.showAndWait();

        return inputField.getText();
    }

    private static void showErrorWindow(String errorMessage) {
        // Show an error alert
        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void PostSearch(String filePath ) {
        List<User> users = parseXML(filePath);
        // Example usage of getUserInput
        String searchType = getUserInput("Do you want to search by 'word' or 'topic'?");
        String query = getUserInput("Enter the word or topic to search:");
        if (searchType == null || searchType.isEmpty() ) {
            showErrorWindow("Input cannot be empty!");
        }
        else if (searchType.equalsIgnoreCase("word")) {
            searchByWord(users, query);
        } else if (searchType.equals("topic")) {
            searchByTopic(users, query);
        }else{
            showErrorWindow("Invalid search type. Please enter 'word' or 'topic'.");
        }
    }
}
