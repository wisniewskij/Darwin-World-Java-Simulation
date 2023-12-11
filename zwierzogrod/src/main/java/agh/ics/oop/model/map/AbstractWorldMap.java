package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.util.exceptions.PositionAlreadyOccupiedException;

import java.util.*;

abstract public class AbstractWorldMap implements WorldMap {
    public Map<Vector2d, WorldElement> animals = new HashMap<>();
    private final UUID mapId;
    public HashSet<MapChangeListener> observers = new HashSet<>();

    private int updateCounter;

    protected AbstractWorldMap() {
        mapId = UUID.randomUUID();
        updateCounter = 0;
    }

    public int getUpdateCounter() {
        return updateCounter;
    }
    public void incrementUpdateCounter() {
        updateCounter++;
    }

    @Override
    public UUID getId() {
        return mapId;
    }
    public void registerObserver(MapChangeListener listener) {
        observers.add(listener);
    }
    public void unregisterObserver(MapChangeListener listener) {
        observers.remove(listener);
    }
    void mapChanged(String message) {
        for (MapChangeListener listener : observers)
                listener.mapChanged(this, message);
    }
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }
    public abstract Boundary getCurrentBounds();
    @Override
    public String toString() {
        Boundary bounds =  getCurrentBounds();
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(bounds.leftLower(), bounds.rightUpper());
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public void move(WorldElement animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        if (animal.move(direction, this)) {
            animals.remove(oldPosition);
            animals.put(animal.getPosition(), animal);
            mapChanged("%s -> %s".formatted(oldPosition, animal.getPosition()));
        }
    }
    public void place(WorldElement animal) throws PositionAlreadyOccupiedException {
        Vector2d animalPos = animal.getPosition();
        if(! canMoveTo(animalPos)) throw new PositionAlreadyOccupiedException(animalPos);
        animals.put(animal.getPosition(), animal);
        mapChanged("new at %s".formatted(animalPos));
    }
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) || objectAt(position) instanceof Grass;
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }
}
