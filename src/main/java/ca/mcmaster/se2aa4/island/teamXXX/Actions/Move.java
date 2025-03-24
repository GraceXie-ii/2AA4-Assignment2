package ca.mcmaster.se2aa4.island.teamXXX.Actions;

import org.json.JSONObject;

public class Move {

    public String fly() {
        // make action JSON {"action": "fly"}
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "fly"); // make action JSON {"action": "fly"}
        return decision.toString(); // return decision JSON as a string
    }

    public String heading(String direction) { // left or right turn
        // create new JSON object - decision, parameters
        JSONObject decision = new JSONObject(); 
        JSONObject parameters = new JSONObject(); 

        // create JSON {"action": "heading", "parameters": {"direction": direction}}
        decision.put("action", "heading"); // make action JSON {"action": "heading"}
        parameters.put("direction", direction); // put parameter JSON {"parameters": {"direction": drone direction}}
        decision.put("parameters",  parameters); // combine JSON {"action": "heading", "parameters": {"direction": drone direction}}

        return decision.toString(); // return decision JSON as a string
    }

    public String stop() {
        // make action JSON {"action": "stop"}
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "stop"); // make action JSON {"action": "stop"}
        return decision.toString(); // return decision JSON as a string
    }
 
    
}
