package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.*;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction interpretStringDirection(String direction) {
        if (direction.equals("N")) return NORTH;
        if (direction.equals("E")) return EAST;
        if (direction.equals("S")) return SOUTH;
        if (direction.equals("W")) return WEST;
        return null;
    }

    public boolean isValidTurn(Direction direction){ //return false if it is U turn
        switch(this){
            case NORTH:
                return direction != SOUTH;
            case SOUTH:
                return direction != NORTH;
            case WEST:
                return direction != EAST;
            case EAST:
                return direction != WEST;
            default: return false;
        }
    }


}

