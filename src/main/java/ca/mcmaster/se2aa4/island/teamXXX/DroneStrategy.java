package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class DroneStrategy {

    // Private variables to keep track of the state of the exploration
    private boolean scanned = false, radared = false, Newfoundland = false;
    private boolean land = true, left = false, right = false, turned_left = false, turned_right = false;
    private String strategy;

    // Constructor to allow strategy choice
    public DroneStrategy(String strategy) {
        if (strategy.equals("findLand")) {
            this.strategy = "findLand";
        }
    }

    // Method to find land
    public String getStrategy(int batteryLevel, JSONObject radarResults) {
        // if MVP search land
        if (strategy.equals("findLand")){
            return findLand(batteryLevel, radarResults);
        }
        // else temporarily return empty string
        return "";
    }

    private String findLand(int batteryLevel, JSONObject radarResults) {
        // JSONObject is used to delay getting data from radar, to when data is actually collected and available
        // Initialize the decision JSON object as a string
        String decision = "";

        // Implement the decision logic
        // If battery is low, stop mission; ocean search phase - scan/radar S/move E; ground phase - move S/stop
        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        }
        if (left) {
            decision = "left";
            left = false;
            turned_left = !turned_left;
        }
        else if (right) {
            decision = "right";
            right = false;
            turned_left = !turned_left;
        }
        else if (scanned == false) { // If not scanned, scan
            decision = "scan"; // scan
        }
        else if (radared == false) { // If not radared, radar
            if (Newfoundland == false) {
                decision = "echo right"; // echo south, ocean search phase"
            }
            else {
                decision = "echo front"; // echo south, approaching coast
            }
        }
        else if (Newfoundland == false) { // ocean search phase
            if (radarResults.getString("found").equals("GROUND")) {
                decision = "heading right"; // turn [S]outh (>v)
                Newfoundland = true;
            }
            else { // if foundValue.equals("OUT_OF_RANGE")
                decision = "fly"; // fly [E]ast
            }
        }
        else { // land is found
            if (radarResults.getInt("range") >= 2) {
                decision = "fly"; // fly [S]outh to coast
            }
            else { // if rangeValue < 2
                decision = "stop"; // stop
            }
        }

        // rotate strategy state: scan, radar, fly/heading/stop
        if (!scanned && !radared) {
            scanned = true;
        }
        else if (scanned && !radared) {
            radared = true;
        }
        else if (scanned && radared) {
            scanned = false;
            radared = false;
        }

        // return command string decision
        return decision;
    }

    public String gridSearch(int batteryLevel, JSONObject radarResults) {
        String decision = "";

        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        }
        else if (scanned == false) { // If not scanned, scan
            decision = "scan"; // scan
        }
        else if (radared == false) { // If not radared, radar
            decision = "echo front";
            if (radarResults.getString("found").equals("GROUND")) {
                land = true;
            }
            else {
                land = false;
            }
        }
        else if (land == true) { // ocean search phase
            decision = "fly";
        }
        else { // land is found not found(out of range)
            if (!turned_left) {
                decision = "left";
                left = true;
            }
            else {
                decision = "right";
                right = true;
            }
        }

        // rotate strategy state: scan, radar, fly/heading/stop
        if (!scanned && !radared) {
            scanned = true;
        }
        else if (scanned && !radared) {
            radared = true;
        }
        else if (scanned && radared) {
            scanned = false;
            radared = false;
        }



        return decision;
    }

    public String coastSearch(int batteryLevel, JSONObject radarResults){
        String decision = "";

        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        }



        return decision;
    }
    
}
