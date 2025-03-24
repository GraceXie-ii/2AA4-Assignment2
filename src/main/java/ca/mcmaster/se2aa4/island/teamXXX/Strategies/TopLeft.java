package ca.mcmaster.se2aa4.island.teamXXX.Strategies;

import org.json.JSONObject;

public class TopLeft implements SearchAlgorithm {

  private boolean left_most = false, top_most = false, radared = false;
 
  public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir, boolean allFound) {

    String decision = "";

    if (batteryLevel < 100) { // If battery is low, stop mission
      decision = "stop"; // stop
    } 
    else if (!radared) { // If not radared, do radar
      decision = "echo front"; // radar front
    }
    else if (!left_most) {
      if (!(droneDir.equals("W"))) {
        decision = "heading right";
      }
      else {
        if (radarResults.getInt("range") > 1 || radarResults.getString("found").equals("GROUND")) {
          decision = "fly";
        }
        else {
          left_most = true;
          decision = "heading right";
        }
      }
    }
    else if (!top_most) {
      if (radarResults.getInt("range") > 1 || radarResults.getString("found").equals("GROUND")) {
        decision = "fly";
      }
      else {
        decision = "heading right";
      }
    }
    else {
      decision = "stop";
    }

    if (radared) { //alternating the state of radared for each function call
      radared = false;
    }
    else {
      radared = true;
    }


    return decision;
  }

}
