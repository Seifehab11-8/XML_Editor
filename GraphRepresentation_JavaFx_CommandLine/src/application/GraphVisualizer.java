package application;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraphVisualizer extends Application {

    private static myHashMap graph;
    private static String output;

    public static void setGraph(myHashMap graph) {
        GraphVisualizer.graph = graph;
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        Random random = new Random();
        Map<String, Double[]> positions = new HashMap<>();
        double radius = 30.0; // Increase the size of the nodes

        if (graph == null || graph.keys.isEmpty()) {
            System.out.println("No graph data available to visualize.");
            return;
        }

//        System.out.println("Drawing nodes...");

        // Draw nodes with collision detection
        graph.keys.forEach(key -> {
            double x, y;
            boolean collision;
            do {
                x = random.nextDouble() * 400 + 100;  // Ensure nodes are within the pane bounds
                y = random.nextDouble() * 400 + 100;
                collision = false;
                for (Double[] pos : positions.values()) {
                    double dx = pos[0] - x;
                    double dy = pos[1] - y;
                    if (Math.sqrt(dx * dx + dy * dy) < 2 * radius) {
                        collision = true;
                        break;
                    }
                }
            } while (collision);

            Circle circle = new Circle(x, y, radius, Color.SKYBLUE); // Set sky blue color
            Text text = new Text(x - 10, y + 5, key); // Adjust text position to center in circle
            text.setFill(Color.BLACK); // Set text to dark black
            text.setFont(Font.font(14)); // Set a suitable font size
            pane.getChildren().addAll(circle, text);
            positions.put(key, new Double[]{x, y});
//            System.out.println("Added node: " + key + " at (" + x + ", " + y + ")");
        });

//        System.out.println("Drawing edges...");

        // Draw directed edges with arrowheads
        graph.keys.forEach(key -> {
            Double[] startPos = positions.get(key);
            ArrayList<String> followers = graph.getValue(key);
            if (followers != null) {
                followers.forEach(follower -> {
                    Double[] endPos = positions.get(follower);
                    if (endPos != null) {
                        double startX = startPos[0];
                        double startY = startPos[1];
                        double endX = endPos[0];
                        double endY = endPos[1];

                        // Calculate the new start and end points for the edge
                        double deltaX = endX - startX;
                        double deltaY = endY - startY;
                        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                        double newStartX = startX + (deltaX * radius / distance);
                        double newStartY = startY + (deltaY * radius / distance);
                        double newEndX = endX - (deltaX * radius / distance);
                        double newEndY = endY - (deltaY * radius / distance);

                        Line line = new Line(newStartX, newStartY, newEndX, newEndY);
                        line.setStrokeWidth(2.0); // Make the edges slightly thicker
                        pane.getChildren().add(line);

                        // Create arrowhead
                        Polygon arrowHead = createArrowHead(newEndX, newEndY, newStartX, newStartY, radius);
                        pane.getChildren().add(arrowHead);

//                        System.out.println("Added edge from " + key + " to " + follower);
                    }
                });
            }
        });

        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setTitle("Graph Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();

//        System.out.println("Graph visualization window shown.");

        // Save the scene as a JPG
        saveAsJPG(scene);
    }

    private Polygon createArrowHead(double endX, double endY, double startX, double startY, double radius) {
        double arrowLength = 15;
        double arrowWidth = 7.5;

        double angle = Math.atan2(endY - startY, endX - startX);

        double x1 = endX - arrowLength * Math.cos(angle - Math.PI / 6);
        double y1 = endY - arrowLength * Math.sin(angle - Math.PI / 6);
        double x2 = endX - arrowLength * Math.cos(angle + Math.PI / 6);
        double y2 = endY - arrowLength * Math.sin(angle + Math.PI / 6);

        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(endX, endY, x1, y1, x2, y2);
        arrowHead.setFill(Color.BLACK);

        return arrowHead;
    }

    private void saveAsJPG(Scene scene) {
        WritableImage fxImage = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
        scene.snapshot(fxImage);

        BufferedImage bImage = SwingFXUtils.fromFXImage(fxImage, null);

        // Create a new BufferedImage to ensure compatibility with ImageIO.write
        BufferedImage formattedImage = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = formattedImage.createGraphics();
        graphics.drawImage(bImage, 0, 0, null);
        graphics.dispose();

        File file = new File(output);

        System.out.println("Attempting to save the graph to " + file.getAbsolutePath());

        try {
            boolean result = ImageIO.write(formattedImage, "jpg", file);
            if (result) {
                System.out.println("Graph saved as " + file.getAbsolutePath());
            } else {
                System.out.println("ImageIO.write returned false. The image might not have been saved.");
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

	public static String getOutput() {
		return output;
	}

	public static void setOutput(String output) {
		GraphVisualizer.output = output;
	}
}
