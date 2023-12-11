package agh.ics.oop.model;


import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.MapDirection;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;

public class Animal implements WorldElement {
    private MapDirection animalDirection;
    private Vector2d animalPosition;
    public Animal(Vector2d position) {
        animalPosition = position;
        animalDirection = MapDirection.NORTH;
    }
    public Animal() {
        this(new Vector2d(2, 2));
    }
    public MapDirection getDirection() {
        return animalDirection;
    }

    public Vector2d getPosition() {
        return animalPosition;
    }
    public String toString() {
        String[] dirs = {"N", "E", "S", "W"};
        return dirs[animalDirection.ordinal()];
    }
    public boolean isAt(Vector2d position) {
        return animalPosition.equals(position);
    }
    public boolean move(MoveDirection direction, MoveValidator moveValidator) {
        switch (direction) {
            case RIGHT -> animalDirection = animalDirection.next();
            case LEFT -> animalDirection = animalDirection.previous();
            case FORWARD, BACKWARD -> {
                Vector2d nextAnimalPosition = direction == MoveDirection.FORWARD ? animalPosition.add(animalDirection.toUnitVector()) : animalPosition.subtract(animalDirection.toUnitVector());
                if(moveValidator.canMoveTo(nextAnimalPosition)) {
                    animalPosition = nextAnimalPosition;
                    return true;
                }
            }
        }
        return false;
    }

}
