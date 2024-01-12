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

abstract public class AbstractWorldMap implements WorldMap, Visitable {
    protected final int mapHeight, mapWidth;
    public Map<Vector2d, Grass> grasses = new HashMap<>();
    public Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    public HashSet<MapChangeListener> observers = new HashSet<>();
    private final UUID mapId;
    private int updateCounter;

    public AbstractWorldMap(int initialGrassesNumber, int mapHeight, int mapWidth) {
        mapId = UUID.randomUUID();
        updateCounter = 0;
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
        for (MapChangeListener listener : observers)
            listener.statsChanged(stats);
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

    public TreeSet<Animal> animalsAt(Vector2d position) {
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

    void placeAnimalAt(Animal animal, Vector2d position) {
        if (animals.containsKey(position)) {
            animals.get(position).add(animal);
        } else {
            animals.put(position, new TreeSet<>(Comparator.comparingInt(Animal::getEnergy).reversed().thenComparing(Animal::getBornIn).thenComparing(Animal::getNoCildren).thenComparing(Animal::getUniqueId)));
            animals.get(position).add(animal);
        }
    }

    void takeAnimalFrom(Animal animal, Vector2d oldPosition) {
        if (animals.containsKey(oldPosition)) {
            animals.get(oldPosition).remove(animal);
            if (animals.get(oldPosition).isEmpty())
                animals.remove(oldPosition);
        } else mapChanged(oldPosition.toString());
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
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

    public void killAnimal(Animal animal) {
        mapChanged("died at %s".formatted(animal.getPosition()));
        takeAnimalFrom(animal, animal.getPosition());
    }

    public void killGrass(Grass grass) {
        mapChanged("eaten at %s".formatted(grass.getPosition()));
        grasses.remove(grass.getPosition());
    }

    @Override
    public List<Animal> getAnimals() {
        ArrayList<Animal> allAnimals = new ArrayList<>();
        for (TreeSet<Animal> animalSet : animals.values()) {
            allAnimals.addAll(animalSet);
        }
        return allAnimals;
    }

    @Override
    public WorldElement objectAt(Vector2d key) {
        TreeSet<Animal> animalTreeSet = animalsAt(key);
        if(animalTreeSet != null && !animalTreeSet.isEmpty()) return animalTreeSet.first();
        return grassAt(key);
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
        Vector2d upper = new Vector2d(mapWidth - 1, mapHeight - 1);
        Vector2d lower = new Vector2d(0, 0);
        return new Boundary(lower, upper);
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
