package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

public class CommandList {
    private List<String> commandList;

    public CommandList() {
        commandList = new ArrayList<>();
    }

    public void addCommand(String command) {
        commandList.add(command);
    }

    public String getNextCommand(){
        if(commandList.size() > 0){
            return commandList.remove(0); // remove and return the first command
        }
        return null;
    }

    public void addTestCommands(){ //commands used for test
        commandList.add("fly");
        commandList.add("fly");
        commandList.add("scan");
        commandList.add("stop");

    }
}