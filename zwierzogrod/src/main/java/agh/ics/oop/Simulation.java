package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.util.exceptions.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    final static int SIMULATION_INTERVAL = 300;
    List<Animal> animals;
    WorldMap map;
    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.map = map;
        animals = new ArrayList<>(0);
        for(Vector2d pos: positions) {
            Animal animal = new Animal();
//            try {
            map.placeNewAnimal(animal);
            animals.add(animal);
            map.mapChanged("Animal added on position: " + positions);
//            } catch (PositionAlreadyOccupiedException e) {
//                System.out.println(e.getMessage());
//            }
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

