package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Drone implements FlyMachine{

    // private variables to keep track of the state of drone
    private int batteryLevel;
    private String DroneDir;

    public Drone(int batteryLevel, String direction){
        this.batteryLevel = batteryLevel;
        this.DroneDir = direction;
    }
  
    public JSONObject fly() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "fly"); // make action JSON {"action": "fly"}
        return decision; // return decision JSON {"action": "fly"}
    }

    public JSONObject heading(String direction) {
        JSONObject decision = new JSONObject(); // create new JSON object - decision
        JSONObject parameters = new JSONObject(); // create new JSON object - parameters

        decision.put("action", "heading"); // make action JSON {"action": "heading"}
        parameters.put("direction", direction); // put parameter JSON {"parameters": {"direction": "S"}}
        decision.put("parameters",  parameters); // combine JSON {"action": "heading", "parameters": {"direction": "S"}}

        return decision; // return decision JSON {"action": "heading", "parameters": {"direction": direction}}
    }

    public JSONObject echo(String direction) {
        JSONObject decision = new JSONObject(); // create new JSON object - decision
        JSONObject parameters = new JSONObject(); // create new JSON object - parameters

        decision.put("action", "echo"); // make action JSON {"action": "echo"}
        parameters.put("direction", direction); // put parameter JSON {"parameters": {"direction": direction}}
        decision.put("parameters",  parameters); // combine JSON {"action": "echo", "parameters": {"direction": direction}}

        return decision; // return decision JSON {"action": "echo", "parameters": {"direction": direction}}
    }

    public JSONObject scan() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "scan"); // make action JSON {"action": "scan"}
        return decision; // return decision JSON {"action": "scan"}
    }

    public JSONObject stop() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "stop"); // make action JSON {"action": "stop"}
        return decision; // return decision JSON {"action": "stop"}
    }
 

}
