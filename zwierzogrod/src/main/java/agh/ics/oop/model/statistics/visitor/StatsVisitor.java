package agh.ics.oop.model.statistics.visitor;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalGenome;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StatsVisitor implements Visitor{

    private int livingAnimalsCount, deadAnimalsCount, totalFields, plantCount;
    private final int currentDay;
    private double totalEnergy, totalLifespan, totalChildren;
    private final Map<AnimalGenome, Integer> genomeFrequencyMap;
    private final HashSet<Vector2d> visitedFields;

    public StatsVisitor(int day) {
        this.visitedFields = new HashSet<>();
        this.genomeFrequencyMap = new HashMap<>();
        this.deadAnimalsCount = 0;
        this.livingAnimalsCount = 0;
        this.totalEnergy = 0;
        this.totalLifespan = 0;
        this.totalChildren = 0;
        this.totalFields = 0;
        this.plantCount = 0;
        this.currentDay = day;
    }
    @Override
    public void visit(Animal animal) {
        if(animal.dead()) {
            deadAnimalsCount++;
            totalLifespan += animal.getDayDiedIn() - animal.getDayBornIn();
        } else {
            livingAnimalsCount++;
            totalChildren += animal.getNumberOfChildren();
            totalEnergy += animal.getEnergy();
            genomeFrequencyMap.merge(animal.getGenome(), 1, Integer::sum);
        }
    }

    @Override
    public void visit(WorldMap map) {
        plantCount += map.getGrass().size();
        visitedFields.addAll(map.getElements().stream().map(WorldElement::getPosition).toList());
        totalFields = map.getMapArea();
    }

    @Override
    public MapStats getStats() {
        return new MapStats(
                livingAnimalsCount,
                plantCount,
                totalFields - visitedFields.size(),
                livingAnimalsCount == 0 ? 0 : totalEnergy / livingAnimalsCount,
                deadAnimalsCount == 0 ? 0 : totalLifespan / deadAnimalsCount,
                livingAnimalsCount == 0 ? 0 : totalChildren / livingAnimalsCount,
                getDominatingGenome(),
                genomeFrequencyMap.getOrDefault(getDominatingGenome(), 0),
                currentDay
        );
    }

    private AnimalGenome getDominatingGenome() {
        return genomeFrequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
