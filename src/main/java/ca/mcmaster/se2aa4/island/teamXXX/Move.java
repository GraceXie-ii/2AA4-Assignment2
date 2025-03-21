package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Move {

    public String fly() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "fly"); // make action JSON {"action": "fly"}
        return decision.toString(); // return decision JSON {"action": "fly"}
    }

    public String heading(String direction) { // left or right turn
        JSONObject decision = new JSONObject(); // create new JSON object - decision
        JSONObject parameters = new JSONObject(); // create new JSON object - parameters

        decision.put("action", "heading"); // make action JSON {"action": "heading"}
        parameters.put("direction", direction); // put parameter JSON {"parameters": {"direction": drone direction}}
        decision.put("parameters",  parameters); // combine JSON {"action": "heading", "parameters": {"direction": "S"}}

        return decision.toString(); // return decision JSON {"action": "heading", "parameters": {"direction": drone direction}}
    }

    public String stop() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "stop"); // make action JSON {"action": "stop"}
        return decision.toString(); // return decision JSON {"action": "stop"}
    }
 
    
}
