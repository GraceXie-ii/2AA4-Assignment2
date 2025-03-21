package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Heading {

  Direction direction;

  public Heading(String direction) {
    
  }

  public String heading(String direction) { // left or right turn
        JSONObject decision = new JSONObject(); // create new JSON object - decision
        JSONObject parameters = new JSONObject(); // create new JSON object - parameters

        // calculate new direction, update drone direction
        if (direction.equals("L")) {
            DroneDir = directions.charAt((directions.indexOf(DroneDir) + 3) % 4) + "";
        } else if (direction.equals("R")) {
            DroneDir = directions.charAt((directions.indexOf(DroneDir) + 1) % 4) + "";
        }

        decision.put("action", "heading"); // make action JSON {"action": "heading"}
        parameters.put("direction", DroneDir); // put parameter JSON {"parameters": {"direction": drone direction}}
        decision.put("parameters",  parameters); // combine JSON {"action": "heading", "parameters": {"direction": "S"}}

        return decision.toString(); // return decision JSON {"action": "heading", "parameters": {"direction": drone direction}}
    }
}
