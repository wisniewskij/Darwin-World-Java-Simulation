package agh.ics.oop.model.util;

import agh.ics.oop.model.util.directions.Vector2d;

public interface MoveValidator {

    /**
     * Indicate if any object can move to the given position.
     *
     * @param position The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);
    /**
     * Give the correct output position
     *
     * @param position The position checked for conversion.
     * @return New position for the object.
     */
    Vector2d convertNextPosition(Vector2d position);

}