package ca.mcmaster.se2aa4.island.teamXXX.Actions;

import org.json.JSONObject;
//import org.json.JSONTokener;
//import java.io.*;

public class Radar {

    // Declare JSON objects for radar info in FRONT, LEFT, RIGHT directions
    private JSONObject frontRadar, leftRadar, rightRadar;

    // Declare variables to keep track of the last direction and orientation of the radar echo
    private String lastOrientation;

    public Radar(){
        // Initialize radar info JSON objects, last direction and orientation
        this.frontRadar = new JSONObject();
        this.leftRadar = new JSONObject();
        this.rightRadar = new JSONObject();
        this.lastOrientation = "FRONT";
    }

    public String sendRadarSignal(String orientation, String echoDir){
        // create new JSON object - decision, parameters
        JSONObject decision = new JSONObject(); 
        JSONObject parameters = new JSONObject();      

        // Store last orientation, FRONT/LEFT/RIGHT
        lastOrientation = orientation;

        // create JSON object = {"action": "echo", "parameters": {"direction": echoDir}}
        decision.put("action", "echo"); 
        parameters.put("direction", echoDir); 
        decision.put("parameters",  parameters); 

        // return decision JSON
        return decision.toString();  
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
