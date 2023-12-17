package agh.ics.oop.model.map;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.util.exceptions.PositionAlreadyOccupiedException;

import java.util.List;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {
    /**
     * Notifies ovservers about map change.
     * @param message message indicating what changed.
     */
    public void mapChanged(String message);
    /**
     * Increment map's update counter
     */
    public void incrementUpdateCounter();
    /**
     * @return map's update counter
     */
    public int getUpdateCounter();
    /**
     * @return map's UUID
     */
    UUID getId();
    /**
     * Place an animal on the map
     * @param object The object to place on the map.
     * @throws {@link PositionAlreadyOccupiedException}
     */
    default void place(WorldElement object) throws PositionAlreadyOccupiedException {}
    /**
     * Moves an object (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(WorldElement object);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the object
     * cannot move.
     *
     * @param key Position to check.
     * @return True if the map[key] is occupied.
     */
    boolean isOccupied(Vector2d key);

    /**
     * Return an object at a given position.
     *
     * @param key The key of the object.
     * @return object or null if the map[key] is not occupied.
     */
    WorldElement objectAt(Vector2d key);

    /**
     * @return a list of {@link WorldElement} of objects of map
     */
    List<WorldElement> getElements();

    Boundary getCurrentBounds();
}
