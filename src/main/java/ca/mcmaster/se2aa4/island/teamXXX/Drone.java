package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;

public class Drone {

    // private classes to keep track of extra information
    private Radar radar;
    private PhotoScanner scanner;

    // private variables to keep track of the state of drone
    private int batteryLevel;
    private String DroneDir;
    private final String directions = "NESW";

    public Drone(int batteryLevel, String direction){

        // Initialize drone's radar, scanner
        this.radar = new Radar();
        this.scanner = new PhotoScanner();

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

    // command chain to scan methods: scan
    public String scan() {
        return scanner.scan(); // return scan JSON {"action": "scan"}
    }
    public void processScanResponse(JSONArray creeks, JSONArray sites) {
        scanner.processScanResponse(creeks, sites); // process scan response
    }
    public JSONObject getScanInfo() {
        return scanner.getScanInfo(); // return scan info
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
