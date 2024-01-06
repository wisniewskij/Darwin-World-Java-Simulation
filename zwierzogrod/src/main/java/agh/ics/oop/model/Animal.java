package agh.ics.oop.model;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.MapDirection;
import agh.ics.oop.model.util.directions.Vector2d;

import static java.lang.Math.min;

public class Animal implements WorldElement {
    private static int GENOME_LENGTH = 10;
    private static int MAX_ENERGY = 10;
    private MapDirection animalDirection;
    private Vector2d animalPosition;
    private int animalEnergy;
    private final AnimalGenome animalGenome;
    public Animal(Vector2d position, int energy, AnimalGenome animalGenome) {
        animalPosition = position;
        animalDirection = MapDirection.getRandomDirection();
        animalEnergy = energy;
        this.animalGenome = animalGenome;
    }
    public Animal() {
        this(new Vector2d(2, 2), 10, new AnimalGenome(GENOME_LENGTH));
    }

    public void addEnergy( int energy ) {
        animalEnergy = min(MAX_ENERGY, animalEnergy + energy);
    }

    public MapDirection getDirection() {
        return animalDirection;
    }

    public Vector2d getPosition() {
        return animalPosition;
    }

    public AnimalGenome getGenome() {
        return animalGenome;
    }

    public int getEnergy() {
        return animalEnergy;
    }

    public String toString() {
        return "%s %d".formatted(animalDirection.toArrow(), animalEnergy);
    }

    public boolean isAt(Vector2d position) {
        return animalPosition.equals(position);
    }
    public boolean move(MoveValidator moveValidator) {
        animalEnergy--;
        animalDirection = animalGenome.nextDirection(animalDirection);
        Vector2d nextAnimalPosition = animalPosition.add(animalDirection.toUnitVector());
        if(moveValidator.canMoveTo(nextAnimalPosition)) {
            animalPosition = moveValidator.convertNextPosition(nextAnimalPosition);
            return true;
        } else {
            animalDirection = animalDirection.flip();
        }
        return false;
    }
}
