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
    private DroneStrategy strategy;
    private Position position;

    private Integer batteryLevel;

    @Override
    public void initialize(String s) {
        // create new JSON reader info
        logger.info("** Initializing the Exploration Command Center");
        info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));

        // create variables to get drone heading and battery level
        String direction = info.getString("heading");
        this.batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        // Initialize drone direction, basic movements, radar, scanner, position, and strategy set to find land
        droneDir = new Direction(direction);
        move = new Move();
        radar = new Radar();
        scanner = new PhotoScanner();
        position = new Position();
        strategy = new DroneStrategy("bruteForce"); // choose strategy

    }

    @Override
    public String takeDecision() {
        // Initialize the decision JSON object as a string, and get decision from strategy
        String decision = strategy.getStrategy(this.batteryLevel, radar.getRadarInfo(), scanner.getScanInfo(), droneDir.getDroneDir());

        // translate drone command strings to function calls, update drone position if heading or fly
        if (decision.equals("stop")) {
            decision = move.stop();
        }
        else if (decision.equals("scan")) {
            decision = scanner.scan();
        }
        else if (decision.equals("echo right")) {
            decision = radar.sendRadarSignal("RIGHT", droneDir.ifTurn("RIGHT"));
        }
        else if (decision.equals("echo front")) {
            decision = radar.sendRadarSignal("FRONT", droneDir.ifTurn("FRONT"));
        }
        else if (decision.equals("echo left")) {
            decision = radar.sendRadarSignal("LEFT", droneDir.ifTurn("LEFT"));
        }
        else if (decision.equals("heading right")) {
            decision = move.heading(droneDir.turn("R"));
            position.updatePosition(droneDir.getDirectionVector("R"));
        }
        else if (decision.equals("heading left")) {
            decision = move.heading(droneDir.turn("L"));
            position.updatePosition(droneDir.getDirectionVector("L"));
        }
        else if (decision.equals("fly")) {
            decision = move.fly();
            position.updatePosition(droneDir.getDirectionVector());
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
        else if (extraInfo.has("creeks")||extraInfo.has("sites")) {
            scanner.processScanResponse(extraInfo.getJSONArray("creeks"), extraInfo.getJSONArray("sites"));

            // if creek exists and NOT in creekIDs, store creek ID and coordinates
            for (int i = 0; i < extraInfo.getJSONArray("creeks").length(); i++) {
                if (position.getCreeksID().contains(extraInfo.getJSONArray("creeks").getString(i)) == false) {
                    position.addCreek(extraInfo.getJSONArray("creeks").getString(i), position.getCoordinates());
                }
            }

            // if site exists and NOT in sitesIDs, store site ID and coordinates
            for (int j = 0; j < extraInfo.getJSONArray("sites").length(); j++) {
                if (position.getSitesID().contains(extraInfo.getJSONArray("sites").getString(j)) == false) {
                    position.addSite(extraInfo.getJSONArray("sites").getString(j), position.getCoordinates());
                }
            }
        }

        logger.info("The drone position is {}", position.getCoordinates());
        // update battery level
        this.batteryLevel -= cost;
        logger.info("drone battery is at {}", this.batteryLevel);
    }

    @Override
    public String deliverFinalReport() {
       return "no creek found";
    }

}
