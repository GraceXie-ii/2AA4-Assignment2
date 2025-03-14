package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Drone implements FlyMachine{

    // private classes to keep track of extra information
    private Radar radar;

    // private variables to keep track of the state of drone
    private int batteryLevel;
    private String DroneDir;
    private final String directions = "NESW";

    public Drone(int batteryLevel, String direction){

        // Initialize drone's radar
        this.radar = new Radar();

        // Initialize drone's battery level and direction
        this.batteryLevel = batteryLevel;
        this.DroneDir = direction;
    }

    // command chain to radar methods: echo, processRadarResponse, getRadarInfo
    public String echo(String direction) {
        return radar.sendRadarSingal(direction, this.DroneDir); // return radar JSON {"action": "echo", "parameters": {"direction": direction}}
    }
    public void processRadarResponse(String found, int range) {
        radar.processRadarResponse(found, range); // process radar response
    }
    public JSONObject getRadarInfo() {
        return radar.getRadarInfo(); // return radar info
    }

    public String scan() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "scan"); // make action JSON {"action": "scan"}
        return decision.toString(); // return decision JSON {"action": "scan"}
    }
  
    public String fly() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "fly"); // make action JSON {"action": "fly"}
        return decision.toString(); // return decision JSON {"action": "fly"}
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

    public String stop() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "stop"); // make action JSON {"action": "stop"}
        return decision.toString(); // return decision JSON {"action": "stop"}
    }
 

}
