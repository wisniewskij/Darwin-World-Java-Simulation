package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static agh.ics.oop.SimulationApp.configureStage;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class SimulationPresenter {
    @FXML
    private TextField genomeTextField, maxMutationsTextField, minMutationsTextField, energyUsedToReplicateTextField,
            energyCopulateTextField, energyNumberTextField, animalNumberTextField, dailyPlantsTextField,
            plantEnergyTextField, plantsNumberTextField, mapWidthTextField, mapHeightTextField, speedTextField;

    @FXML
    private ChoiceBox<String> mapChoiceBox, mutationChoiceBox;

    @FXML
    private CheckBox statsCheckBox;

    @FXML
    public void initialize() {
        mapChoiceBox.setValue("Earth");
        mutationChoiceBox.setValue("Normal");
    }

    public static int parseIntOrDefault(TextField textField, int defaultValue) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String assureInRangeAndToString(int val, int lower, int upper) {
        return String.valueOf(min(max(val, lower), upper));
    }

    private void assureDataValidity() {
        mapWidthTextField.setText(assureInRangeAndToString(parseIntOrDefault(mapWidthTextField, 10), 1, 20));
        mapHeightTextField.setText(assureInRangeAndToString(parseIntOrDefault(mapHeightTextField, 10), 1, 20));

        int mapArea = Integer.parseInt(mapWidthTextField.getText()) * Integer.parseInt(mapHeightTextField.getText());

        dailyPlantsTextField.setText(assureInRangeAndToString(parseIntOrDefault(dailyPlantsTextField, 4), 0, mapArea));
        plantEnergyTextField.setText(assureInRangeAndToString(parseIntOrDefault(plantEnergyTextField, 5), 0, Integer.MAX_VALUE));
        animalNumberTextField.setText(assureInRangeAndToString(parseIntOrDefault(animalNumberTextField, 4), 1, mapArea));
        plantsNumberTextField.setText(assureInRangeAndToString(parseIntOrDefault(plantsNumberTextField, 10), 0, mapArea));
        energyCopulateTextField.setText(assureInRangeAndToString(parseIntOrDefault(energyCopulateTextField, 4), 0, Integer.MAX_VALUE));
        energyUsedToReplicateTextField.setText(assureInRangeAndToString(parseIntOrDefault(energyUsedToReplicateTextField, 3), 0, Integer.parseInt(energyCopulateTextField.getText())));
        speedTextField.setText(assureInRangeAndToString(parseIntOrDefault(speedTextField, 300), 50, 5000));
        genomeTextField.setText(assureInRangeAndToString(parseIntOrDefault(genomeTextField, 10), 1, 100));
        minMutationsTextField.setText(assureInRangeAndToString(parseIntOrDefault(minMutationsTextField, 0), 0, Integer.parseInt(genomeTextField.getText())));
        maxMutationsTextField.setText(assureInRangeAndToString(parseIntOrDefault(maxMutationsTextField, 0), Integer.parseInt(minMutationsTextField.getText()), Integer.parseInt(genomeTextField.getText())));
        energyNumberTextField.setText(assureInRangeAndToString(parseIntOrDefault(energyNumberTextField, 10), 0, Integer.MAX_VALUE));
    }

    public void onSimulationStartClicked() throws IOException {

        assureDataValidity();

        Stage newWindowStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = loader.load();
        WindowPresenter presenter = loader.getController();

        presenter.setArgs(mapChoiceBox.getValue(),
                mutationChoiceBox.getValue(),
                Integer.parseInt(mapWidthTextField.getText()),
                Integer.parseInt(mapHeightTextField.getText()),
                Integer.parseInt(minMutationsTextField.getText()),
                Integer.parseInt(maxMutationsTextField.getText()),
                Integer.parseInt(plantsNumberTextField.getText()),
                Integer.parseInt(animalNumberTextField.getText()),
                Integer.parseInt(dailyPlantsTextField.getText()),
                Integer.parseInt(plantEnergyTextField.getText()),
                Integer.parseInt(energyCopulateTextField.getText()),
                Integer.parseInt(energyUsedToReplicateTextField.getText()),
                Integer.parseInt(genomeTextField.getText()),
                Integer.parseInt(energyNumberTextField.getText()),
                Integer.parseInt(speedTextField.getText()),
                statsCheckBox.isSelected());

        configureStage(newWindowStage, viewRoot);
        newWindowStage.show();

        presenter.runSimulation();
    }

    @FXML
    private void onSaveClicked() {

        assureDataValidity();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Configuration Files", "*.properties"));

        setInitialDirectory(fileChooser);

        Stage stage = (Stage) genomeTextField.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveArgsToFile(file);
        }
    }

    @FXML
    private void onLoadClicked() {

        assureDataValidity();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Configuration Files", "*.properties"));

        setInitialDirectory(fileChooser);

        Stage stage = (Stage) genomeTextField.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            loadArgsFromFile(file);
        }
    }

    private void saveArgsToFile(File file) {
        try (OutputStream output = new FileOutputStream(file)) {
            Properties properties = new Properties();
            savePropertyIfNotEmpty(properties, "MAP_TYPE", mapChoiceBox.getValue());
            savePropertyIfNotEmpty(properties, "MUTATION_TYPE", mutationChoiceBox.getValue());
            savePropertyIfNotEmpty(properties, "MAP_WIDTH", mapWidthTextField.getText());
            savePropertyIfNotEmpty(properties, "MAP_HEIGHT", mapHeightTextField.getText());
            savePropertyIfNotEmpty(properties, "MIN_MUTATIONS", minMutationsTextField.getText());
            savePropertyIfNotEmpty(properties, "MAX_MUTATIONS", maxMutationsTextField.getText());
            savePropertyIfNotEmpty(properties, "PLANTS_NUMBER", plantsNumberTextField.getText());
            savePropertyIfNotEmpty(properties, "ANIMAL_NUMBER", animalNumberTextField.getText());
            savePropertyIfNotEmpty(properties, "DAILY_PLANTS", dailyPlantsTextField.getText());
            savePropertyIfNotEmpty(properties, "PLANT_ENERGY", plantEnergyTextField.getText());
            savePropertyIfNotEmpty(properties, "ENERGY_COPULATE", energyCopulateTextField.getText());
            savePropertyIfNotEmpty(properties, "ENERGY_USED_TO_REPLICATE", energyUsedToReplicateTextField.getText());
            savePropertyIfNotEmpty(properties, "GENOME", genomeTextField.getText());
            savePropertyIfNotEmpty(properties, "ENERGY_NUMBER", energyNumberTextField.getText());
            savePropertyIfNotEmpty(properties, "SPEED", speedTextField.getText());
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArgsFromFile(File file) {
        try (InputStream input = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(input);

            mapChoiceBox.setValue(properties.getProperty("MAP_TYPE", "Earth"));
            mutationChoiceBox.setValue(properties.getProperty("MUTATION_TYPE", "Normal"));
            mapWidthTextField.setText(properties.getProperty("MAP_WIDTH", "10"));
            mapHeightTextField.setText(properties.getProperty("MAP_HEIGHT", "10"));
            minMutationsTextField.setText(properties.getProperty("MIN_MUTATIONS", "0"));
            maxMutationsTextField.setText(properties.getProperty("MAX_MUTATIONS", "0"));
            plantsNumberTextField.setText(properties.getProperty("PLANTS_NUMBER", "10"));
            animalNumberTextField.setText(properties.getProperty("ANIMAL_NUMBER", "4"));
            dailyPlantsTextField.setText(properties.getProperty("DAILY_PLANTS", "2"));
            plantEnergyTextField.setText(properties.getProperty("PLANT_ENERGY", "4"));
            energyCopulateTextField.setText(properties.getProperty("ENERGY_COPULATE", "5"));
            energyUsedToReplicateTextField.setText(properties.getProperty("ENERGY_USED_TO_REPLICATE", "3"));
            genomeTextField.setText(properties.getProperty("GENOME", "10"));
            energyNumberTextField.setText(properties.getProperty("ENERGY_NUMBER", "10"));
            speedTextField.setText(properties.getProperty("SPEED", "300"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePropertyIfNotEmpty(Properties properties, String key, String value) {
        if (value != null && !value.isEmpty()) {
            properties.setProperty(key, value);
        }
    }

    private void setInitialDirectory(FileChooser fileChooser) {
        Path currentRelativePath = Paths.get("", "config_files");
        String projectLocation = currentRelativePath.toAbsolutePath().toString();
        fileChooser.setInitialDirectory(new File(projectLocation));
    }
}
