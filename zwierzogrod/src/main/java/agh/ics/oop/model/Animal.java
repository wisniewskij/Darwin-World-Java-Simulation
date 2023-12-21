package agh.ics.oop.model;
import agh.ics.oop.model.util.Boundary;
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
    public boolean move(MoveValidator moveValidator, Boundary currentBoundary) {
        animalEnergy--;
        animalDirection = animalGenome.nextDirection(animalDirection);
        Vector2d nextAnimalPosition = animalPosition.add(animalDirection.toUnitVector());

        /*
        if(nextAnimalPosition.getX() > currentBoundary.rightUpper().getX()) {
            nextAnimalPosition = animalPosition.moveToLeftBoundary(currentBoundary, nextAnimalPosition);
        } else if (nextAnimalPosition.getX() < currentBoundary.leftLower().getX()) {
            nextAnimalPosition = animalPosition.moveToRightBoundary(currentBoundary, nextAnimalPosition);
        }

        if (nextAnimalPosition.getY() > currentBoundary.rightUpper().getY()){
            nextAnimalPosition = animalPosition.changeYDirectionToSouth();
        } else if (nextAnimalPosition.getY() < currentBoundary.leftLower().getY()) {
            nextAnimalPosition = animalPosition.changeYDirectionToNorth();
        }
        */

        if(nextAnimalPosition.getX() > currentBoundary.rightUpper().getX() ||
            nextAnimalPosition.getX() < currentBoundary.leftLower().getX() ||
            nextAnimalPosition.getY() < currentBoundary.leftLower().getY() ||
            nextAnimalPosition.getY() > currentBoundary.rightUpper().getY()) {
            nextAnimalPosition = animalPosition.random(currentBoundary);
            animalEnergy = energyMinusHalf();
        } else {
            animalEnergy --;
        }

        if(moveValidator.canMoveTo(nextAnimalPosition)) {
            animalPosition = nextAnimalPosition;
            return true;
        } else {
            animalDirection = animalDirection.flip();
        }
        return false;
    }

    private int energyMinusHalf() {
        return animalEnergy - animalEnergy/2;
    }
}
