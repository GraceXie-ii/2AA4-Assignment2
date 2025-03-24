package ca.mcmaster.se2aa4.island.teamXXX;
import org.json.JSONObject;

public class RectangleSearch implements SearchAlgorithm{

  private boolean scanned = false, radared = false;
  private boolean land = true, left = false, right = false, turned_left = false, empty_check = false;

  @Override
  public String getStrategy(int batteryLevel, JSONObject radarResults, JSONObject scanResults, String droneDir, boolean allFound) {
    String decision = "";

    return decision;
  }
  
}
