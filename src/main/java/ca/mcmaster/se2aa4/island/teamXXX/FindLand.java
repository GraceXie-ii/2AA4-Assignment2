package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class FindLand {

    private boolean scanned = false, radared = false, Newfoundland = false; // find land

    public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults) {
        // JSONObject is used to delay getting data from radar, to when data is actually collected and available
        // Initialize the decision JSON object as a string
        String decision = "";

        // Implement the decision logic
        // If battery is low, stop mission; ocean search phase - scan/radar S/move E; ground phase - move S/stop
        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        } else if (scanned == false) { // If not scanned, scan
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
    
}
