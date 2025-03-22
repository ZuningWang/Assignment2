package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestStrategy implements ExplorationStrategy {

    private final Logger logger = LogManager.getLogger();

    private int stage = 0;
    private int ocean = 0;
    private boolean overOcean = false;

    public void explore(DroneState droneState, CommandList commandList, JSONObject extraInfo) {
        switch (stage) {
            case 0:
                if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.trace("ECHO RESULT HERE: {}", echoResult);
                    if (echoResult.equals("OUT_OF_RANGE")) {
                        logger.trace("ADDING MORE INSTRUCTIONS");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newScanCommand());
                        commandList.addCommand(CreateCommand.newEchoCommand("S"));
                    } else {
                        int yDistance = extraInfo.getInt("range");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newScanCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        commandList.addCommand(CreateCommand.newScanCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                        commandList.addCommand(CreateCommand.newScanCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        commandList.addCommand(CreateCommand.newScanCommand());
                        for (int i = 0; i < yDistance - 2; i++) {
                            commandList.addCommand(CreateCommand.newFlyCommand());
                            commandList.addCommand(CreateCommand.newScanCommand());
                        }

                        logger.info("NUMBER OF COMMANDS: {}", commandList.numCommands());
                        stage++;
                        logger.info("MOVING ONTO NEXT STAGE:{}", stage);
                    }
                } else if (extraInfo.has("biomes")) {
                    String scanResult = extraInfo.get("biomes").toString();
                    logger.info("SCAN RESULT HERE: {}", scanResult);
                }
                break;
            case 1:
                if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.info("ECHO RESULT HERE: {}", echoResult);
                } else if (extraInfo.has("biomes")) {

                    JSONArray scanResult = extraInfo.getJSONArray("biomes");

                    if (commandList.numCommands() == 0) {

                        for (int i = 0; i < scanResult.length(); i++) {
                            String scanResultElement = scanResult.getString(i);
                            if (!scanResultElement.equals("OCEAN")) {
                                ocean = 0;
                            }
                        }

                        if (ocean == 2) {
                            commandList.addCommand(CreateCommand.newStopCommand());
                        } else if (scanResult.length() == 1 && scanResult.getString(0).equals("OCEAN")) {
                            logger.info("THIS MFKSFIJSKDA WORKS");
                            switch (droneState.getDirection().toString()) {
                                case "SOUTH":
                                    commandList.addCommand(CreateCommand.newHeadingCommand("E"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("N"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("N"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    ocean++;
                                    break;
                                case "EAST":
                                    break;
                                case "NORTH":
                                    commandList.addCommand(CreateCommand.newHeadingCommand("E"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newFlyCommand());
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                                    commandList.addCommand(CreateCommand.newScanCommand());
                                    ocean++;
                                    break;
                                case "WEST":
                                    break;
                                default:
                                    commandList.addCommand(CreateCommand.newStopCommand());
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

                }

                logger.info("REACHED STAGE ONE:{}", commandList.numCommands());
                
                break;
            default:
                break;
        }
    }

}