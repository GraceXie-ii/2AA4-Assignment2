package ca.mcmaster.se2aa4.island.teamXXX.Strategies;

import org.json.JSONObject;

public class TopLeft implements SearchAlgorithm {

  private boolean left_most = false, top_most = false, radared = false;
 
  public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir, boolean allFound) {

    String decision = ""; //initilization of decision returning string

    if (batteryLevel < 100) { // If battery is low, stop mission
      decision = "stop"; // stop
    } 
    else if (!radared) { // If not radared, do radar
      decision = "echo front"; // radar front
    }
    else if (!left_most) { //if not at left most
      if (!(droneDir.equals("W"))) { //if drone is not heading east, turn till get to east
        decision = "heading right";
      }
      else {
        if (radarResults.getInt("range") > 1 || radarResults.getString("found").equals("GROUND")) { //check if the drone have not yet reached the border
          decision = "fly";
        }
        else { //else turn right
          left_most = true;
          decision = "heading right";
        }
      }
    }
    else if (!top_most) {
      if (radarResults.getInt("range") > 1 || radarResults.getString("found").equals("GROUND")) { //check if drone has not reached border
        decision = "fly";
      }
      else {
        decision = "heading right"; // turn right if reaching border
        top_most = true;
      }
    }
    else {
      decision = "stop"; //stop if all conidtion above not met, meaning drone is at top left location
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
