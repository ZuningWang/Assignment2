package ca.mcmaster.se2aa4.island.teamXXX;

import static ca.mcmaster.se2aa4.island.teamXXX.Direction.EAST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoastlineStrategy implements ExplorationStrategy {

    private final Logger logger = LogManager.getLogger();

    private int stage = 0;
    private int rangeX = 0;
    private int rangeY = 0;
    private boolean onlyOcean = true;
    private String echoResultL;
    private int echoRangeL;
    private String echoResultR;
    private int echoRangeR;
    private int startX;
    private int startY;
    private Direction startHeading;

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
                } else if (rangeY == 0) {
                    rangeY = extraInfo.getInt("range");
                    logger.trace("THE RANGE OF Y IS: {}", rangeX);
                    commandList.addCommand(CreateCommand.newFlyCommand());
                    commandList.addCommand(CreateCommand.newEchoCommand("S"));
                } else if (extraInfo.has("found")) {
                    String echoResult = extraInfo.getString("found");
                    logger.trace("ECHO RESULT HERE: {}", echoResult);
                    if (echoResult.equals("OUT_OF_RANGE")) {
                        logger.trace("ADDING MORE INSTRUCTIONS");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newEchoCommand("S"));
                    } else {
                        int yDistance = extraInfo.getInt("range");
                        commandList.addCommand(CreateCommand.newFlyCommand());
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        commandList.addCommand(CreateCommand.newHeadingCommand("W"));
                        commandList.addCommand(CreateCommand.newHeadingCommand("S"));
                        for (int i = 0; i < yDistance - 2; i++) {
                            commandList.addCommand(CreateCommand.newFlyCommand());
                        }
                        stage++;
                        logger.trace("MOVING ONTO NEXT STAGE:{}", stage);
                        logger.info("NUMBER OF COMMANDS: {}", commandList.numCommands());

                    }
                } else if (extraInfo.has("biomes")) {
                    String scanResult = extraInfo.get("biomes").toString();
                    logger.info("SCAN RESULT HERE: {}", scanResult);
                }
                break;
            case 1:
                logger.warn("       reached stage 1");
                if (commandList.numCommands() == 0) {
                    commandList.addCommand(CreateCommand.newScanCommand());
                    commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().left()));
                    logger.info("                           this works i think hopefully");
                    stage++;
                }
                break;
            case 2:
                if (startX == 0 && startY == 0) {
                    startX = droneState.getPositionX();
                    startY = droneState.getPositionY();
                    startHeading = droneState.getDirection();
                }
                logger.warn("       reached stage 2");
                if (droneState.getPositionY() > 52 || droneState.getPositionY() < 2 || droneState.getPositionX() < 2 || droneState.getPositionX() > 52) {
                    logger.warn("RANGE ERROR");
                    commandList.emptyCommands();
                    commandList.addCommand(CreateCommand.newStopCommand());
                }
                if (droneState.getPositionX() == startX && droneState.getPositionY() == startY) {
                    logger.info("Completed Loop");
                    commandList.emptyCommands();
                    commandList.addCommand(CreateCommand.newStopCommand());
                }

                if (extraInfo.has("found")) {
                    logger.warn("       has found");
                    String echoResult = extraInfo.getString("found");
                    int echoRange = extraInfo.getInt("range");
                    logger.info("ECHO RESULT HERE: {}, {}", echoResult, echoRange);

                    // if (echoResult.equals("GROUND") && echoRange > 0) {
                    //     logger.warn("ADDING ADJUST");
                    //     commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                    //     commandList.addCommand(CreateCommand.newScanCommand());
                    //     for (int i = 0; i < echoRange - 2; i++) {
                    //         commandList.addCommand(CreateCommand.newFlyCommand());
                    //         commandList.addCommand(CreateCommand.newScanCommand());
                    //     }
                    //     commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().forwards()));
                    //     commandList.addCommand(CreateCommand.newScanCommand());
                    //     commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().left()));
                    // } else if (echoResult.equals("OUT_OF_RANGE")) {
                    //     logger.warn("ADDING TURN");
                    //     commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                    //     commandList.addCommand(CreateCommand.newScanCommand());
                    //     commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().backwards()));
                    // } else {
                    //     logger.warn("ADDING STRAIGHT");
                    //     commandList.addCommand(CreateCommand.newFlyCommand());
                    //     commandList.addCommand(CreateCommand.newScanCommand());
                    //     commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().left()));
                    // }

                    if (echoResultL == null && echoResultR == null) {
                        echoResultL = echoResult;
                        echoRangeL = echoRange;
                        commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().right()));
                        logger.warn("ECHORESULTL AND ECHORANGEL: {},{}",echoResultL, echoRangeL);
                    } else if (echoResultR == null) {
                        echoResultR = echoResult;
                        echoRangeR = echoRange;
                        logger.warn("ECHORESULTR AND ECHORANGER: {},{}",echoResultR, echoRangeR);
                        if (echoResultL.equals("GROUND") && echoRangeL > 0) {
                            logger.warn("ADDING ADJUST");
                            commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                            commandList.addCommand(CreateCommand.newScanCommand());
                            for (int i = 0; i < echoRangeL - 2; i++) {
                                commandList.addCommand(CreateCommand.newFlyCommand());
                                commandList.addCommand(CreateCommand.newScanCommand());
                            }
                            commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().forwards()));
                            commandList.addCommand(CreateCommand.newScanCommand());
                            commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().left()));
                        } else if (echoResultL.equals("OUT_OF_RANGE")) {
                            logger.warn("ADDING TURN");
                            commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().left()));
                            commandList.addCommand(CreateCommand.newScanCommand());
                            commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().backwards()));
                        } else if (echoResultL.equals(echoResultR) && echoResultL.equals("GROUND")) {
                            commandList.addCommand(CreateCommand.newHeadingCommand(droneState.getDirection().right()));
                            commandList.addCommand(CreateCommand.newScanCommand());
                            commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().forwards()));
                        } else {
                            logger.warn("ADDING STRAIGHT");
                            commandList.addCommand(CreateCommand.newFlyCommand());
                            commandList.addCommand(CreateCommand.newScanCommand());
                            commandList.addCommand(CreateCommand.newEchoCommand(droneState.getDirection().left()));
                        }
                        logger.warn("ECHO RESULTS RESET");
                        echoResultL = null;
                        echoResultR = null;
                    } else {
                        logger.warn("IDK WHAT HAPPNED");
                    }

                } else if (extraInfo.has("biomes")) {
                    logger.warn("       has biomes");
                    JSONArray scanResult = extraInfo.getJSONArray("biomes");
                    for (int i = 0; i < scanResult.length(); i++) {
                        String scanResultElement = scanResult.getString(i);
                        logger.info("SCAN RESULT {}: {}", i+1, scanResultElement); 
                    }
                    if (scanResult.length() == 1 && scanResult.getString(0).equals("OCEAN")) {
                        onlyOcean = true;
                    } else {
                        onlyOcean = false;
                    }
                }
                break;
            default:
                logger.warn("THIS SHOULD NEVER HAPPEN");
                commandList.addCommand(CreateCommand.newStopCommand());
                break;
        }
    }
}
