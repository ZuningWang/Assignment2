package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CommandList {
    private List<JSONObject> commandList;

    public CommandList() {
        commandList = new ArrayList<>();
    }

    public void addCommand(JSONObject command) {
        commandList.add(command);
    }

    public JSONObject getNextCommand(){
        if(commandList.size() > 0){
            return commandList.remove(0); // remove and return the first command
        }
        return null;
    }

    public void addTestCommands(){ //commands used for test
        commandList.add(CreateCommand.newFlyCommand());
        commandList.add(CreateCommand.newHeadingCommand("S"));
        commandList.add(CreateCommand.newFlyCommand());
        commandList.add(CreateCommand.newHeadingCommand("E"));
        commandList.add(CreateCommand.newHeadingCommand("N"));
        commandList.add(CreateCommand.newEchoCommand("E"));
        commandList.add(CreateCommand.newStopCommand());

    }

//    public void updatingCommandAction(JSONObject command){
//        if(command.getString("action").equals("heading")){
//            String direction = command.getJSONObject("parameters").getString("direction");
//            DroneState.updateHeading(Direction.interpretStringDirection(direction));
//        }
//    }
}