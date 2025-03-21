package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
//import org.json.JSONTokener;
//import java.io.*;

public class Radar {

    // Declare JSON objects for radar info in FRONT, LEFT, RIGHT directions
    private JSONObject frontRadar, leftRadar, rightRadar;

    // Declare variables to keep track of the last direction and orientation of the radar echo
    private String lastDir, lastOrientation;

    // Declare final variables to store the four directions
    public enum Direction{
        N, E, S, W;
    
        public Direction turnLeft(){
            return Direction.values()[(this.ordinal() + 3) % 4];
        }

        public Direction turnRight(){
            return Direction.values()[(this.ordinal() + 1) % 4];
        }
    }


    public Radar(){
        // Initialize radar info JSON objects, last direction and orientation
        this.frontRadar = new JSONObject();
        this.leftRadar = new JSONObject();
        this.rightRadar = new JSONObject();
        this.lastDir = "E";
        this.lastOrientation = "FRONT";
    }

    public String sendRadarSignal(String direction, String droneDir){
        JSONObject decision = new JSONObject(); // create new JSON object - decision
        JSONObject parameters = new JSONObject(); // create new JSON object - parameters

        // Calculate direction of echo based on current drone direction, update last direction, LEFT/FRONT/RIGHT -> NESW
        if (direction.equals("LEFT")) {
            lastDir = droneDir.turnLeft();
        } else if (direction.equals("RIGHT")) {
            lastDir = droneDir.turnRight();
        } else if (direction.equals("FRONT")) {
            lastDir = droneDir;
        }

        // Store last orientation, FRONT/LEFT/RIGHT
        lastOrientation = direction;

        System.out.println("Last direction: " + lastDir + " Last orientation: " + lastOrientation + "given direction: " + direction);

        decision.put("action", "echo"); // make action JSON {"action": "echo"}
        parameters.put("direction", lastDir); // put parameter JSON {"parameters": {"direction": lastDir}}
        decision.put("parameters",  parameters); // combine JSON {"action": "echo", "parameters": {"direction": lastDir}}

        return decision.toString(); // return decision JSON {"action": "echo", "parameters": {"direction": lastDir}}
    }

    public void processRadarResponse(String found, int range){
        // Update radar info based on the direction of the last radar echo
        if(lastOrientation.equals("FRONT")){
            this.frontRadar.clear();
            this.frontRadar.put("found", found);
            this.frontRadar.put("range", range);
        }else if(lastOrientation.equals("LEFT")){
            this.leftRadar.clear();
            this.leftRadar.put("found", found);
            this.leftRadar.put("range", range);
        }else if(lastOrientation.equals("RIGHT")){
            this.rightRadar.clear();
            this.rightRadar.put("found", found);
            this.rightRadar.put("range", range);
        }
        else{
            System.out.println("Invalid direction");
        }
    }

    public JSONObject getRadarInfo(){
        // Determine the direction of the last radar echo, return the corresponding radar info
        if (lastOrientation.equals("FRONT")){
            return this.frontRadar;
        }else if (lastOrientation.equals("LEFT")){
            return this.leftRadar;
        }else if (lastOrientation.equals("RIGHT")){
            return this.rightRadar;
        }else{ // If incorrect direction, return null
            System.out.println("Invalid direction");
            return null;
        }
    }
}
