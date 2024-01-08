package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomGrassPositionGenerator;
import agh.ics.oop.model.util.directions.Vector2d;
import java.util.*;

abstract public class AbstractWorldMap implements WorldMap {
    protected final int mapHeight, mapWidth;
    public Map<Vector2d, Grass> grasses = new HashMap<>();
    public Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    public HashSet<MapChangeListener> observers = new HashSet<>();
    private final UUID mapId;
    private int updateCounter;

    public AbstractWorldMap(int grass, int mapHeight, int mapWidth) {
        mapId = UUID.randomUUID();
        updateCounter = 0;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        RandomGrassPositionGenerator randomGrassPositionGenerator = new RandomGrassPositionGenerator(mapHeight, mapWidth, grasses);

        int already_added = 0;
        for(Vector2d grassPosition : randomGrassPositionGenerator) {
            if (already_added++ >= grass) break;
            grasses.put(grassPosition, new Grass(grassPosition));
        }
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
            animals.put(position, new TreeSet<>(Comparator.comparingInt(Animal::getEnergy).reversed()));
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
        List<WorldElement> lista = new ArrayList<>(getAnimals());
        lista.addAll(getGrass());
        return lista;
    }
}
