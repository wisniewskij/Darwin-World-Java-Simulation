package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalGenome;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.Arrays;

public class World {
//    tested genoms generation
    public static void main(String[] args) {
        AnimalGenome animalGenome1 = new AnimalGenome(new int[]{1,1,1,1});
        AnimalGenome animalGenome2 = new AnimalGenome(new int[]{2,2,2,2});
        Animal animal1 = new Animal(new Vector2d(2, 2), 24, animalGenome1);
        Animal animal2 = new Animal(new Vector2d(2, 2), 76, animalGenome2);

        System.out.println(Arrays.toString(AnimalGenome.newAnimalGenomeFromReproduction(animal1, animal2, 4, 0, 1).genome));
    }
}
