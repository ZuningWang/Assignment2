package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.CommandList;
import ca.mcmaster.se2aa4.island.teamXXX.CreateCommand;
import ca.mcmaster.se2aa4.island.teamXXX.DroneState;

public class BoxStrategy implements ExplorationStrategy {

    private final Logger logger = LogManager.getLogger();

    private int stage = 0;
    private int startX;
    private int endX;
    private int startY;
    private int endY;

    public void start(DroneState droneState, CommandList commandList) {
        commandList.addCommand(CreateCommand.newEchoCommand("S"));
    }

    public void explore(DroneState droneState, CommandList commandList, JSONObject extraInfo) {
        switch (stage) {
            case 0:

                if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.trace("ECHO RESULT: {}", echoResult);
                    
                    if (echoResult.equals("GROUND") && startX == 0) {
                        startX = droneState.getPositionX();
                    } else if (echoResult.equals("OUT_OF_RANGE") && startX != 0 && endX == 0) {
                        endX = droneState.getPositionX() - 1;
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                    } else if (echoResult.equals("GROUND") && startX !=0 && endX != 0 && startY == 0) {
                        startY = droneState.getPositionY();
                    } else if (echoResult.equals("OUT_OF_RANGE") && startX !=0 && endX != 0 && startY != 0 && endY == 0) {
                        endY = droneState.getPositionY() - 1;
                        commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("N"));
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        stage++;
                    }

                    commandList.addCommand(CreateCommand.newFlyCommand());
                    commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().right()));

                }
                break;

            case 1:
                if (startX > endX || startY > endY) {
                    commandList.emptyCommands();
                    logger.warn("Completed Loop");
                    commandList.addCommand(CreateCommand.newStopCommand());
                }

                logger.warn("{} {} {} {}",startX,endX,startY,endY);
                if (extraInfo.has("biomes")) {

                    // add points of interest
                    if(!extraInfo.get("creeks").toString().equals("[]")){
                        TargetFound.getInstance().addCreeksFound(extraInfo.get("creeks").toString(),"Creek", droneState.getPositionX(), droneState.getPositionY());
                    }
                    if(!extraInfo.get("sites").toString().equals("[]")){
                        TargetFound.getInstance().addSiteFound(extraInfo.get("sites").toString(),"Site", droneState.getPositionX(), droneState.getPositionY());
                    }

                    if (commandList.numCommands() == 0) {
                        switch (droneState.getDirection()) {
                            case NORTH:
                                if (droneState.getPositionY() > startY + 1) {
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                } else {
                                    commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    startY++;
                                }
                                break;
                            case EAST:
                                if (droneState.getPositionX() < endX - 1) {
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                } else {
                                    commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    endX--;
                                }
                                break;
                            case SOUTH:
                                if (droneState.getPositionY() < endY - 1) {
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                } else {
                                    commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    endY--;
                                }
                                break;
                            case WEST:
                                if (droneState.getPositionX() > startX + 1) {
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                } else {
                                    commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    startX++;
                                }
                                break;
                            default:
                        }
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

                } else if (extraInfo.has("found")) {
                    commandList.addCommand(CreateCommand.newFlyCommand());
                    commandList.addCommand(CreateCommand.newScanCommand());
                }
                
                break;
        }
    }

}