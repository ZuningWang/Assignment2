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
}

