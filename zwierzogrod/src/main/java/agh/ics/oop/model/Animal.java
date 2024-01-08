package agh.ics.oop.model;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.MapDirection;
import agh.ics.oop.model.util.directions.Vector2d;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Animal implements WorldElement {
    private int MAX_ENERGY = 1000000, reproductionUsedEnergy, MIN_MUTATIONS, MAX_MUTATIONS;
    private MapDirection animalDirection;
    private Vector2d animalPosition;
    private int animalEnergy;
    private final AnimalGenome animalGenome;
    public void setArgs(int minMutations, int maxMutations, int reproductionUsedEnergy) {
        reproductionUsedEnergy = reproductionUsedEnergy;
        MIN_MUTATIONS = minMutations;
        MAX_MUTATIONS = maxMutations;
    }
    public Animal(Vector2d position, int energy, AnimalGenome animalGenome) {
        animalPosition = position;
        animalDirection = MapDirection.getRandomDirection();
        animalEnergy = energy;
        this.animalGenome = animalGenome;
    }
    public Animal(Vector2d position, int energy, int minMutations, int maxMutations, int reproductionUsedEnergy, int geonmeLength, Boolean mutationsEnabled) {
        this(position, energy, new AnimalGenome(geonmeLength, mutationsEnabled, minMutations, maxMutations));
        this.reproductionUsedEnergy = reproductionUsedEnergy;
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
            Vector2d newPosition = moveValidator.convertNextPosition(nextAnimalPosition);
            if(! nextAnimalPosition.equals(newPosition)) {
                animalEnergy = max(0, animalEnergy - reproductionUsedEnergy * moveValidator.getConversionCostMultiplier());
            }
            animalPosition = newPosition;
            return true;
        } else {
            animalDirection = animalDirection.flip();
        }
        return false;
    }
}
