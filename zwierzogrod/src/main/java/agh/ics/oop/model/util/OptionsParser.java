package agh.ics.oop.model.util;

import agh.ics.oop.model.util.directions.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> stringToMoveDirectionEnum(String[] args) {

        List<MoveDirection> directionsArray = new ArrayList<>();
        for(String arg: args)
            switch (arg) {
                case "f" -> directionsArray.add(MoveDirection.FORWARD) ;
                case "b" -> directionsArray.add(MoveDirection.BACKWARD);
                case "r" -> directionsArray.add(MoveDirection.RIGHT);
                case "l" -> directionsArray.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }

        return directionsArray;
    }
}
