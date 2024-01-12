package agh.ics.oop.model;

import agh.ics.oop.model.util.directions.MapDirection;

import java.util.Arrays;
import java.util.Random;


public class AnimalGenome {
    private static final int MAX_GEN_VALUE = 7;
    private static final int MIN_GEN_VALUE = 0;
    private boolean mutationsinMovesEnabled = true;
    private int minMutations, maxMutations;

    public final int[] genome;

    private int currentGenIndex;
    private final Random random = new Random();

    public AnimalGenome(int[] genome, Boolean mutationsinMovesEnabled, int minMutations, int maxMutations) {
        this.mutationsinMovesEnabled = mutationsinMovesEnabled;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genome = genome;
        currentGenIndex = random.nextInt(genome.length);
    }
    public AnimalGenome(int numberOfGenes, Boolean mutationsinMovesEnabled, int minMutations, int maxMutations) {
        this.mutationsinMovesEnabled = mutationsinMovesEnabled;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genome = createRandomGenome(numberOfGenes);
        currentGenIndex = random.nextInt(numberOfGenes);
    }
    public int[] createRandomGenome(int numberOfGenes) {
        int[] genome = new int[numberOfGenes];
        for(int i=0; i < numberOfGenes; i++)
            genome[i] = random.nextInt(MAX_GEN_VALUE - MIN_GEN_VALUE + 1) + MIN_GEN_VALUE;
        return genome;
    }

    public MapDirection nextDirection(MapDirection currentDirection) {
        if (mutationsinMovesEnabled && random.nextInt(5) == 0) {
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
            cutPoint = genome.length * animal2.getEnergy()/(animal1.getEnergy() + animal2.getEnergy());
        } else {
            strongerGenome = animal2.getGenome();
            weakerGenome = animal1.getGenome();
            cutPoint = genome.length * animal1.getEnergy()/(animal1.getEnergy() + animal2.getEnergy());
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

        return new AnimalGenome(newGenomeArray, mutationsinMovesEnabled, minMutations, maxMutations);
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