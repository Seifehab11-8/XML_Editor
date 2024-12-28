package DSAproject;

import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Moham
 */
public class XMLSuggestFollowers {

    public static ArrayList<String> MutualFollowers(myHashMap graph, String key) {
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> follower_of_followers = new ArrayList<>();
        ArrayList<String> suggested_followers = new ArrayList<>();
        followers.addAll(graph.getValue(key));
        boolean first_time = true;
        boolean found = false;
        for (String follower : followers) {
            follower_of_followers.clear();
            if (first_time) {
                suggested_followers.addAll(graph.getValue(xml_editor.getKeyById(graph, follower)));
                first_time = false;
                continue;
            }
            follower_of_followers.addAll(graph.getValue(xml_editor.getKeyById(graph, follower)));
            for (String follower_of_follower : follower_of_followers) {
                for (String suggested_follower : suggested_followers) {
                    if (follower_of_follower.equals(suggested_follower)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    suggested_followers.add(follower_of_follower);
                }
                found = false;
            }

        }
        for (int i = 0; i < suggested_followers.size(); i++) {
            if (suggested_followers.get(i).equals(key.split("-")[1])) {
                suggested_followers.remove(i);
            }
        }
        return suggested_followers;
    }
}
