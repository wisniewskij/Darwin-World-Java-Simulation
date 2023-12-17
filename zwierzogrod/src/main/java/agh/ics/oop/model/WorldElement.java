package agh.ics.oop.model;

import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.Vector2d;

/**
 * The interface responsible for objects on the map.
 * @author jwisniewski
 */
public interface WorldElement {
    /**
     * Get object's position.
     * @return object's position as {@link Vector2d} system
     */
    Vector2d getPosition();
    /**
     * Get object's symbol representatnion.
     * @return object's symbol representatnion as string.
     */
    String toString();
    /**
     * Move an object.
     * @return Boolean telling if object moved.
     */
    boolean move(MoveValidator validator);

}
