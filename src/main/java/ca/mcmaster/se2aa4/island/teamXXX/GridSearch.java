package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class GridSearch {

    private boolean scanned = false, radared = false;
    private boolean land = true, left = false, right = false, turned_left = false, empty_check = false; // turned_right = false; // grid search
    // private int empty_count = 0;
    // protected int uTurnStep = 5; // u turn

    public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults) {
        String decision = "";

        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        }

        if (left) {
            decision = "heading left";
            left = false;
            turned_left = !turned_left;
            scanned = false;
            radared = false;
        }
        else if (right) {
            decision = "heading right";
            right = false;
            turned_left = !turned_left;
            scanned = false;
            radared = false;
        }

        else if (scanned == false) { // If not scanned, scan
            decision = "scan"; // scan
            scanned = true;
        }
        else if (radared == false) { // If not radared, radar
            decision = "echo front";
            radared = true;
            
        }
        else if (land == true) { // search on island
            decision = "fly";
            scanned = false;
            radared = false;
        }
        else { // land is found not found(out of range)
            if (!turned_left) {
                decision = "heading left";
                left = true;
            }
            else {
                decision = "heading right";
                right = true;
            }
        }

        if (radarResults.getString("found").equals("GROUND")) {
            land = true;
            if (empty_check) {
                empty_check = false;
            }

        }
        else {
            land = false;
            if (!empty_check){
                empty_check = true;
            }
            else {
                decision = "stop";
            }
        }

        return decision;
    }
    
}
