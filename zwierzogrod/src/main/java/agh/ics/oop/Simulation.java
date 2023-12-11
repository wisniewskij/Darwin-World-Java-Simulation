package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.util.exceptions.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    final static int SIMULATION_INTERVAL = 300;
    List<Animal> animals;
    WorldMap map;
    List<MoveDirection> directions;
    public Simulation(List<MoveDirection> directions, List<Vector2d> positions, WorldMap map) {
        this.map = map;
        animals = new ArrayList<>(0);
        for(Vector2d pos: positions) {
            Animal animal = new Animal(pos);
            try {
                map.place(animal);
                animals.add(animal);
            } catch (PositionAlreadyOccupiedException e) {
                System.out.println(e.getMessage());
            }
        }
        this.directions = directions;
    }
    @Override
    public void run() {
        for(int i=0; i<directions.size(); i++) {
            int animalIndex = i % animals.size(), moveIndex = i % directions.size();
            Animal animal = animals.get(animalIndex);
            map.move(animal, directions.get(moveIndex));
            try {
                Thread.sleep(SIMULATION_INTERVAL);
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
//            System.out.println("Zwierze %d : %s %s".formatted(animalIndex, animals.get(animalIndex).getPosition(), animals.get(animalIndex).toString().split(" ")[0]));
        }
    }
}
