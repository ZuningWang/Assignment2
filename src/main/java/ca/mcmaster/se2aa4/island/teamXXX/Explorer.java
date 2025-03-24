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
    private ExplorationStrategy strategy;

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

        droneState = new DroneState();
        droneState.initializeDrone(direction, batteryLevel.intValue()); //initialize the drone state

        strategy = new SweepStrategy();
        strategy.start(droneState, commandList);
    }

    @Override
    public String takeDecision() { //takeDecision() only return one command once

        JSONObject command = commandList.getNextCommand(); //get next command from the command list
        logger.info("** Decision: {}", command.toString());

        if(command.getString("action").equals("heading")){
            Direction direction = Direction.interpretStringDirection(command.getJSONObject("parameters").getString("direction"));
            droneState.updateHeading(direction);
        }

        if(command.getString("action").equals("fly")){
            droneState.flyForward();
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
        logger.info("Current position: ({},{})", droneState.getPositionX(), droneState.getPositionY());

        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

        strategy.explore(droneState, commandList, extraInfo);
        
    }

    @Override
    public String deliverFinalReport() {

        TargetFound.getInstance().printResults();
        return "what happen";
    }

}
