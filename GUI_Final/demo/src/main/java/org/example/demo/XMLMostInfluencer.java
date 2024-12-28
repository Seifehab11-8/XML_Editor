package org.example.demo;


import java.util.ArrayList;

/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Moham
 */
public class XMLMostInfluencer {

    public static ArrayList<String> MostInfluencer(myHashMap graph) {
        int maxFollowCount = 0;
        ArrayList<String> mostinfluencerUsers = new ArrayList<>();
        for (String key : graph.keys) {
            ArrayList<String> followers = graph.getValue(key); // Get the followers of the user
            if (followers != null) {
                int followCount = followers.size(); // Count the number of followers
                if (followCount > maxFollowCount) {
                    maxFollowCount = followCount; // Update the max count
                    mostinfluencerUsers.clear(); // Clear the previous list
                    mostinfluencerUsers.add(key); // Add the current user's key
                } else if (followCount == maxFollowCount) {
                    mostinfluencerUsers.add(key); // Add the current user's ID
                }
            }
        }

        return mostinfluencerUsers; // Return the list of most active user IDs
    }
}
