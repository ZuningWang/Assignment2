package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

import org.json.JSONObject;

public interface ExplorationStrategy {

    public void explore(DroneState droneState, CommandList commandList, JSONObject extraInfo);

}