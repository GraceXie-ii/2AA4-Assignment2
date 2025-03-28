package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.teamXXX.Actions.Move;
import ca.mcmaster.se2aa4.island.teamXXX.Actions.PhotoScanner;
import ca.mcmaster.se2aa4.island.teamXXX.Actions.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Datas.Direction;
import ca.mcmaster.se2aa4.island.teamXXX.Datas.Position;
import ca.mcmaster.se2aa4.island.teamXXX.Strategies.DroneStrategy;
import eu.ace_design.island.bot.IExplorerRaid;

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
        strategy = new DroneStrategy("bruteForce"); // choose strategy: "findLand"/"bruteForce"/"topLeft"

    }

    @Override
    public String takeDecision() {
        // Initialize the decision JSON object as a string, and get decision from strategy
        String decision = strategy.getStrategy(this.batteryLevel, radar.getRadarInfo(), scanner.getScanInfo(), droneDir.getDroneDir(), position.allFound());

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
            // update drone position estimation by 1 grid before direction and 1 grid after direction change
            position.updatePosition(droneDir.getDirectionVector());
            decision = move.heading(droneDir.turn("R"));
            position.updatePosition(droneDir.getDirectionVector());
        }
        else if (decision.equals("heading left")) {
            // update drone position estimation by 1 grid before direction and 1 grid after direction change
            position.updatePosition(droneDir.getDirectionVector());
            decision = move.heading(droneDir.turn("L"));
            position.updatePosition(droneDir.getDirectionVector());
        }
        else if (decision.equals("fly")) {
            decision = move.fly();
            // update drone position estimation by 1 grid, direction does not change
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
        //return "no creek found";
        String report = "The following creeks were found:\n";

        // Get creeks and sites - ID's and Coordinates
        List<String> creeksID = position.getCreeksID();
        List<String> sitesID = position.getSitesID();
        List<int[]> creeksCoors = position.getCreeksCoordinates();
        List<int[]> sitesCoors = position.getSitesCoordinates();

        // append creek ID's and coordinates
        for (int i = 0; i < creeksID.size(); i++) {
            report += "\tCreek #" + String.valueOf(i + 1) + ": ";
            report += "uid: " + creeksID.get(i) + ", ";
            report += "At [x, y]: " + String.valueOf(creeksCoors.get(i)[0]) + ", " + String.valueOf(creeksCoors.get(i)[1]) + "\n";
        }

        // append the emergency site and coordinate
        for (int j = 0; j < sitesID.size(); j++) {
            report += "Found Site #" + String.valueOf(j + 1) + ": ";
            report += "uid: " + sitesID.get(j) + ", ";
            report += "At [x, y]: " + String.valueOf(sitesCoors.get(j)[0]) + ", " + String.valueOf(sitesCoors.get(j)[1]) + "\n";
        }

        // Get closest creek # and distance
        int closestCreekID = (int) (position.findNearestCreek()[0]);
        double closestCreekDistance = position.findNearestCreek()[1];

        // Add to report
        report += "The closest creek was creek #" + String.valueOf(closestCreekID+1) + ", with a distance of " + String.valueOf(closestCreekDistance) + "\n";

        // temporary to see format
        logger.info("Final Report:\n {}", report);

        return report;
    }

}
