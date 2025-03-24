package ca.mcmaster.se2aa4.island.teamXXX.Strategies;

import org.json.JSONObject;

public class DroneStrategy {

    // private sub-classes to delegate specfic strategy
    private FindLand findLand = new FindLand();
    private InterlacedSearch interlaced_search = new InterlacedSearch();
    private TopLeft topLeft = new TopLeft();
    

    // Private variables to keep track of the state of the exploration
    private String strategy; // strategy choice

    // Constructor to allow strategy choice
    public DroneStrategy(String strategy) {
        if (strategy.equals("findLand") || (strategy.equals("bruteForce")) || (strategy.equals("topLeft"))) {
            this.strategy = strategy;
        } else {
            System.out.println("Not a valid Strategy");
        }
    }

    // Method to find land
    public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir, boolean allFound) {
        if (strategy.equals("findLand")){ // if MVP search land
            return findLand.getStrategy(batteryLevel, radarResults, scanResults, droneDir, allFound);
        } else if (strategy.equals("bruteForce")) { // brute force search
            return interlaced_search.getStrategy(batteryLevel, radarResults, scanResults, droneDir, allFound);
        } else if (strategy.equals("topLeft")) {
            String decision = topLeft.getStrategy(batteryLevel, radarResults, scanResults, droneDir, allFound);
            if (decision.equals("done")) {
                return interlaced_search.getStrategy(batteryLevel, radarResults, scanResults, droneDir, allFound);
            }
            else {
                return decision;
            }
        }
        else {
            System.out.println("not valid");
            return "";
        }
    }

    /* obselete or not developed code, temporarily commented out
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

    public String toOrigin(String direction, String position) {
        String decision = "";
        
        return decision;
    }
    */

}
