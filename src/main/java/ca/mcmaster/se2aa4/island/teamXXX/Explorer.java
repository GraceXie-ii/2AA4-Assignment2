package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;
//import org.json.JSONArray;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private JSONObject info, extraInfo;
    //private int choice = 0;
    private boolean radared = false, Newfoundland = false;
    private String flyDir = "E";
    
    

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        // Create the decision and parameter JSON objects
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        String foundValue = "";
        int rangeValue = 0;

        // Check if battery is low, stop
        if (info.getInt("budget") < 100) {
            decision.put("action", "stop"); // make action JSON {"action": "stop"}
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
        /* 
         *  If not Newfoundland:
         *      If not radared, echo [S]outh
         *      Elif (radered and) value is OUT_OF_RANGE:
         *          fly [E]ast
         *      Else (radered and) value is GROUND:
         *          heading [S]outh
         *          change direction to [S]outh
         *          make Newfoundland boolean true
         *  If Newfoundland:
         *      If range >= 2:
         *          fly to coast
         *      Else range < 2:    
         *          log land is found !!!FOR MVP!!!
         *          stop !!!FOR NOW!!!
        */
        if (Newfoundland == false) {
            if (radared == false) {
                // echo [S]outh
                decision.put("action", "echo"); // make action JSON {"action": "echo"}
                parameters.put("direction", "S"); // put parameter JSON {"parameters": {"direction": "E"}}
                decision.put("parameters",  parameters); // combine JSON {"action": "echo", "parameters": {"direction": "E"}}
            }
            else if (foundValue.equals("GROUND")) {
                // heading [S]outh
                decision.put("action", "heading"); // make action JSON {"action": "heading"}
                parameters.put("direction", "S"); // put parameter JSON {"parameters": {"direction": "S"}}
                decision.put("parameters",  parameters); // combine JSON {"action": "heading", "parameters": {"direction": "S"}}
                Newfoundland = true;
                flyDir = "S";
            }
            else { // if foundValue.equals("OUT_OF_RANGE")
                // fly [E]ast
                decision.put("action", "fly"); // make action JSON {"action": "fly"}
                parameters.put("direction", flyDir); // put parameter JSON {"parameters": {"direction": "E"}}
                decision.put("parameters",  parameters); // combine JSON {"action": "fly", "parameters": {"direction": "E"}}
            }
        }
        else { // newfoundland is true
            if (rangeValue >= 2) {
                // fly [S]outh to coast
                decision.put("action", "fly"); // make action JSON {"action": "fly"}
                parameters.put("direction", flyDir); // put parameter JSON {"parameters": {"direction": "E"}}
                decision.put("parameters",  parameters); // combine JSON {"action": "fly", "parameters": {"direction": "E"}}
            }
            else { // if rangeValue < 2
                // log land is found and stop mission !!!FOR MVP!!!
                logger.info("Land is found");
                decision.put("action", "stop"); // make action JSON {"action": "stop"}
            }
        }
        
        // switch state of radared
        radared = !radared;

        // Log the decision
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
