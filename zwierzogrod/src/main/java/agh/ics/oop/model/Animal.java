package agh.ics.oop.model;
import agh.ics.oop.model.statistics.visitor.Visitable;
import agh.ics.oop.model.statistics.visitor.Visitor;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.directions.MapDirection;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Animal implements WorldElement, Visitable {
    private int reproductionUsedEnergy, MIN_MUTATIONS, MAX_MUTATIONS;
    private MapDirection animalDirection;
    private Vector2d animalPosition;
    private int animalEnergy;
    private final AnimalGenome animalGenome;
    private final int bornIn;
    private int diedIn = -1;
    private int timesEnergyAdded=0;
    private List<Animal> children = new ArrayList<>();
    private int uniqueID;
    private static int lastID = 0;

    public void setArgs(int minMutations, int maxMutations, int reproductionUsedEnergy) {
        this.reproductionUsedEnergy = reproductionUsedEnergy;
        this.MIN_MUTATIONS = minMutations;
        this.MAX_MUTATIONS = maxMutations;
    }
    public Animal(Vector2d position, int energy, AnimalGenome animalGenome, int bornIn) {
        uniqueID = lastID++;
        this.bornIn = bornIn;
        animalPosition = position;
        animalDirection = MapDirection.getRandomDirection();
        animalEnergy = energy;
        this.animalGenome = animalGenome;
    }
    public Animal(Vector2d position, int energy, int minMutations, int maxMutations, int reproductionUsedEnergy, int geonmeLength, Boolean mutationsEnabled, int bornIn) {
        this(position, energy, new AnimalGenome(geonmeLength, mutationsEnabled, minMutations, maxMutations), bornIn);
        this.reproductionUsedEnergy = reproductionUsedEnergy;
    }

    public void addEnergy( int energy ) {
        animalEnergy += energy;
        timesEnergyAdded++;
    }

    public void takeEnergy(int energy) {
        animalEnergy -= energy;
    }

    public int getUniqueId() {
        return uniqueID;
    }

    public void addChild(Animal animal) {
        children.add(animal);
    }
    public int getNoCildren() {
        return children.size();
    }

    public int getTimesEnergyAdded() {
        return timesEnergyAdded;
    }

    public int getBornIn() {
        return bornIn;
    }

    public int getDiedIn() {
        return diedIn;
    }

    public Boolean dead() { return diedIn != -1; }

    public void setDeathDay(int day) {
        diedIn = day;
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
    public Animal copulate(Animal other, int parentEnergyUsed, int birthDate) {
        Animal child = new Animal(this.getPosition(), parentEnergyUsed * 2, getGenome().newAnimalGenomeFromReproduction(this, other), birthDate);
        this.takeEnergy(parentEnergyUsed);
        this.addChild(child);
        other.takeEnergy(parentEnergyUsed);
        other.addChild(child);
        return child;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getDescendantNo() {
        HashSet<Animal> descendants = new HashSet<>();
        descendants.add(this);
        Queue<Animal> Q = new LinkedList<>();
        Q.offer(this);
        while(! Q.isEmpty()) {
            Animal currAnimal = Q.poll();
            for(Animal child : currAnimal.children)
                if(! descendants.contains(child)) {
                    descendants.add(currAnimal);
                    Q.offer(child);
                }
        }
        return  descendants.size() - 1;
    }
}
