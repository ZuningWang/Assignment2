package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SweepStrategy implements ExplorationStrategy {

    private final Logger logger = LogManager.getLogger();

    private int stage = 0;
    private boolean hasLand = false;
    private int rangeX = 0;
    private int rangeY = 0;
    private int startX = 0;

    // starts by determining bound range of x
    public void start(DroneState droneState, CommandList commandList) {
        commandList.addCommand(CreateCommand.newEchoCommand("E"));
    }

    public void explore(DroneState droneState, CommandList commandList, JSONObject extraInfo) {
        switch (stage) {
            case 0:

                if (rangeX == 0) {
                    // updates bound range of x, sends command to check bound range of y
                    rangeX = extraInfo.getInt("range");
                    logger.info("THE RANGE OF X IS: {}", rangeX);
                    commandList.addCommand(CreateCommand.newEchoCommand("S"));
                } else if (rangeY == 0) {
                    // updates bound range of y, send command to start search
                    rangeY = extraInfo.getInt("range");
                    logger.info("THE RANGE OF Y IS: {}", rangeY);
                    commandList.addCommand(CreateCommand.newFlyCommand());
                    commandList.addCommand(CreateCommand.newEchoCommand("S"));
                    startX++;
                } else if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.trace("ECHO RESULT: {}", echoResult);

                    if (echoResult.equals("OUT_OF_RANGE")) {
                        // no land found, continue east
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newEchoCommand("S"));
                        startX++;
                    } else {
                        // land found, perform 90 degree turn maneuver
                        int yDistance = extraInfo.getInt("range");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        // fly distance to land
                        for (int i = 0; i < yDistance - 2; i++) {
                            commandList.addCommand(CreateCommand.newFlyCommand());
                        }
                        commandList.addCommand(CreateCommand.newScanCommand());
                        stage++;
                        logger.trace("MOVING ONTO NEXT STAGE: {}", stage);
                        logger.info("NUMBER OF COMMANDS: {}", commandList.numCommands());
                    }

                } else if (extraInfo.has("biomes")) {
                    JSONArray scanResult = extraInfo.getJSONArray("biomes");

                    for (int i = 0; i < scanResult.length(); i++) {
                        String scanResultElement = scanResult.getString(i);
                        logger.trace("SCAN RESULT {}: {}", i+1, scanResultElement); 
                    }

                }
                break;

            case 1:

                // if drone is going to fly out of range, return to base
                if ((droneState.getPositionY() < 2 && droneState.getPositionX() > startX + 2) || droneState.getPositionX() > rangeX - 2 || droneState.getPositionY() > rangeY) {
                    commandList.emptyCommands();
                    logger.warn("STOPPING BECAUSE OF RANGE");
                    commandList.addCommand(CreateCommand.newStopCommand());
                }

                if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.trace("ECHO RESULT: {}", echoResult);
                } else if (extraInfo.has("biomes")) {

                    // add points of interest
                    if(!extraInfo.get("creeks").toString().equals("[]")){
                        TargetFound.getInstance().addCreeksFound(extraInfo.get("creeks").toString(),"Creek", droneState.getPositionX(), droneState.getPositionY());
                    }
                    if(!extraInfo.get("sites").toString().equals("[]")){
                        TargetFound.getInstance().addSiteFound(extraInfo.get("sites").toString(),"Site", droneState.getPositionX(), droneState.getPositionY());
                    }

                    JSONArray scanResult = extraInfo.getJSONArray("biomes");
                    
                    if (commandList.numCommands() == 0) {

                        // sets variable based on scan results
                        for (int i = 0; i < scanResult.length(); i++) {
                            String scanResultElement = scanResult.getString(i);
                            if (!scanResultElement.equals("OCEAN")) {
                                hasLand = true;
                            }
                        }
                        
                        if (hasLand == true && scanResult.length() == 1 && scanResult.getString(0).equals("OCEAN")) {
                            // perform 180 degree turn maneuver
                            commandList.addCommand(CreateCommand.newHeadingCommand("E"));
                            commandList.addCommand(CreateCommand.newFlyCommand());
                            commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().backwards()));
                            commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                            commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().backwards()));
                            commandList.addCommand(CreateCommand.newScanCommand());
                            hasLand = false;
                        } else {
                            //fly through land
                            commandList.addCommand(CreateCommand.newFlyCommand());
                            commandList.addCommand(CreateCommand.newScanCommand());
                        }
                    }

                    for (int i = 0; i < scanResult.length(); i++) {
                        String scanResultElement = scanResult.getString(i);
                        logger.trace("SCAN RESULT {}: {}", i+1, scanResultElement); 
                    }

                    // stop after finding first creek
                    // JSONArray creek = extraInfo.getJSONArray("creeks");
                    // if (creek.length() != 0) {
                    //     logger.info("CREEK: {}",creek.getString(0));
                    //     commandList.emptyCommands();
                    //     logger.warn("FOUND CREEK");
                    //     commandList.addCommand(CreateCommand.newStopCommand());
                    // }

                    // stop after finding site
                    // JSONArray site = extraInfo.getJSONArray("sites");
                    // if (site.length() != 0) {
                    //     logger.info("SITE: {}",site.getString(0));
                    //     commandList.emptyCommands();
                    //     logger.warn("FOUND SITE");
                    //     commandList.addCommand(CreateCommand.newStopCommand());
                    // }

                }
                
                break;
        }
    }

}