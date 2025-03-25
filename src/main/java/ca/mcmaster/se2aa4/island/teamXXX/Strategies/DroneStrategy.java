package ca.mcmaster.se2aa4.island.teamXXX.Strategies;

import org.json.JSONObject;

public class DroneStrategy {

    // private sub-classes to delegate specfic strategy
    private FindLand findLand = new FindLand();
    private InterlacedSearch interlaced_search = new InterlacedSearch();
    
    // Private variables to keep track of the state of the exploration
    private String strategy; // strategy choice

    // Constructor to allow strategy choice
    public DroneStrategy(String strategy) {
        if (strategy.equals("findLand") || (strategy.equals("bruteForce"))) {
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
        } else {
            System.out.println("not valid");
            return "";
        }
    }

}
