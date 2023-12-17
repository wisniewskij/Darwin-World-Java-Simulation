package agh.ics.oop.model;

import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.Vector2d;

public class Grass implements WorldElement {
    Vector2d position;
    public Grass(Vector2d position) {
        this.position = position;
    }
    public Grass() {
        this(new Vector2d(0, 0));
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }
    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean move(MoveValidator validator) {
        return false;
    }
}
