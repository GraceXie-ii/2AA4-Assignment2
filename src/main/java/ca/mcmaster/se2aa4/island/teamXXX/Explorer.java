package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;
//import org.json.JSONArray;

public class Explorer implements IExplorerRaid {

    // Initialize the logger
    private final Logger logger = LogManager.getLogger();

    // Private JSON objects to store exploration info and results info
    private JSONObject info;

    // private classes of sub-classes
    private Direction droneDir;
    private Move move;
    private Radar radar;
    private PhotoScanner scanner;

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

        // Initialize drone direction, basic movements, radar, and scanner
        droneDir = new Direction(direction);
        move = new Move();
        radar = new Radar();
        scanner = new PhotoScanner();

    }

    @Override
    public String takeDecision() {
        // Initialize the decision JSON object, variables to store found value and range
        String decision = "";

        // Implement the decision logic
        // If battery is low, stop mission; ocean search phase - scan/radar S/move E; ground phase - move S/stop
        if (info.getInt("budget") < 100) { // If battery is low, stop mission
            decision = move.stop(); // stop
        }
        else if (scanned == false) { // If not scanned, scan
            decision = scanner.scan(); // scan
        }
        else if (radared == false) { // If not radared, radar
            if (Newfoundland == false) {
                decision = radar.sendRadarSignal("RIGHT", droneDir.ifTurn("RIGHT")); // echo south, ocean search phase
            }
            else {
                decision = radar.sendRadarSignal("FRONT", droneDir.ifTurn("FRONT")); // echo south, approaching coast
            }
        }
        else if (Newfoundland == false) { // ocean search phase
            if (radar.getRadarInfo().getString("found").equals("GROUND")) {
                decision = move.heading(droneDir.turn("R")); // turn [S]outh (>v)
                Newfoundland = true;
            }
            else { // if foundValue.equals("OUT_OF_RANGE")
                decision = move.fly(); // fly [E]ast
            }
        }
        else { // land is found
            if (radar.getRadarInfo().getInt("range") >= 2) {
                decision = move.fly(); // fly [S]outh to coast
            }
            else { // if rangeValue < 2
                logger.info("Land is found"); // log land is found and stop mission !!!FOR MVP!!!
                decision = move.stop(); // stop
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
        logger.info("** Decision: {}",decision);
        return decision;
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
        JSONObject extraInfo = response.getJSONObject("extras"); // store extra information in self class
        logger.info("Additional information received: {}", extraInfo);

        // If echo, found and range values are present, update them in radar
        if (extraInfo.has("found")) {
            radar.processRadarResponse(extraInfo.getString("found"), extraInfo.getInt("range"));
        }
        // If scan, creeks and sites values are present, update them in scanner
        else if (extraInfo.has("creeks")) {
            scanner.processScanResponse(extraInfo.getJSONArray("creeks"), extraInfo.getJSONArray("sites"));
        }
    }

    @Override
    public String deliverFinalReport() {
        // log land is found !!!FOR MVP!!!
        if (Newfoundland) {
            logger.info("Scanned creeks and sites: {}", scanner.getScanInfo().toString(2)); // temporary scan log
            return "Land is found for MVP!!!";
        }
        else {
            return "no creek found";
        }
    }

}
