package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TargetFound {
    private final Logger logger = LogManager.getLogger();
    private static final TargetFound instance = new TargetFound();

    private List<JSONObject> creeksFound = new ArrayList<>();
    private List<JSONObject> siteFound = new ArrayList<>();

    private TargetFound() {

    }

//    public static JSONObject resultToJSON(String id, String type, int x, int y){
//        JSONObject result = new JSONObject();
//        result.put("ID", id);
//        result.put("Type", type);
//        result.put("X", x);
//        result.put("Y", y);
//        return result;
//    }

    public static TargetFound getInstance() {
        return instance;
    }

    public void addCreeksFound(String id, String type, int x, int y) {
        JSONObject result = new JSONObject();
        result.put("ID", id);
        result.put("Type", type);
        result.put("X", x);
        result.put("Y", y);
        creeksFound.add(result);
    }
    public void addSiteFound(String id, String type, int x, int y) {
        JSONObject result = new JSONObject();
        result.put("ID", id);
        result.put("Type", type);
        result.put("X", x);
        result.put("Y", y);
        siteFound.add(result);
    }

    public List<JSONObject> getCreeksFound() {
        return creeksFound;
    }
    public List<JSONObject> getSiteFound() {
        return siteFound;
    }

    public void printResults(){
        for(JSONObject jo : creeksFound){
            String creek = jo.toString();
            logger.info("Creeks found are {}", creek);
        }


        for(JSONObject jo : siteFound){
            String site = jo.toString();
            logger.info("Site found is {}", site);
        }
    }

    public JSONObject findClosetCreek(){
        double minimum = Integer.MAX_VALUE;
        JSONObject closestCreek = new JSONObject();
        if(siteFound.size() > 0){
            int siteX = siteFound.get(0).getInt("X");
            int siteY = siteFound.get(0).getInt("Y");
            for(JSONObject creek : creeksFound){
                double distance = findDistance(creek.getInt("X"),creek.getInt("Y"),siteX, siteY);
                if(distance < minimum){
                    minimum = distance;
                    closestCreek = creek;
                }
            }
        }
        return closestCreek;
    }

    public double findDistance(int CreekX, int CreekY, int SiteX, int SiteY){
        int deltaX = CreekX - SiteX;
        int deltaY = CreekY - SiteY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance;

    }
}