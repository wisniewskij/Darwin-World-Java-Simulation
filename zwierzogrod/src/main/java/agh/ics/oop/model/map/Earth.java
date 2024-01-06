package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomGrassPositionGenerator;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;

public class Earth extends AbstractWorldMap implements WorldMap {

    private final int mapHeight, mapWidth;

    Map<Vector2d, Grass> grasses = new HashMap<>();
    public Earth(int grass, int mapHeight, int mapWidth) {
        super();
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        RandomGrassPositionGenerator randomGrassPositionGenerator = new RandomGrassPositionGenerator(mapHeight, mapWidth, grasses);

        int already_added = 0;
        for(Vector2d grassPosition : randomGrassPositionGenerator) {
            if (already_added++ >= grass) break;
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }
    public Earth() {
        this(10, 10, 10);
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

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.getY() >= 0 && position.getY() < mapHeight);
    }

    @Override
    public Vector2d convertNextPosition(Vector2d position) {
        return new Vector2d((position.getX() + mapWidth) % mapWidth, position.getY());
    }
}
