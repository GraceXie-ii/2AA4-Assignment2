package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhotoScanner {

    // Declare JSON objects for radar info in FRONT, LEFT, RIGHT directions
    private JSONArray creeks, sites;

    public PhotoScanner(){
        // Initialize radar info JSON objects, last direction and orientation
        this.creeks = new JSONArray();
        this.sites = new JSONArray();
    }

    public String scan() {
        JSONObject decision = new JSONObject(); // create new JSON object
        decision.put("action", "scan"); // make action JSON {"action": "scan"}
        return decision.toString(); // return decision JSON {"action": "scan"}
    }

    public void processScanResponse(JSONArray creeks, JSONArray sites) {
        // If creeks and sites not in this.creeks and this.sites, add them
        for (int i = 0; i < creeks.length(); i++) {
            if (!this.creeks.toString().contains(creeks.toString())) {
                this.creeks.put(creeks);
            }
        }
        for (int i = 0; i < sites.length(); i++) {
            if (!this.sites.toString().contains(sites.toString())) {
                this.sites.put(sites);
            }
        }
    }

    public JSONObject getScanInfo() {
        JSONObject scanInfo = new JSONObject();
        scanInfo.put("creeks", creeks);
        scanInfo.put("sites", sites);
        return scanInfo;
    }
  
}
