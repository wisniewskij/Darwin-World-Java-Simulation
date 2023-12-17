package agh.ics.oop.model.util.directions;

import java.util.Random;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
    public String toString() {
        String[] directions = { "Północ", "Północny wschód" ,"Wschód", "Południowy wschód", "Południe", "Południowy zachód", "Zachód", "Północny Zachód" };

        return directions[this.ordinal()];
    }
    public MapDirection next(int i) {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal()+i)%values.length];
    }
    public MapDirection next() {
        return next(1);
    }
    public MapDirection previous() {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal()-1+values.length)%values.length];
    }
    public Vector2d toUnitVector() {
        Vector2d[] vectors = {
                new Vector2d(0, 1), new Vector2d(1, 1),
                new Vector2d(1, 0), new Vector2d(1, -1),
                new Vector2d(0, -1), new Vector2d(-1, -1),
                new Vector2d(-1, 0), new Vector2d(-1, 1),
        };
        return vectors[this.ordinal()];
    }

    public String toArrow() {
        String[] arrows = {"\u2191", "\u2197", "\u2192", "\u2198", "\u2193", "\u2199", "\u2190", "\u2196"};
        return arrows[this.ordinal()];
    }

    public static MapDirection getRandomDirection() {
        MapDirection[] values = MapDirection.values();
        Random random = new Random();
        int randomIndex = random.nextInt(values.length);

        return values[randomIndex];
    }

    public MapDirection flip() {
        MapDirection[] values = MapDirection.values();
        return values[(this.ordinal() + values.length/2)%values.length];
    }
}
