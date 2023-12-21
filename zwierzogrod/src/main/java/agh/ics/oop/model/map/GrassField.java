package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrassField extends AbstractWorldMap implements WorldMap {

    Map<Vector2d, WorldElement> grasses = new HashMap<>();

    Vector2d upper = new Vector2d(10,10);

    Vector2d lower = new Vector2d(-10, -10);
    public GrassField() {
        super();
    }

    public void setGrasses(int grass) {
        int minX = lower.getX();
        int minY = lower.getY();
        int maxX = upper.getX();
        int maxY = upper.getY();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(minX,maxX, minY, maxY, grass);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement tmp = super.objectAt(position);
        if(tmp != null) return tmp;
        else return grasses.get(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lower, upper);
    }

    public List<WorldElement> getElements() {
        List<WorldElement> lista = new ArrayList<>(super.getElements());
        lista.addAll(grasses.values());
        return lista;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }
}
