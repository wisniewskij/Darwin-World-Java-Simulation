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
    public GrassField(int grass) {
        super();
        int boundary = (int) Math.sqrt(10 * grass);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(-boundary, boundary, -boundary, boundary, grass);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }
    public GrassField() {
        this(10);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement tmp = super.objectAt(position);
        if(tmp != null) return tmp;
        else return grasses.get(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d upper = null;
        Vector2d lower = null;

        amendCorners(lower, upper, super.animals);
        Vector2d[] tmp = amendCorners(lower, upper, grasses);
        lower = tmp[0];
        upper = tmp[1];
        tmp = amendCorners(lower, upper, super.animals);
        lower = tmp[0];
        upper = tmp[1];
        if(upper == null) upper = new Vector2d(0, 0);
        if(lower == null) lower = new Vector2d(0, 0);
        return new Boundary(lower, upper);
    }

    private Vector2d[] amendCorners(Vector2d lower, Vector2d upper, Map<Vector2d, WorldElement> map) {
        for(WorldElement elem: map.values()){
            Vector2d pos = elem.getPosition();
            if(upper == null) upper = new Vector2d(pos.getX(), pos.getY());
            if(lower == null) lower = new Vector2d(pos.getX(), pos.getY());
            upper = upper.upperRight(pos);
            lower = lower.lowerLeft(pos);
        }
        return new Vector2d[]{lower, upper};
    }

    public List<WorldElement> getElements() {
        List<WorldElement> lista = new ArrayList<>(super.getElements());
        lista.addAll(grasses.values());
        return lista;
    }
}
