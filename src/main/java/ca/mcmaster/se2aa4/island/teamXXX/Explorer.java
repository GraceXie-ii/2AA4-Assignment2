package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;
//import org.json.JSONArray;

public class Explorer implements IExplorerRaid {

    // Initialize the logger
    private final Logger logger = LogManager.getLogger();

    // Private JSON objects to store exploration info and results info
    private JSONObject info, extraInfo;

    // private classes of sub-classes
    Drone drone;

    // Private variables to keep track of the state of the exploration
    private boolean scanned = false, radared = false, Newfoundland = false;

    @Override
    public void initialize(String s) {
        // create new JSON reader info
        logger.info("** Initializing the Exploration Command Center");
        info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));

        // create variables to get drone heading and battery level
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        // Initialize drone state
        drone = new Drone(batteryLevel, direction);

    }

    @Override
    public String takeDecision() {
        // Initialize the decision JSON object, variables to store found value and range
        JSONObject decision = new JSONObject();
        String foundValue = "";
        int rangeValue = 0;

        // If battery is low, stop mission
        if (info.getInt("budget") < 100) {
            decision = drone.stop(); // stop
        }

        // If last decision was echo, get value and range
        if (radared) {
            if (extraInfo.has("found")) {
                foundValue = extraInfo.getString("found");
                logger.info("Found value: {}", foundValue);
            }
            if (extraInfo.has("range")) {
                rangeValue = extraInfo.getInt("range");
                logger.info("Range value: {}", rangeValue);
            }
        }

        // Implement the decision logic
        if (scanned == false) { // If not scanned, scan
            decision = drone.scan(); // scan
        }
        else if (radared == false) { // If not radared, radar
            decision = drone.echo("S"); // echo [S]outh
        }
        else if (Newfoundland == false) { // ocean search phase
            if (foundValue.equals("GROUND")) {
                decision = drone.heading("S"); // turn [S]outh (>v)
                Newfoundland = true;
            }
            else { // if foundValue.equals("OUT_OF_RANGE")
                decision = drone.fly(); // fly [E]ast
            }
        }
        else { // newfoundland is true
            if (rangeValue >= 2) {
                decision = drone.fly(); // fly [S]outh to coast
            }
            else { // if rangeValue < 2
                logger.info("Land is found"); // log land is found and stop mission !!!FOR MVP!!!
                decision = drone.stop(); // stop
            }
        }
        
        
        // rotate strategy state: scan, radar, fly/heading/stop
        if (!scanned && !radared) {
            scanned = true;
        }
        else if (scanned && !radared) {
            radared = true;
        }
        else if (scanned && radared) {
            scanned = false;
            radared = false;
        }

        // Log the decision
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        // Create the response JSON object
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));

        // Get the cost, status and extra information
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        extraInfo = response.getJSONObject("extras"); // store extra information in self class
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        // log land is found !!!FOR MVP!!!
        if (Newfoundland) {
            return "Land is found for MVP!!!";
        }
        else {
            return "no creek found";
        }
    }

}
