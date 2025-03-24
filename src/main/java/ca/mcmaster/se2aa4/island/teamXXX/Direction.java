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

    public String right() {
        if (this.equals(NORTH)) return "E";
        if (this.equals(EAST)) return "S";
        if (this.equals(SOUTH)) return "W";
        if (this.equals(WEST)) return "N";
        return null;
    }

    public String left() {
        if (this.equals(NORTH)) return "W";
        if (this.equals(EAST)) return "N";
        if (this.equals(SOUTH)) return "E";
        if (this.equals(WEST)) return "S";
        return null;
    }

    public String forwards() {
        if (this.equals(NORTH)) return "N";
        if (this.equals(EAST)) return "E";
        if (this.equals(SOUTH)) return "S";
        if (this.equals(WEST)) return "W";
        return null;
    }

    public String backwards() {
        if (this.equals(NORTH)) return "S";
        if (this.equals(EAST)) return "W";
        if (this.equals(SOUTH)) return "N";
        if (this.equals(WEST)) return "E";
        return null;
    }
}

