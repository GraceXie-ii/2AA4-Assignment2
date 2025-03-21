package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Move {

    private int x, y; // drone coordinates

    public Move() {
        this.x = 1; // set x coordinate to 1
        this.y = 1; // set y coordinate to 1
    }

    // return drone coordinates
    public int[] getCoordinates() {
        return new int[] {this.x, this.y}; // return drone coordinates
    }

    public String fly(int[] direction_vector) {

        // update drone position
        this.x += direction_vector[0];
        this.y += direction_vector[1];

        // make action JSON {"action": "fly"}
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "fly"); // make action JSON {"action": "fly"}
        return decision.toString(); // return decision JSON as a string
    }

    public String heading(String direction, int[] direction_vector) { // left or right turn

        // update drone position
        this.x += direction_vector[0];
        this.y += direction_vector[1];

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
