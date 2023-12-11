package agh.ics.oop.model.util.exceptions;

import agh.ics.oop.model.util.directions.Vector2d;

public class PositionAlreadyOccupiedException extends Exception {
    public PositionAlreadyOccupiedException(Vector2d pos) {
        super("Pos %s is already occupied".formatted(pos));
    }
}
