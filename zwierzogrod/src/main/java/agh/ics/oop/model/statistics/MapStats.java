package agh.ics.oop.model.statistics;

import agh.ics.oop.model.AnimalGenome;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapStats {
    private int animals, plants, emptyFields, mostPopularGenomeNo;
    private double averageEnergy, averageLifespan, averageChildren;
    private AnimalGenome mostPopularGenome;

    public MapStats(int animals, int plants, int emptyFields,
                    double averageEnergy, double averageLifespan,
                    double averageChildren, AnimalGenome mostPopularGenome, int mostPopularGenomeNo) {
        this.animals = animals;
        this.plants = plants;
        this.emptyFields = emptyFields;
        this.averageEnergy = averageEnergy;
        this.averageLifespan = averageLifespan;
        this.averageChildren = averageChildren;
        this.mostPopularGenome = mostPopularGenome;
        this.mostPopularGenomeNo = mostPopularGenomeNo;
    }

    // Setters
    public void setAnimals(int animals) {
        this.animals = animals;
    }
    public void setMostPopularGenomeNo(int mostPopularGenomeNo) {
        this.mostPopularGenomeNo = mostPopularGenomeNo;
    }

    public void setPlants(int plants) {
        this.plants = plants;
    }

    public void setEmptyFields(int emptyFields) {
        this.emptyFields = emptyFields;
    }

    public void setAverageEnergy(double averageEnergy) {
        this.averageEnergy = averageEnergy;
    }

    public void setAverageLifespan(double averageLifespan) {
        this.averageLifespan = averageLifespan;
    }

    public void setAverageChildren(double averageChildren) {
        this.averageChildren = averageChildren;
    }

    public void setMostPopularGenome(AnimalGenome mostPopularGenome) {
        this.mostPopularGenome = mostPopularGenome;
    }

    // Getters
    public int getAnimals() {
        return animals;
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
        // Check if the file is empty or doesn't exist, then write the header
        if (isFileEmpty(filePath)) {
            String header = "Age,Animals,Plants,EmptyFields,AverageEnergy,AverageLifespan,AverageChildren,MostPopularGenome";
            appendToFile(filePath, header);
        }
    }

    public void logStatsToCSV(String filePath, int dayNumber) {
        String data = formatAsCSV(dayNumber);
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

    private String formatAsCSV(int dayNumber) {
        return dayNumber + "," + animals + "," + plants + "," + emptyFields + "," +
                averageEnergy + "," + averageLifespan + "," + averageChildren + "," +
                (mostPopularGenome != null ? mostPopularGenome.toString() + " " + mostPopularGenomeNo : "N/A");
    }

    private static void appendToFile(String filePath, String data) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
