package agh.ics.oop.model.statistics;

import agh.ics.oop.model.AnimalGenome;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapStats implements Stats {
    private int animals, plants, emptyFields, mostPopularGenomeNo;
    private final int currentDay;
    private double averageEnergy, averageLifespan, averageChildren;
    private AnimalGenome mostPopularGenome;

    public MapStats(int animals, int plants, int emptyFields,
                    double averageEnergy, double averageLifespan,
                    double averageChildren, AnimalGenome mostPopularGenome, int mostPopularGenomeNo, int currentDay) {
        this.animals = animals;
        this.plants = plants;
        this.emptyFields = emptyFields;
        this.averageEnergy = averageEnergy;
        this.averageLifespan = averageLifespan;
        this.averageChildren = averageChildren;
        this.mostPopularGenome = mostPopularGenome;
        this.mostPopularGenomeNo = mostPopularGenomeNo;
        this.currentDay = currentDay;
    }


    // Getters
    public int getAnimals() {
        return animals;
    }
    public int getCurrentDay() {
        return currentDay;
    }
    public int getMostPopularGenomeNo() {
        return mostPopularGenomeNo;
    }

    public int getPlants() {
        return plants;
    }

    public int getEmptyFields() {
        return emptyFields;
    }

    public double getAverageEnergy() {
        return averageEnergy;
    }

    public double getAverageLifespan() {
        return averageLifespan;
    }

    public double getAverageChildren() {
        return averageChildren;
    }

    public AnimalGenome getMostPopularGenome() {
        return mostPopularGenome;
    }

    // CSV
    public static void writeHeaderToCSV(String filePath) {
        if (isFileEmpty(filePath)) {
            String header = "Age,Animals,Plants,EmptyFields,AverageEnergy,AverageLifespan,AverageChildren,MostPopularGenome";
            appendToFile(filePath, header);
        }
    }

    public void logStatsToCSV(String filePath) {
        String data = formatAsCSV();
        appendToFile(filePath, data);
    }

    private static boolean isFileEmpty(String filePath) {
        try {
            final Path path = Paths.get(filePath);
            return Files.notExists(path) || Files.size(path) == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatAsCSV() {
        return String.join(",",
                String.valueOf(currentDay),
                String.valueOf(animals),
                String.valueOf(plants),
                String.valueOf(emptyFields),
                String.valueOf(averageEnergy),
                String.valueOf(averageLifespan),
                String.valueOf(averageChildren),
                mostPopularGenome != null ? mostPopularGenome + " " + mostPopularGenomeNo : "N/A"
        );
    }

    private static void appendToFile(String filePath, String data) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
