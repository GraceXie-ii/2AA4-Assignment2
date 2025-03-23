package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public interface SearchAlgorithm {
    public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir, boolean allFound);
}
