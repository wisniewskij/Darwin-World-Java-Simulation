package agh.ics.oop.model.util.directions;

public enum MapDirection {
    NORTH, EAST, SOUTH, WEST;
    public String toString() {
        String[] directions = { "Północ", "Wschód", "Południe",  "Zachód" };
        return directions[this.ordinal()];
    }
    public MapDirection next() {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal()+1)%values.length];
    }
    public MapDirection previous() {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal()-1+values.length)%values.length];
    }
    public Vector2d toUnitVector() {
        Vector2d[] vectors = { new Vector2d(0, 1), new Vector2d(1, 0), new Vector2d(0, -1), new Vector2d(-1, 0) };
        return vectors[this.ordinal()];
    }
}
