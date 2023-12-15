package agh.ics.oop.model;


import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.MapDirection;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;

public class Animal implements WorldElement {
    private MapDirection animalDirection;
    private Vector2d animalPosition;

    private final AnimalGen animalGen;

    public Animal(Vector2d position) {
        animalPosition = position;
        animalDirection = MapDirection.NORTH;
        animalGen = new AnimalGen(50);
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
        String[] dirs = {"N", "NE", "E", "SE", "S", "SW", "W", "NW" };
        return dirs[animalDirection.ordinal()];
    }
    public boolean isAt(Vector2d position) {
        return animalPosition.equals(position);
    }
    public boolean move(MoveDirection direction, MoveValidator moveValidator) {
        animalDirection = animalGen.computeNextDirection(animalDirection);
        Vector2d nextAnimalPosition = animalPosition.add(animalDirection.toUnitVector());
        if(moveValidator.canMoveTo(nextAnimalPosition)) {
            animalPosition = nextAnimalPosition;
            return true;
        }
        return false;
    }

}
