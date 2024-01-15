package agh.ics.oop.model.util;

import agh.ics.oop.model.util.directions.Vector2d;

public interface MoveValidator {

    boolean canMoveTo(Vector2d position);

    Vector2d convertNextPosition(Vector2d position);

    int getConversionCostMultiplier();

}