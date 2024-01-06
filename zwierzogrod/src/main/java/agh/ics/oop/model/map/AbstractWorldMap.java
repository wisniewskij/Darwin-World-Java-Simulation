package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.util.exceptions.PositionAlreadyOccupiedException;

import java.util.*;

abstract public class AbstractWorldMap implements WorldMap {
    public Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
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
    public void mapChanged(String message) {
        for (MapChangeListener listener : observers)
                listener.mapChanged(this, message);
    }
    public TreeSet<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }
    public abstract Boundary getCurrentBounds();
    @Override
    public String toString() {
        Boundary bounds =  getCurrentBounds();
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(bounds.leftLower(), bounds.rightUpper());
    }

    void placeAnimalAt(Animal animal, Vector2d position) {
        if (animals.containsKey(position)) {
            animals.get(position).add(animal);
        } else {
            animals.put(position, new TreeSet<>(Comparator.comparingInt(Animal::getEnergy).reversed()));
            animals.get(position).add(animal);
        }
    }
    void takeAnimalFrom(Animal animal, Vector2d oldPosition) {
        animals.get(oldPosition).remove(animal);
        if (animals.get(oldPosition).isEmpty())
            animals.remove(oldPosition);
    }

    public boolean isOccupied(Vector2d position) {
        return animalsAt(position) != null;
    }

    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        if (animal.move(this)) {
            takeAnimalFrom(animal, oldPosition);
            placeAnimalAt(animal, animal.getPosition());
            mapChanged("%s -> %s".formatted(oldPosition, animal.getPosition()));
        }
    }
    public void placeNewAnimal(Animal animal) {
        placeAnimalAt(animal, animal.getPosition());
        mapChanged("new at %s".formatted(animal.getPosition()));
    }

    @Override
    public List<Animal> getAnimals() {
        ArrayList<Animal> allAnimals = new ArrayList<>();
        for (TreeSet<Animal> animalSet : animals.values()) {
            allAnimals.addAll(animalSet);
        }
        return allAnimals;
    }
}
