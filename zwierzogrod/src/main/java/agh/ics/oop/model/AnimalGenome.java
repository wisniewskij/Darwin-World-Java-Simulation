package agh.ics.oop.model;

import agh.ics.oop.model.util.directions.MapDirection;

import java.util.Arrays;
import java.util.Random;


public class AnimalGenome {
    private static final int MAX_GEN_VALUE = 7;
    private static final int MIN_GEN_VALUE = 0;
    private final boolean mutationsInMovesEnabled;
    private final int minMutations, maxMutations;
    public final int[] genome;
    private int currentGenIndex;
    private final Random random = new Random();

    public AnimalGenome(int[] genome, Boolean mutationsInMovesEnabled, int minMutations, int maxMutations) {
        this.mutationsInMovesEnabled = mutationsInMovesEnabled;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genome = genome;
        this.currentGenIndex = random.nextInt(genome.length);
    }
    public AnimalGenome(int numberOfGenes, Boolean mutationsInMovesEnabled, int minMutations, int maxMutations) {
        this(createRandomGenome(numberOfGenes), mutationsInMovesEnabled, minMutations, maxMutations);
    }
    public static int[] createRandomGenome(int numberOfGenes) {
        return new Random().ints(numberOfGenes, MIN_GEN_VALUE, MAX_GEN_VALUE + 1).toArray();
    }

    public int getCurrentGenIndex() {
        return currentGenIndex;
    }

    public MapDirection nextDirection(MapDirection currentDirection) {
        if (mutationsInMovesEnabled && random.nextInt(5) == 0) {
            currentGenIndex = random.nextInt(genome.length);
        } else {
            currentGenIndex = (currentGenIndex + 1) % genome.length;
        }
        return currentDirection.next(genome[currentGenIndex]);
    }

    @Override
    public String toString() {
        return Arrays.toString(genome).replaceAll(",", " ");
    }

    public AnimalGenome newAnimalGenomeFromReproduction(Animal animal1, Animal animal2) {

        int[] newGenomeArray = new int[genome.length];
        AnimalGenome strongerGenome, weakerGenome;
        int cutPoint;

        if (animal1.getEnergy() > animal2.getEnergy()) {
            strongerGenome = animal1.getGenome();
            weakerGenome = animal2.getGenome();
            cutPoint = calculateCutPoint(animal1, animal2);
        } else {
            strongerGenome = animal2.getGenome();
            weakerGenome = animal1.getGenome();
            cutPoint = calculateCutPoint(animal2, animal1);
        }

        Random random = new Random();
        int randomParam = random.nextInt(2);

        for (int i = 0; i < genome.length; i++) {
            newGenomeArray[i] = strongerGenome.genome[i];
            if (randomParam == 0 && i < cutPoint)
                newGenomeArray[i] = weakerGenome.genome[i];
            else if (randomParam == 1 && i >= genome.length - cutPoint)
                newGenomeArray[i] = weakerGenome.genome[i];
        }

        if (minMutations >= 0 && maxMutations > minMutations) {
            for (int i = 0; i < random.nextInt(maxMutations - minMutations + 1) + minMutations; i++) {
                newGenomeArray[random.nextInt(genome.length)] = random.nextInt(MAX_GEN_VALUE);
            }
        }

        return new AnimalGenome(newGenomeArray, mutationsInMovesEnabled, minMutations, maxMutations);
    }

    private int calculateCutPoint(Animal stronger, Animal weaker) {
        return genome.length * weaker.getEnergy() / (stronger.getEnergy() + weaker.getEnergy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalGenome that = (AnimalGenome) o;

        return Arrays.equals(genome, that.genome);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genome);
    }
}