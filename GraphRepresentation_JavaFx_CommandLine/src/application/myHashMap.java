package application;

import java.util.ArrayList;

public class myHashMap {

    public ArrayList<String> keys;
    private ArrayList<ArrayList<String>> values;

    public myHashMap() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public boolean addKeyValue(String key, ArrayList<String> value) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key)) {
                values.set(i, value); // Update value if key exists
                return true;
            }
        }
        keys.add(key);
        values.add(value);
        return true;
    }

    public ArrayList<String> getValue(String key) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key)) {
                return values.get(i);
            }
        }
        return null;
    }
}
