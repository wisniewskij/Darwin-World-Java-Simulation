package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.statistics.visitor.Visitable;
import agh.ics.oop.model.statistics.visitor.Visitor;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomGrassPositionGenerator;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

abstract public class AbstractWorldMap implements WorldMap, Visitable {
    protected final int mapHeight, mapWidth;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    private final Set<MapChangeListener> observers = new HashSet<>();
    private final UUID mapId;
    private int updateCounter;

    public AbstractWorldMap(int initialGrassesNumber, int mapHeight, int mapWidth) {
        this.mapId = UUID.randomUUID();
        this.updateCounter = 0;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        spawnNewGrasses(initialGrassesNumber);
    }

    public AbstractWorldMap() {
        this(10, 10, 10);
    }

    public int getUpdateCounter() {
        return updateCounter;
    }

    public void incrementUpdateCounter() {
        updateCounter++;
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

    public void statsChanged(MapStats stats) {
        observers.forEach(listener -> listener.statsChanged(stats));
    }

    public void spawnNewGrasses(int n) {
        RandomGrassPositionGenerator randomGrassPositionGenerator = new RandomGrassPositionGenerator(mapHeight, mapWidth, grasses);
        for(Vector2d grassPosition : randomGrassPositionGenerator) {
            if (n-- <= 0) break;
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public int getMapArea() {
        return mapWidth * mapHeight;
    }

    public HashSet<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public String toString() {
        Boundary bounds =  getCurrentBounds();
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(bounds.leftLower(), bounds.rightUpper());
    }

    @Override
    public UUID getId() {
        return mapId;
    }

    private void placeAnimalAt(Animal animal, Vector2d position) {
        animals.computeIfAbsent(position, k -> new HashSet<>()).add(animal);
    }

    private void takeAnimalFrom(Animal animal, Vector2d oldPosition) {
        animals.computeIfPresent(oldPosition, (pos, animalSet) -> {
            animalSet.remove(animal);
            return animalSet.isEmpty() ? null : animalSet;
        });
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        if (animal.move(this)) {
            takeAnimalFrom(animal, oldPosition);
            placeAnimalAt(animal, animal.getPosition());
        }
        mapChanged("%s -> %s".formatted(oldPosition, animal.getPosition()));
    }

    public void placeNewAnimal(Animal animal) {
        placeAnimalAt(animal, animal.getPosition());
        mapChanged("new at %s".formatted(animal.getPosition()));
    }

    public void killAnimal(Animal animal) {
        takeAnimalFrom(animal, animal.getPosition());
        mapChanged("died at %s".formatted(animal.getPosition()));
    }

    public void killGrass(Grass grass) {
        grasses.remove(grass.getPosition());
        mapChanged("eaten at %s".formatted(grass.getPosition()));
    }

    @Override
    public List<Animal> getAnimals() {
        return animals.values().stream()
                .flatMap(Set::stream)
                .toList();
    }

    public WorldElement objectAt(Vector2d position) {
        WorldElement val = Optional.ofNullable(animalsAt(position))
                .filter(not(Set::isEmpty))
                .map(Set::stream)
                .flatMap(Stream::findFirst)
                .orElse(null);
        return val == null ? grassAt(position) : val;
    }

    @Override
    public Grass grassAt(Vector2d position) {
        return grasses.get(position);
    }

    @Override
    public List<Grass> getGrass() {
        return new ArrayList<>(grasses.values());
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));
    }

    public List<WorldElement> getElements() {
        List<WorldElement> list = new ArrayList<>(getAnimals());
        list.addAll(getGrass());
        return list;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
