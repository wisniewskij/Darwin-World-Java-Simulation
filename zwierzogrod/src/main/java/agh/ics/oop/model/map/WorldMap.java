package agh.ics.oop.model.map;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.statistics.visitor.Visitable;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.WorldElement;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator, Visitable {
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
     */
    void placeNewAnimal(Animal object);

    /**
     * Moves an object (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */

    void move(Animal object);
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
     * Return an object TtreeSet at a given position.
     * @param key The key of the object TreeSet.
     * @return object or null if the map[key] is not occupied.
     */
    WorldElement objectAt(Vector2d key);
    TreeSet<Animal> animalsAt(Vector2d key);
    Grass grassAt(Vector2d key);

    /**
     * @return a list of {@link Animal} of objects of map
     */
    List<Animal> getAnimals();
    List<Grass> getGrass();

    Boundary getCurrentBounds();

    void registerObserver(MapChangeListener listener);

    void killAnimal(Animal animal);

    void killGrass(Grass grass);

    void spawnNewGrasses(int n);

    List<WorldElement> getElements();

    int getMapArea();

    void statsChanged(MapStats stats);
}
