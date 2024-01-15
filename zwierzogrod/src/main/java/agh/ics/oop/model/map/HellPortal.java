package agh.ics.oop.model.map;
import agh.ics.oop.model.util.directions.Vector2d;

public class HellPortal extends AbstractWorldMap implements WorldMap {
    public HellPortal(int grass, int mapHeight, int mapWidth) {
        super(grass, mapHeight, mapWidth);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public Vector2d convertNextPosition(Vector2d position) {
        if (position.getX() >= mapWidth || position.getX() < 0 || position.getY() < 0 || position.getY() >= mapHeight)
            return Vector2d.randomVector(0, mapWidth - 1, 0, mapHeight - 1);
        else return position;
    }

    @Override
    public int getConversionCostMultiplier() {
        return 1;
    }
}
