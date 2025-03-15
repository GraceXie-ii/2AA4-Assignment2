package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
//import org.json.JSONTokener;
//import java.io.*;

public class Radar {
    private String frontRadar;
    private String leftRadar;
    private String rightRadar;
    
    private String foundValue = "";
    private int rangeValue = 0;

    public Radar(){
        this.frontRadar = "";
        this.leftRadar = "";
        this.rightRadar = "";
    }

    public String sendRadarSingal(String direction){
        JSONObject decision = new JSONObject(); // create new JSON object - decision
        JSONObject parameters = new JSONObject(); // create new JSON object - parameters

        decision.put("action", "echo"); // make action JSON {"action": "echo"}
        parameters.put("direction", direction); // put parameter JSON {"parameters": {"direction": direction}}
        decision.put("parameters",  parameters); // combine JSON {"action": "echo", "parameters": {"direction": direction}}

        return decision.toString(); // return decision JSON {"action": "echo", "parameters": {"direction": direction}}
    }

    public void processRadarResponse(String direction, String response){
        if(direction.equals("FRONT")){
            this.frontRadar = response;
        }else if(direction.equals("LEFT")){
            this.leftRadar = response;
        }else if(direction.equals("RIGHT")){
            this.rightRadar = response;
        }
        else{
            System.out.println("Invalid direction");
        }
    }

    public JSONObject getRadarInfo(){
        JSONObject radarInfo = new JSONObject();
        radarInfo.put("front", this.frontRadar);
        radarInfo.put("left", this.leftRadar);
        radarInfo.put("right", this.rightRadar);
        return radarInfo;
    }
}
