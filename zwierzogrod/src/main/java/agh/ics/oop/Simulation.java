package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    final static int SIMULATION_INTERVAL = 500;
    List<Animal> animals;
    WorldMap map;
    public Simulation(WorldMap map, int minMutations, int maxMutations, int plantEnergyRegain, int initialAnimalNumber, int initialPlantNumber, int plantGrowNumber, int reproductionReadyEnergy, int reproductionUsedEnergy, int geonmeLength, int baseEnergy, Boolean moveMutationsEnabled) {
        this.map = map;
        animals = new ArrayList<>(0);
        for(int i = 0; i < initialAnimalNumber; i++) {
            Vector2d positions = Vector2d.randomVector(0, map.getCurrentBounds().rightUpper().getX()-1, 0, map.getCurrentBounds().rightUpper().getY()-1);
            Animal animal = new Animal(positions, baseEnergy, minMutations, maxMutations, reproductionUsedEnergy, geonmeLength, moveMutationsEnabled);
            map.placeNewAnimal(animal);
            animals.add(animal);
            map.mapChanged("Animal added on position: " + positions);
        }
    }
    @Override
    public void run() {
        for(int i= 0; i<100; i++) { // debug
            try {
                Thread.sleep(SIMULATION_INTERVAL);
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
            for(Animal animal : animals) {
                map.move(animal);
            }
        }
    }
}

