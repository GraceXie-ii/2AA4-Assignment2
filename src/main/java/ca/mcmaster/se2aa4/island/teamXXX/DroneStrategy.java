package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class DroneStrategy {

    // Private variables to keep track of the state of the exploration
    private String strategy; // strategy choice
    private boolean scanned = false, radared = false, Newfoundland = false, doGridSearch = false; // find land
    private boolean land = true, left = false, right = false, turned_left = false;// turned_right = false; // grid search
    private int empty_count = 0;
    protected int uTurnStep = 5; // u turn

    // Constructor to allow strategy choice
    public DroneStrategy(String strategy) {
        if (strategy.equals("findLand")) {
            this.strategy = "findLand";
        } else if (strategy.equals("bruteForce")) {
            this.strategy = "bruteForce";
        }
    }

    // Method to find land
    public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir) {
        // if MVP search land
        if (strategy.equals("findLand") && !doGridSearch){
            return findLand(batteryLevel, radarResults, scanResults);
        } else if (strategy.equals("bruteForce")) {
            return bruteSearch(batteryLevel, radarResults, scanResults, droneDir);
        } else {
            return gridSearch(batteryLevel, radarResults, scanResults);
        }
        // else temporarily return empty string
        //return "";
    }

    private String findLand(int batteryLevel, JSONObject radarResults, JSONObject scanResults) {
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
                // only find land
                decision = "stop"; // stop
                
                // link to grid search
                //decision = "fly";
                //doGridSearch = true;
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

    private boolean searchDown = true, uRight = false, uLeft = false; 

    public String bruteSearch(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir) {
        // JSONObject is used to delay getting data from radar, to when data is actually collected and available
        // ----------------------------Initialize the decision JSON object as a string----------------------------
        String decision = "";

        // ----------------------------Implement the decision logic-----------------------------------------------
        // If battery is low, stop mission; scan, radar
        if (batteryLevel < 100) { // If battery is low, stop mission
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
            //decision = "stop";
             
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

    public String gridSearch(int batteryLevel, JSONObject radarResults, JSONObject scanResults) {
        String decision = "";

        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        }

        if (left) {
            decision = "heading left";
            left = false;
            turned_left = !turned_left;
        }
        else if (right) {
            decision = "heading right";
            right = false;
            turned_left = !turned_left;
        }
        
        else if (scanned == false) { // If not scanned, scan
            decision = "scan"; // scan
        }
        else if (radared == false) { // If not radared, radar
            decision = "echo front";
            if (radarResults.getString("found").equals("GROUND")) {
                land = true;
                empty_count--;
            }
            else {
                land = false;
                empty_count++;
            }
        }
        else if (land == true) { // ocean search phase
            decision = "fly";
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
        if (empty_count > 3) {
            decision = "stop";
        }

        return decision;
    }

    public String coastSearch(int batteryLevel, JSONObject radarResults, JSONObject scanResults){
        String decision = "";

        if (batteryLevel < 100) { // If battery is low, stop mission
            decision = "stop"; // stop
        }

        return decision;
    }
    
    public String uTurn(String direction){
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        String[] steps = new String[5];

        if (direction == "N"){
            steps = new String[]{"W", "N", "E", "E", "S"};
        }else if(direction == "S"){
            steps = new String[]{"E", "S", "W", "W", "N"};
        }

        for (int i = 0; i < uTurnStep; i++){
            decision.put("action", "heading");
            parameters.put("direction", steps[i]);
            decision.put("parameters", parameters);
        }
        return decision.toString();
    }

}
