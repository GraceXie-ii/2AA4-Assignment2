package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;

public class Radar {
    private String frontRadar;
    private String leftRadar;
    private String rightRadar;

    public Radar(){
        this.frontRadar = "";
        this.leftRadar = "";
        this.rightRadar = "";
    }

    public String sendRadarSingal(String direction){
        JSONObject command = new JSONObject();
        command.put("action", "echo");
        command.put("direction", direction);
        return command.toString();
    }

    public  void processRadarResponse(String direction, String response){
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
