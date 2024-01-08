package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import static agh.ics.oop.SimulationApp.configureStage;

public class SimulationPresenter {
    @FXML
    private TextField argInput; // Depricated
    @FXML
    private TextField genomeTextField, maxMutationsTextField, minMutationsTextField, energyUsedToReplicateTextField, energyCopulateTextField, energyNumberTextField, animalNumberTextField, dailyPlantsTextField, plantEnergyTextField, plantsNumberTextField, mapWidthTextField, mapHeightTextField;
    @FXML
    private ChoiceBox<String> mapChoiceBox, mutationChoiceBox;

    public void onSimulationStartClicked() throws IOException {
        Stage newWindowStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = loader.load();
        WindowPresenter presenter = loader.getController();

        presenter.setArgs(mapChoiceBox.getValue(), mutationChoiceBox.getValue(), Integer.parseInt(mapWidthTextField.getText()), Integer.parseInt(mapHeightTextField.getText()), Integer.parseInt(minMutationsTextField.getText()), Integer.parseInt(maxMutationsTextField.getText()), Integer.parseInt(plantsNumberTextField.getText()), Integer.parseInt(animalNumberTextField.getText()), Integer.parseInt(dailyPlantsTextField.getText()), Integer.parseInt(plantEnergyTextField.getText()), Integer.parseInt(energyCopulateTextField.getText()), Integer.parseInt(energyUsedToReplicateTextField.getText()), Integer.parseInt(genomeTextField.getText()), Integer.parseInt(energyNumberTextField.getText()));

        configureStage(newWindowStage, viewRoot);
        newWindowStage.show();

        presenter.runSimulation();

//        Thread newSimThread = new Thread(presenter);
//        threads.add(newSimThread);
//        executorService.submit(newSimThread);

    }
}
