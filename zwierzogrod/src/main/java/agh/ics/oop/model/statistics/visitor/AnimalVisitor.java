package agh.ics.oop.model.statistics.visitor;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalGenome;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.AnimalStats;

public class AnimalVisitor implements Visitor {
    private int energy, plantsEaten, children, descendants, bornIn, diedIn;
    private AnimalGenome animalGenome;

    public AnimalVisitor() {
        animalGenome = null;
    }

    @Override
    public void visit(Animal animal) {
        this.energy = animal.getEnergy();
        this.plantsEaten = animal.getTimesEnergyAdded();
        this.children = animal.getNumberOfChildren();
        this.descendants = animal.getNumberOfDescendants();
        this.bornIn = animal.getDayBornIn();
        this.diedIn = animal.getDayDiedIn();
        this.animalGenome = animal.getGenome();
    }

    @Override
    public void visit(WorldMap map) {}

    @Override
    public AnimalStats getStats() {
        return new AnimalStats(energy, plantsEaten, children, descendants, bornIn, diedIn, animalGenome);
    }
}
