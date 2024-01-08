package agh.ics.oop.model.util.directions;

import java.util.Objects;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d {
    private final int x, y;
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String toString() {
        return "(%d,%d)".formatted(x, y);
    }
    public boolean precedes(Vector2d other) {
        return x <= other.x && y <= other.y;
    }
    public boolean follows(Vector2d other) {
        return x >= other.x && y >= other.y;
    }
    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }
    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(max(x, other.x), max(y, other.y));
    }
    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(min(x, other.x), min(y, other.y));
    }
    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }
    public boolean equals(Object other){
        if(this == other)
            return true;
        if(!(other instanceof Vector2d that))
            return false;
        return that.x == this.x && that.y == this.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public static Vector2d randomVector(int lowX, int highX, int lowY, int highY) {
        Random random = new Random();
        return new Vector2d(random.nextInt((highX - lowX) + 1) + lowX, random.nextInt((highY - lowY) + 1) + lowY);
    }

}
