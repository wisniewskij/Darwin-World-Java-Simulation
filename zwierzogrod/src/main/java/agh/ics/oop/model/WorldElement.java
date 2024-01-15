package agh.ics.oop.model;

import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.Vector2d;

public interface WorldElement {
    Vector2d getPosition();
    String toString();
    boolean move(MoveValidator validator);
}
