package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private CommandList commandList;
    private DroneState droneState;

    @Override
    public void initialize(String s) {   // initialize() only be called once
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);


        commandList = new CommandList(); //list of commands
        commandList.addTestCommands();

        droneState = new DroneState();
        droneState.initializeDrone(direction, batteryLevel.intValue()); //initialize the drone state
    }

    @Override
    public String takeDecision() { //takeDecision() only return one command once

        JSONObject command = commandList.getNextCommand(); //get next command from the command list
        logger.info("** Decision: {}", command.toString());

        if(command.getString("action").equals("heading")){
            String direction = command.getJSONObject("parameters").getString("direction");
            droneState.updateHeading(Direction.interpretStringDirection(direction));
        }
        return command.toString();

    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");

        droneState.updateBatteryLevel(cost.intValue()); // Deduct the cost of this action from the current battery level

        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);

        logger.info("Current battery level: {}", droneState.getBatteryLevel()); //print current battery level
        logger.info("Current heading: {}", droneState.getDirection());

        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

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
            }
        } else if (extraInfo.has("biomes")) {
            String scanResult = extraInfo.get("biomes").toString();
            logger.trace("SCAN RESULT HERE: {}", scanResult);
        }
    }

    @Override
    public String deliverFinalReport() {
        return "what happen";
    }

}
