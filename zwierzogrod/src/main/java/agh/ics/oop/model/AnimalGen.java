package agh.ics.oop.model;

import agh.ics.oop.model.util.directions.MapDirection;

import java.util.Random;

public class AnimalGen {
    private static final int MIN_GEN_VALUE = 0;

    private static final int MAX_GEN_VALUE = 7;

    private final int[] gen;

    private int actualGenIndex = 0;


    public AnimalGen(int numberOfGenes) {
        this.gen = initGen(numberOfGenes);
    }

    private int[] initGen(int numberOfGenes){
        int[] gen = new int[numberOfGenes];
        for(int i=0; i < numberOfGenes; i++){
            Random random = new Random();
            gen[i] = random.nextInt(MIN_GEN_VALUE, MAX_GEN_VALUE +1);
        }
        return gen;
    }

    public MapDirection computeNextDirection(MapDirection actualDirection){
        if(actualGenIndex == gen.length) {
            actualGenIndex = 0;
        }
        int actualGen = this.gen[actualGenIndex];
        MapDirection result = actualDirection;
        for(int i = 0; i < actualGen; i++){
            result = result.next();
        }
        actualGenIndex++;
        return result;
    }
}
