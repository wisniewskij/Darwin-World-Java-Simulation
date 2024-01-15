package agh.ics.oop.model.statistics;

import agh.ics.oop.model.AnimalGenome;

public class AnimalStats implements Stats {
    private final int activeGene, energy, plantsEaten, children, descendants, dayBornIn, dayDiedIn;
    private final AnimalGenome animalGenome;

    public AnimalStats(int energy, int plantsEaten, int children, int descendants, int dayBornIn, int dayDiedIn, AnimalGenome animalGenome) {
        this.energy = energy;
        this.plantsEaten = plantsEaten;
        this.children = children;
        this.descendants = descendants;
        this.dayBornIn = dayBornIn;
        this.dayDiedIn = dayDiedIn;
        this.animalGenome = animalGenome;
        this.activeGene = animalGenome.getCurrentGenIndex();
    }
    public int getActiveGene() {
        return activeGene;
    }
    public int getEnergy() {
        return energy;
    }
    public int getPlantsEaten() {
        return plantsEaten;
    }
    public int getChildren() {
        return children;
    }
    public int getDescendants() {
        return descendants;
    }
    public int getDayBornIn() {
        return dayBornIn;
    }
    public int getDayDiedIn() {
        return dayDiedIn;
    }
    public AnimalGenome getAnimalGenome() {
        return animalGenome;
    }
}
