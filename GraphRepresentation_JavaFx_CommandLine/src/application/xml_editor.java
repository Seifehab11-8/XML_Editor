package application;

import java.io.IOException;

public class xml_editor {
    public static void main(String[] args) {
    	String inputFilePath = null;
    	String outputFilePath = null;
    	if(args[0].equals("draw")) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-i") && i + 1 < args.length) {
                    inputFilePath = args[i + 1];
                } else if (args[i].equals("-o") && i + 1 < args.length) {
                    outputFilePath = args[i + 1];
                }
            }
            System.out.println(inputFilePath);
            System.out.println(outputFilePath);
            try {
            	System.out.println("Generating graph from file...");
            	myHashMap graph = GraphGenerator.file_to_graph(inputFilePath);
            	System.out.println("Graph generation complete.");
            	
            	// Print the graph data for debugging
            	System.out.println("Graph Data:");
            	graph.keys.forEach(key -> {
            		System.out.println("User: " + key + ", Followers: " + graph.getValue(key));
            	});
            	GraphVisualizer.setOutput(outputFilePath);
            	GraphVisualizer.setGraph(graph);
            	GraphVisualizer.launch(GraphVisualizer.class);
            } catch (IOException e) {
            	e.printStackTrace();
            }
    	}
    }
}

//--module-path /Users/seifehab/Downloads/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml