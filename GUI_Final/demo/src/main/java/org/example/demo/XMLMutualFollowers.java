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
public class XMLMutualFollowers {

    public static ArrayList<String> MutualFollowers(myHashMap graph, ArrayList<String> keys) {
        ArrayList<String> mutualfollowers = new ArrayList<>();
        ArrayList<String> followers = new ArrayList<>();
        boolean first_time = true;
        boolean found = false;
        for (String key : keys) {
            followers.clear();
            if (first_time) {
                mutualfollowers.addAll(graph.getValue(key));
                first_time = false;
                continue;
            }
            followers.addAll(graph.getValue(key));
            for (int i = mutualfollowers.size() - 1; i >= 0; i--) {
                for (String follower : followers) {
                    if (follower.equals(mutualfollowers.get(i))) {
                        found = true;
                    }
                }
                if (!found) {
                    mutualfollowers.remove(i);
                }
                found = false;
            }
        }

        return mutualfollowers;
    }
}
