package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private JSONObject info;
    private int choice = 0;
    
    

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
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        if (choice == 0) {
            decision.put("action", "fly"); // make action JSON {"action": "fly"}
            choice=1; // set to turn south
        }
        else if (choice == 1) {
            decision.put("action", "heading"); // make action JSON {"action": "heading"}
            parameters.put("direction", "S"); // put parameter JSON {"parameters": {"direction": "S"}}
            decision.put("parameters",  parameters); // combine JSON {"action": "heading", "parameters": {"direction": "S"}}

            choice=2; // set to echo
        } 
        else if (choice == 2) {
            decision.put("action", "echo"); // make action JSON {"action": "echo"}
            parameters.put("direction", "E"); // put parameter JSON {"parameters": {"direction": "E"}}
            decision.put("parameters",  parameters); // combine JSON {"action": "echo", "parameters": {"direction": "E"}}

            choice = 3; // set to scan
        }
        else if (choice == 3) {
            decision.put("action", "scan"); // make action JSON {"action": "scan"}

            choice = 4; // set to stop
        }
        else {
            decision.put("action", "stop"); // make action JSON {"action": "stop"}
        }
        //decision.put("action", "stop"); // we stop the exploration immediately
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
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
