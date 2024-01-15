package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.statistics.visitor.StatsVisitor;
import agh.ics.oop.model.statistics.visitor.Visitor;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Simulation implements Runnable {
    private final int plantEnergyRegain, plantGrowthNumber, reproductionReadyEnergy, reproductionUsedEnergy, simulationInterval;
    private final List<Animal> animals = new LinkedList<>();
    private final List<Animal> deadAnimals = new LinkedList<>();
    private final WorldMap map;
    private final Random random = new Random();
    private final Lock lock = new ReentrantLock();
    private final Condition pauseCondition = lock.newCondition();
    private boolean paused = false;
    private final boolean statsLoggingEnabled;

    public Simulation(WorldMap map, int minMutations, int maxMutations, int plantEnergyRegain, int initialAnimalNumber, int plantGrowthNumber, int reproductionReadyEnergy, int reproductionUsedEnergy, int genomeLength, int baseEnergy, boolean moveMutationsEnabled, int simulationInterval, Boolean statsLoggingEnabled) {
        this.map = map;
        this.simulationInterval = simulationInterval;
        this.plantGrowthNumber = plantGrowthNumber;
        this.plantEnergyRegain = plantEnergyRegain;
        this.reproductionUsedEnergy = reproductionUsedEnergy;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.statsLoggingEnabled = statsLoggingEnabled;

        initializeAnimals(initialAnimalNumber, minMutations, maxMutations, baseEnergy, genomeLength, moveMutationsEnabled);

        if(statsLoggingEnabled) MapStats.writeHeaderToCSV("log_files/%s.csv".formatted(map.getId()));
    }

    @Override
    public void run() {
        for (int i = 0; !animals.isEmpty(); i++) {
            lock.lock();
            try {
                while (paused) {
                    pauseCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                lock.unlock();
            }

            sleepSimulationInterval();
            processSimulationStep(i);
        }
    }

    public void togglePause() {
        lock.lock();
        try {
            paused = !paused;
            if (!paused) {
                pauseCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public Boolean isPaused() {
        return paused;
    }

    private void initializeAnimals(int initialAnimalNumber, int minMutations, int maxMutations, int baseEnergy, int genomeLength, boolean moveMutationsEnabled) {
        for (int i = 0; i < initialAnimalNumber; i++) {
            Vector2d position = Vector2d.randomVector(0, map.getCurrentBounds().rightUpper().getX(), 0, map.getCurrentBounds().rightUpper().getY());
            Animal animal = new Animal(position, baseEnergy, minMutations, maxMutations, reproductionUsedEnergy, genomeLength, moveMutationsEnabled, 0);
            map.placeNewAnimal(animal);
            animals.add(animal);
            map.mapChanged("Animal added on position: " + position);
        }
    }

    private void sleepSimulationInterval() {
        try {
            Thread.sleep(simulationInterval);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Simulation interrupted", e);
        }
    }

    private void processSimulationStep(int day) {
        removeDeadAnimals(day);
        moveAnimals();
        processGrassEating();
        processReproduction(day);
        map.spawnNewGrasses(plantGrowthNumber);

        MapStats mapStats = gatherMapStats(day);

        map.statsChanged(mapStats);

        if(statsLoggingEnabled) {
            mapStats.logStatsToCSV("log_files/%s.csv".formatted(map.getId()));
        }

    }

    private MapStats gatherMapStats(int day) {
        Visitor visitor = new StatsVisitor(day);
        animals.forEach(animal -> animal.accept(visitor));
        deadAnimals.forEach(animal -> animal.accept(visitor));
        map.accept(visitor);
        return (MapStats) visitor.getStats();
    }

    private void removeDeadAnimals(int day) {
        List<Animal> toRemove = animals.stream()
                .filter(animal -> animal.getEnergy() <= 0)
                .peek(animal -> {
                    animal.setDeathDay(day);
                    map.killAnimal(animal);
                    deadAnimals.add(animal);
                })
                .toList();

        animals.removeAll(toRemove);
    }

    private void moveAnimals() {
        animals.forEach(map::move);
    }

    private void processGrassEating() {
        map.getGrass().stream()
                .filter(grass -> map.animalsAt(grass.getPosition()) != null)
                .forEach(grass -> {
                    map.killGrass(grass);

                    TreeSet<Animal> animalsAtGivenPos = new TreeSet<>(Comparator.comparingInt(Animal::getEnergy).reversed().thenComparing(Animal::getDayBornIn).thenComparing(Animal::getNumberOfChildren).reversed().thenComparing(Animal::getUniqueId));
                    animalsAtGivenPos.addAll(map.animalsAt(grass.getPosition()));
                    List<Animal> candidates = animalsAtGivenPos.stream()
                            .filter(animal -> animal.getEnergy() == animalsAtGivenPos.first().getEnergy() &&
                                    animal.getDayBornIn() == animalsAtGivenPos.first().getDayBornIn() &&
                                    animal.getNumberOfChildren() == animalsAtGivenPos.first().getNumberOfChildren())
                            .toList();

                    if (!candidates.isEmpty()) {
                        candidates.get(random.nextInt(candidates.size())).addEnergy(plantEnergyRegain);
                    }
                });
    }

    private void processReproduction(int day) {
        List<Animal> newAnimals = animals.stream()
                .filter(animal -> map.animalsAt(animal.getPosition()).size() > 1 && map.animalsAt(animal.getPosition()).stream().findFirst().orElse(null) == animal)
                .flatMap(animal -> {
                    TreeSet<Animal> animalsAtGivenPos = new TreeSet<>(Comparator.comparingInt(Animal::getEnergy).reversed().thenComparing(Animal::getDayBornIn).thenComparing(Animal::getNumberOfChildren).reversed().thenComparing(Animal::getUniqueId));
                    animalsAtGivenPos.addAll(map.animalsAt(animal.getPosition()));

                    List<Animal> bestCandidates = new ArrayList<>();
                    for(Animal potentialCandidate : animalsAtGivenPos) {
                        bestCandidates.add(potentialCandidate);
                        if(!(potentialCandidate.getEnergy() == animalsAtGivenPos.first().getEnergy()
                                && potentialCandidate.getDayBornIn() == animalsAtGivenPos.first().getDayBornIn()
                                && potentialCandidate.getNumberOfChildren() == animalsAtGivenPos.first().getNumberOfChildren())){
                            break;
                        }
                    }

                    if((bestCandidates.size() == 2 && bestCandidates.get(1).getEnergy() < reproductionReadyEnergy) ||
                            (bestCandidates.size() > 2 && bestCandidates.get(0).getEnergy() < reproductionReadyEnergy ))
                        return Stream.empty();

                    Animal pick1, pick2;
                    if(bestCandidates.size() == 2) {
                        pick1 = bestCandidates.get(0);
                        pick2 = bestCandidates.get(1);
                    } else {
                        bestCandidates.remove(bestCandidates.size() - 1);
                        pick1 = bestCandidates.get(random.nextInt(bestCandidates.size()));
                        do {
                            pick2 = bestCandidates.get(random.nextInt(bestCandidates.size()));
                        } while (pick1 == pick2);
                    }

                    Animal child = pick1.copulate(pick2, reproductionUsedEnergy, day);
                    map.placeNewAnimal(child);
                    map.mapChanged("Animal born on position: " + child.getPosition());

                    return Stream.of(child);
                })
                .toList();

        animals.addAll(newAnimals);
    }
}
