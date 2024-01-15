package agh.ics.oop.model.util.directions;

import java.util.Random;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    private static final String[] DIRECTIONS = {
            "Północ", "Północny wschód", "Wschód", "Południowy wschód",
            "Południe", "Południowy zachód", "Zachód", "Północny Zachód"
    };

    private static final String[] ARROWS = {
            "\u2191", "\u2197", "\u2192", "\u2198", "\u2193", "\u2199", "\u2190", "\u2196"
    };

    private static final Random RANDOM = new Random();

    private static final int ARR_LEN = 8;

    @Override
    public String toString() {
        return DIRECTIONS[this.ordinal()];
    }

    public MapDirection next(int i) {
        return MapDirection.values()[(this.ordinal()+i+ARR_LEN)%ARR_LEN];
    }

    public MapDirection next() {
        return next(1);
    }

    public MapDirection previous() {
        return next(-1);
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
        return ARROWS[this.ordinal()];
    }

    public static MapDirection getRandomDirection() {
        return MapDirection.values()[RANDOM.nextInt(ARR_LEN)];
    }

    public MapDirection flip() {
        return MapDirection.values()[(this.ordinal() + ARR_LEN/2)%ARR_LEN];
    }
}
