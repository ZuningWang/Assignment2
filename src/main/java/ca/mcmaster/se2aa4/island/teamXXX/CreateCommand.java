package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import org.json.JSONTokener;

public class CreateCommand{ //create JSON object of each command


    public static JSONObject newFlyCommand(){
        JSONObject decision = new JSONObject();
        decision.put("action", "fly");
        return decision;
    }

    public static JSONObject newHeadingCommand(String heading){
        JSONObject decision = new JSONObject();
        decision.put("action", "heading");
        JSONObject direction = new JSONObject();
        direction.put("direction", heading);
        decision.put("parameters", direction);
        return decision;
    }

    public static JSONObject newEchoCommand(String heading){
        JSONObject decision = new JSONObject();
        decision.put("action", "echo");
        JSONObject direction = new JSONObject();
        direction.put("direction", heading);
        decision.put("parameters", direction);
        return decision;
    }

    public static JSONObject newScanCommand(){
        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }

    public static JSONObject newStopCommand(){
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");
        return decision;
    }
}