package agh.ics.oop.model.map;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.Vector2d;

public class RectangularMap extends AbstractWorldMap implements WorldMap {
    static final int minX = 0, minY = 0;
    final int mapHeight, mapWidth;
    public RectangularMap (int width, int height) {
        super();
        mapHeight = height;
        mapWidth = width;
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(new Vector2d(minX, minY), new Vector2d(mapWidth + minX, mapHeight + minY));
    }

    public boolean canMoveTo(Vector2d position) {
        return position.getX() <= mapWidth - minX && position.getX() >= minX &&
                position.getY() <= mapHeight - minY && position.getY() >= minY &&
                super.canMoveTo(position);
    }

}
