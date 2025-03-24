package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class InterlacedSearch implements SearchAlgorithm {

    private boolean scanned = false, radared = false, searchDown = true, uRight = false, uLeft = false; 

    @Override
    public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir, boolean allFound) {
        // JSONObject is used to delay getting data from radar, to when data is actually collected and available
        // ----------------------------Initialize the decision JSON object as a string----------------------------
        String decision = "";

        // ----------------------------Implement the decision logic-----------------------------------------------
        // If battery is low, stop mission; scan, radar
        if (batteryLevel < 100 || allFound) { // If battery is low, stop mission
            decision = "stop"; // stop
        } else if (scanned == false) { // If not scanned, scan
            decision = "scan"; // scan
        } else if (radared == false) { // If not echoed, echo forward
            decision = "echo front";
        }

        // Decide movement: Fly/Turn Left/Turn Right/Stop
        /*
         * downward search
         *      moving East
         *          not at border / at ground -> fly
         *          at border -> turn Right, set uRight = true
         *      moving South 
         *          at south border 
         *              even distance 2 -> searchDown = false
         *              odd distance 3 -> fly 
         *          uRight -> turn Right, reset uRight = false
         *          uLeft -> turn Left, reset uLeft = false
         *      moving West
         *          not at border / at ground -> fly
         *          at border -> turn Left, set uLeft = true
         * upward search
         *      moving East
         *          not at border / at ground -> fly
         *          at border -> turn left, set uLeft = true
         *      moving North 
         *          at north border -> stop
         *          uRight -> turn Right, reset uRight = false
         *          uLeft -> turn Left, reset uLeft = false
         *      moving West
         *          not at border / at ground -> fly
         *          at border -> turn right, set uRight = true
         */
        else if (searchDown) { // Downward search
            if (droneDir.equals("E")) { // moving East
                if (radarResults.getInt("range") > 2 || radarResults.getString("found").equals("GROUND")) { // not at border
                    decision = "fly"; // fly
                } else { // at border
                    decision = "heading right"; // right
                    uRight = true;
                }
            } else if (droneDir.equals("S")) { // if south
                if (radarResults.getInt("range") <= 2) {
                    searchDown = false;
                }
                if (radarResults.getInt("range") == 3) {
                    decision = "fly";
                } else if (uRight) {
                    decision = "heading right"; 
                    uRight = false;
                } else if (uLeft) {
                    decision = "heading left";
                    uLeft = false;
                }
            } else if (droneDir.equals("W")) {
                if (radarResults.getInt("range") > 2 || radarResults.getString("found").equals("GROUND")) {
                    decision = "fly";
                } else {
                    decision = "heading left";
                    uLeft = true;
                }
            }
        } else { // search up             
            if (droneDir.equals("E")) { // moving East
                if (radarResults.getInt("range") > 2 || radarResults.getString("found").equals("GROUND")) { // not at border
                    decision = "fly"; // fly
                } else { // at border
                    decision = "heading left"; // left
                    uLeft = true;
                }
            } else if (droneDir.equals("N")) { // if north
                if (radarResults.getInt("range") <= 2) {
                    decision = "stop";
                }
                else if (uRight) {
                    decision = "heading right"; 
                    uRight = false;
                } else if (uLeft) {
                    decision = "heading left";
                    uLeft = false;
                }
            } else if (droneDir.equals("W")) {
                if (radarResults.getInt("range") > 2 || radarResults.getString("found").equals("GROUND")) {
                    decision = "fly";
                } else {
                    decision = "heading right";
                    uRight = true;
                }
            }
            
        }
        // ----------------------------rotate strategy state: scan, radar, fly/heading/stop-----------------------
        //      gurantee scan and radar results to be available at each move
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

        // ----------------------------return command string decision---------------------------------------------
        return decision;
    }
    
}
