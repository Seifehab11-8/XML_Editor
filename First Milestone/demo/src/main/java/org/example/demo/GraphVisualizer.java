package org.example.demo;

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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraphVisualizer {

    private static myHashMap graph;

    public void Start_GraphVisualization(String inputFilePath) {
        try {
            System.out.println("Generating graph from file...");
            graph = GraphGenerator.file_to_graph(inputFilePath);
            System.out.println("Graph generation complete.");

            // Save the graph visualization as JPG
            saveGraphAsJPG();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGraphAsJPG() {
        Pane pane = new Pane();
        Random random = new Random();
        Map<String, Double[]> positions = new HashMap<>();
        double radius = 30.0; // Node size

        if (graph == null || graph.keys.isEmpty()) {
            System.out.println("No graph data available to visualize.");
            return;
        }

        // Draw nodes
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

            Circle circle = new Circle(x, y, radius, Color.SKYBLUE);
            Text text = new Text(x - 10, y + 5, key);
            text.setFill(Color.BLACK);
            text.setFont(Font.font(14));

            pane.getChildren().addAll(circle, text);
            positions.put(key, new Double[]{x, y});
        });

        // Draw edges
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

                        // Adjust edges to connect at the circle boundary
                        double deltaX = endX - startX;
                        double deltaY = endY - startY;
                        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                        double newStartX = startX + (deltaX * radius / distance);
                        double newStartY = startY + (deltaY * radius / distance);
                        double newEndX = endX - (deltaX * radius / distance);
                        double newEndY = endY - (deltaY * radius / distance);

                        Line line = new Line(newStartX, newStartY, newEndX, newEndY);
                        line.setStrokeWidth(2.0);
                        pane.getChildren().add(line);

                        // Arrowhead
                        Polygon arrowHead = createArrowHead(newEndX, newEndY, newStartX, newStartY);
                        pane.getChildren().add(arrowHead);
                    }
                });
            }
        });

        // Save the scene as a JPG
        saveAsJPG(pane);
    }

    private Polygon createArrowHead(double endX, double endY, double startX, double startY) {
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

    private void saveAsJPG(Pane pane) {
        Scene scene = new Scene(pane, 600, 600);
        WritableImage fxImage = new WritableImage(600, 600);
        scene.snapshot(fxImage);

        BufferedImage bImage = SwingFXUtils.fromFXImage(fxImage, null);
        BufferedImage formattedImage = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = formattedImage.createGraphics();
        graphics.drawImage(bImage, 0, 0, null);
        graphics.dispose();

        File file = new File("src/main/resources/graph.jpg");

        try {
            boolean result = ImageIO.write(formattedImage, "jpg", file);
            if (result) {
                System.out.println("Graph saved as " + file.getAbsolutePath());
            } else {
                System.out.println("Failed to save graph as JPG.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
