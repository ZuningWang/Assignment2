package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SweepStrategy implements ExplorationStrategy {

    private final Logger logger = LogManager.getLogger();

    private int stage = 0;
    private int hasOcean = 0;
    private boolean hasLand = false;
    private int rangeX = 0;
    private int rangeY = 0;
    private int startX = 0;

    public void start(DroneState droneState, CommandList commandList) {
        commandList.addCommand(CreateCommand.newEchoCommand("E"));
    }

    public void explore(DroneState droneState, CommandList commandList, JSONObject extraInfo) {
        switch (stage) {
            case 0:
                if (rangeX == 0) {
                    rangeX = extraInfo.getInt("range");
                    logger.trace("THE RANGE OF X IS: {}", rangeX);
                    commandList.addCommand(CreateCommand.newEchoCommand("S"));
                    startX++;
                } else if (rangeY == 0) {
                    rangeY = extraInfo.getInt("range");
                    logger.trace("THE RANGE OF Y IS: {}", rangeX);
                    commandList.addCommand(CreateCommand.newFlyCommand());
                    commandList.addCommand(CreateCommand.newEchoCommand("S"));
                    startX++;
                } else if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.trace("ECHO RESULT HERE: {}", echoResult);
                    if (echoResult.equals("OUT_OF_RANGE")) {
                        logger.trace("ADDING MORE INSTRUCTIONS");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newEchoCommand("S"));
                        startX++;
                    } else {
                        int yDistance = extraInfo.getInt("range");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        for (int i = 0; i < yDistance - 2; i++) {
                            commandList.addCommand(CreateCommand.newFlyCommand());
                        }
                        commandList.addCommand(CreateCommand.newScanCommand());
                        stage++;
                        logger.trace("MOVING ONTO NEXT STAGE:{}", stage);
                        logger.info("NUMBER OF COMMANDS: {}", commandList.numCommands());

                    }
                } else if (extraInfo.has("biomes")) {
                    String scanResult = extraInfo.get("biomes").toString();
                    logger.info("SCAN RESULT HERE: {}", scanResult);

//add interested sites
                    if(!extraInfo.get("creeks").toString().equals("[]")){
                        TargetFound.getInstance().addCreeksFound(extraInfo.get("creeks").toString(),"Creek", droneState.getPositionX(), droneState.getPositionY());
                    }
                    if(!extraInfo.get("sites").toString().equals("[]")){
                        TargetFound.getInstance().addCreeksFound(extraInfo.get("sites").toString(),"Site", droneState.getPositionX(), droneState.getPositionY());
                    }

                }
                break;
            case 1:
                
                // if ((droneState.getBatteryLevel() < 7000)) {
                //     commandList.emptyCommands();
                //     commandList.addCommand(CreateCommand.newStopCommand());
                // }

                if ((droneState.getPositionY() < 2 && droneState.getPositionX() > startX + 2) || droneState.getPositionX() > rangeX - 2 || droneState.getPositionY() > rangeY) {
                    commandList.emptyCommands();
                    logger.warn("STOPPING BECAUSE OF RANGE");
                    commandList.addCommand(CreateCommand.newStopCommand());
                }

                if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.info("ECHO RESULT HERE: {}", echoResult);
                } else if (extraInfo.has("biomes")) {
//add interested sites
                    if(!extraInfo.get("creeks").toString().equals("[]")){
                        TargetFound.getInstance().addCreeksFound(extraInfo.get("creeks").toString(),"Creek", droneState.getPositionX(), droneState.getPositionY());
                    }
                    if(!extraInfo.get("sites").toString().equals("[]")){
                        TargetFound.getInstance().addCreeksFound(extraInfo.get("sites").toString(),"Site", droneState.getPositionX(), droneState.getPositionY());
                    }

                    JSONArray scanResult = extraInfo.getJSONArray("biomes");
                    
                    if (commandList.numCommands() == 0) {

                        for (int i = 0; i < scanResult.length(); i++) {
                            String scanResultElement = scanResult.getString(i);
                            if (!scanResultElement.equals("OCEAN")) {
                                hasOcean = 0;
                                hasLand = true;
                            }
                        }

                        if (hasOcean == 2) {
                            logger.warn("STOPPING BECAUSE OF DOUBLE OCEAN");
                            commandList.addCommand(CreateCommand.newStopCommand());
                        } else if (hasLand == true && scanResult.length() == 1 && scanResult.getString(0).equals("OCEAN")) {

                            switch (droneState.getDirection()) {
                                case NORTH:
                                    commandList.addCommand(CreateCommand.newHeadingCommand("E"));
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                                    commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                                    commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    hasLand = false;
                                    hasOcean++;
                                    break;
                                case EAST:
                                    break;
                                case SOUTH:
                                    commandList.addCommand(CreateCommand.newHeadingCommand("E"));
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("N"));
                                    commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                                    commandList.addCommand(CreateCommand.newHeadingCommand("N"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    hasLand = false;
                                    hasOcean++;
                                    break;
                                case WEST:
                                    break;
                                default:
                                    commandList.addCommand(CreateCommand.newStopCommand());
                                    logger.warn("shouldnt ever happen");
                                    break;
                            }
                        } else {
                            commandList.addCommand(CreateCommand.newFlyCommand());
                            commandList.addCommand(CreateCommand.newScanCommand());
                        }
                    }

                    for (int i = 0; i < scanResult.length(); i++) {
                        String scanResultElement = scanResult.getString(i);
                        logger.info("SCAN RESULT {}: {}", i+1, scanResultElement); 
                    }

                    // JSONArray creek = extraInfo.getJSONArray("creeks");
                    // if (creek.length() != 0) {
                    //     logger.info("CREEK: {}",creek.getString(0));
                    //     commandList.emptyCommands();
                    //     logger.warn("FOUND CREEK");
                    //     commandList.addCommand(CreateCommand.newStopCommand());
                    // }

                    // JSONArray site = extraInfo.getJSONArray("sites");
                    // if (site.length() != 0) {
                    //     logger.info("SITE: {}",site.getString(0));
                    //     commandList.emptyCommands();
                    //     logger.warn("FOUND SITE");
                    //     commandList.addCommand(CreateCommand.newStopCommand());
                    // }

                }
                
                break;
            default:
                break;
        }
    }

}