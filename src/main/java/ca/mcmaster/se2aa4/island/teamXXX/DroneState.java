package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;
import static ca.mcmaster.se2aa4.island.teamXXX.Direction.*;

public class DroneState {
    private Position position;
    private Direction direction;
    private int batteryLevel;

    public DroneState() {
        batteryLevel = 0;
    }

    public void initializeDrone(String direction, int batteryLevel){
        this.direction = interpretStringDirection(direction);
        this.batteryLevel = batteryLevel;
        System.out.println("Battery level: " + batteryLevel);
    }

    public void updateBatteryLevel(int cost){ //Deduct the cost of this action from the current battery level
        batteryLevel -= cost;
    }

    public void updateHeading(Direction newDirection){
        if(direction.isValidTurn(newDirection)){ //check if it is U turn
            direction = newDirection;
        }
    }

    public int getBatteryLevel(){
        return batteryLevel;
    }

    public Direction getDirection() {
        return direction;
    }
}