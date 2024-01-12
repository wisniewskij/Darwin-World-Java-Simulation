package agh.ics.oop.model.statistics.visitor;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalGenome;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

public class StatsVisitor implements Visitor{

    private int livingAnimals, deadAnimals, allFields, plants;
    private double energySum, lifespanSum, childrenSum;
    Map<AnimalGenome, Integer> genomesFrequencyMap;
    HashSet<Vector2d> checkedFields;

    public StatsVisitor() {
        checkedFields = new HashSet<>();
        genomesFrequencyMap = new HashMap<>();
        deadAnimals = 0;
        livingAnimals = 0;
        energySum = 0;
        lifespanSum = 0;
        childrenSum = 0;
        allFields = 0;
        plants = 0;
    }
    @Override
    public void visit(Animal animal) {
        if(animal.dead()) {
            deadAnimals++;
            lifespanSum += animal.getDiedIn() - animal.getBornIn();
        } else {
            livingAnimals++;
            childrenSum += animal.getNoCildren();
            energySum += animal.getEnergy();
            genomesFrequencyMap.put(animal.getGenome(), genomesFrequencyMap.getOrDefault(animal.getGenome(), 0) + 1);
        }
    }

    @Override
    public void visit(WorldMap map) {
        plants += map.getGrass().size();
        checkedFields.addAll(map.getElements().stream().map(WorldElement::getPosition).toList());
        allFields = map.getMapArea();
    }

    @Override
    public MapStats getStats() {
        AnimalGenome dominatingGenome = null;
        int maxFrequency = 0;
        for (Map.Entry<AnimalGenome, Integer> entry : genomesFrequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                dominatingGenome = entry.getKey();
            }
        }

        return new MapStats(livingAnimals, plants,
                allFields - checkedFields.size(),
                livingAnimals == 0 ? 0 : energySum / livingAnimals, deadAnimals == 0 ? 0 : lifespanSum / deadAnimals,
                livingAnimals == 0 ? 0 : childrenSum / livingAnimals, dominatingGenome, maxFrequency);
    }
}
