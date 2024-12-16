package application;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Generating graph from file...");
            myHashMap graph = GraphGenerator.file_to_graph("text.xml.txt");
            System.out.println("Graph generation complete.");
            
            // Print the graph data for debugging
            System.out.println("Graph Data:");
            graph.keys.forEach(key -> {
                System.out.println("User: " + key + ", Followers: " + graph.getValue(key));
            });
            
            GraphVisualizer.setGraph(graph);
            GraphVisualizer.launch(GraphVisualizer.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//--module-path /Users/seifehab/Downloads/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml