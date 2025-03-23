package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public interface ExplorationStrategy {

    public void start(DroneState droneState, CommandList commandList);
    public void explore(DroneState droneState, CommandList commandList, JSONObject extraInfo);

}