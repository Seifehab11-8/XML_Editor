package org.example.demo;


import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class NetworkAnalysis {

    public static myHashMap file_to_graph(String inputFilePath) throws FileNotFoundException, IOException {
        myHashMap graph = new myHashMap();
        FileInputStream input_file = new FileInputStream(inputFilePath);
        List<Byte> tags_byteList = new ArrayList<>(); // Dynamic array to store bytes
        List<Byte> data_byteList = new ArrayList<>();
        String byteListAsString = null;
        int data;
        String id = null;
        String name = null;
        ArrayList<String> followers = new ArrayList<>();
        boolean start_tag_detected = false;
        boolean tag_detected = false;
        boolean user_detected = false;
        boolean id_detected = false;
        boolean name_detected = false;
        boolean follower_detected = false;
        while ((data = input_file.read()) != -1) {
            if (data == '<' || start_tag_detected) {
                tags_byteList.add((byte) data);
                start_tag_detected = true;
            }
            if (data == '>') {
                start_tag_detected = false;
                byteListAsString = byteListToString(tags_byteList);
                tag_detected = true;
                tags_byteList.clear();
            }
            if (tag_detected) {
                if (byteListAsString.equals("<user>")) {
                    user_detected = true;
                } else if (byteListAsString.equals("<id>")) {
                    id_detected = true;
                } else if (byteListAsString.equals("<name>")) {
                    name_detected = true;
                } else if (byteListAsString.equals("<follower>")) {
                    follower_detected = true;
                } else if (byteListAsString.equals("</user>")) {
                    user_detected = false;
                    graph.addKeyValue(name + "-" + id, new ArrayList<>(followers));
                    followers.clear();
                } else if (byteListAsString.equals("</id>")) {
                    id_detected = false;
                    if (follower_detected) {
                        followers.add(byteListToString(data_byteList));
                        data_byteList.clear();
                    } else {
                        id = byteListToString(data_byteList);
                        data_byteList.clear();
                    }
                } else if (byteListAsString.equals("</name>")) {
                    name_detected = false;
                    name = byteListToString(data_byteList);
                    data_byteList.clear();
                } else if (byteListAsString.equals("</follower>")) {
                    follower_detected = false;
                } else {
                    tag_detected = false;
                    continue;
                }
                tag_detected = false;
                continue;
            }
            if (user_detected && (id_detected || name_detected) && !start_tag_detected) {
                data_byteList.add((byte) data);
            }
        }
        return graph;
    }

    public static String byteListToString(List<Byte> byteList) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteList) {
            sb.append((char) b); // Convert each byte to char and append to the string builder
        }
        return sb.toString();
    }
}
