package agh.ics.oop.model.map;


import agh.ics.oop.model.util.directions.Vector2d;

public class Earth extends AbstractWorldMap implements WorldMap {

    public Earth(int grass, int mapHeight, int mapWidth) {
        super(grass, mapHeight, mapWidth);
    }

    public Earth() {
        super();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.getY() >= 0 && position.getY() < mapHeight);
    }

    @Override
    public Vector2d convertNextPosition(Vector2d position) {
        return new Vector2d((position.getX() + mapWidth) % mapWidth, position.getY());
    }

    @Override
    public int getConversionCostMultiplier() {
        return 0;
    }
}
