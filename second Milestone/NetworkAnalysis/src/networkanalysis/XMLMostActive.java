package networkanalysis;

import java.util.ArrayList;

/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Moham
 */
public class XMLMostActive {

    public static ArrayList<String> MostActive(myHashMap graph) {
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> mostactiveUsers = new ArrayList<>();
        int maxFollowCount = 0;
        int FollowCount = 0;
        for (String key : graph.keys) {
            followers.addAll(graph.getValue(key));
            ids.add(key.split("-")[1]);
        }
        for (String id : ids) {
            FollowCount = 0;
            for (String follower : followers) {
                if (follower.equals(id)) {
                    FollowCount++;
                }
            }
            if (FollowCount > maxFollowCount) {
                maxFollowCount = FollowCount; // Update the max count
                mostactiveUsers.clear(); // Clear the previous list
                mostactiveUsers.add(id); // Add the current user's key
            } else if (FollowCount == maxFollowCount) {
                mostactiveUsers.add(id); // Add the current user's ID
            }
        }
        return mostactiveUsers;
    }
}
