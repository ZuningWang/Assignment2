package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CommandList {
    private List<JSONObject> commandList; //the list of commands

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

    public void emptyCommands() { //remove all commands from the list
        int size = commandList.size();
        for(int i = 0; i < size; i++) {
            commandList.remove(0);
        }
    }


    public int numCommands() {
        return commandList.size();
    }

}