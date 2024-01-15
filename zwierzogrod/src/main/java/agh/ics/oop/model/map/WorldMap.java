package agh.ics.oop.model.map;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.statistics.visitor.Visitable;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.WorldElement;

import java.util.*;

/**
 * The interface responsible for interacting with the map of the world.
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator, Visitable {
    void mapChanged(String message);

    void incrementUpdateCounter();

    int getUpdateCounter();

    UUID getId();

    void placeNewAnimal(Animal object);

    void move(Animal object);

    boolean isOccupied(Vector2d key);

    WorldElement objectAt(Vector2d key);

    HashSet<Animal> animalsAt(Vector2d key);

    Grass grassAt(Vector2d key);

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
