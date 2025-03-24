package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;
import static ca.mcmaster.se2aa4.island.teamXXX.Direction.*;

public class DroneState {
    private Position position = new Position(1,1); //set initial position
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

    public void updateHeading(Direction newDirection){ //update position after heading action
        if(direction.isValidTurn(newDirection)){ //check if it is U turn
            switch(direction) {
                case NORTH:
                    switch (newDirection) {
                        case EAST:
                            updatePosition(getPositionX() + 1, getPositionY() - 1);
                            break;
                        case WEST:
                            updatePosition(getPositionX() - 1, getPositionY() - 1);
                            break;
                        default:
                            break;
                    }
                    break;
                case EAST:
                    switch (newDirection) {
                        case NORTH:
                            updatePosition(getPositionX() + 1, getPositionY() - 1);
                            break;
                        case SOUTH:
                            updatePosition(getPositionX() + 1, getPositionY() + 1);
                            break;
                        default:
                            break;
                    }
                    break;
                case SOUTH:
                    switch (newDirection) {
                        case EAST:
                            updatePosition(getPositionX() + 1, getPositionY() + 1);
                            break;
                        case WEST:
                            updatePosition(getPositionX() - 1, getPositionY() + 1);
                            break;
                        default:
                            break;
                    }
                    break;
                case WEST:
                    switch (newDirection) {
                        case NORTH:
                            updatePosition(getPositionX() - 1, getPositionY() - 1);
                            break;
                        case SOUTH:
                            updatePosition(getPositionX() - 1, getPositionY() + 1);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            direction = newDirection;
        }
    }

    public void flyForward(){
        switch(direction) {
            case NORTH:
                updatePosition(getPositionX(), getPositionY() - 1);
                break;
            case EAST:
                updatePosition(getPositionX() + 1, getPositionY());
                break;
            case SOUTH:
                updatePosition(getPositionX(), getPositionY() + 1);
                break;
            case WEST:
                updatePosition(getPositionX() - 1, getPositionY());
                break;
            default:
                break;
        }
    }

    public int getBatteryLevel(){
        return batteryLevel;
    }

    public Direction getDirection() {
        return direction;
    }

    public void updatePosition(int x, int y) {
        position.update(x, y);
    }

    public int getPositionX() {
        return position.getX();
    }

    public int getPositionY() {
        return position.getY();
    }
}