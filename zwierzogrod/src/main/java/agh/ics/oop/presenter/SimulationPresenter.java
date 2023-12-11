package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.MapChangeListener;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static agh.ics.oop.SimulationApp.configureStage;

public class SimulationPresenter {
    @FXML
    private TextField argInput;

//    private List<Thread> threads = new ArrayList<Thread>();
//    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void onSimulationStartClicked() throws IOException {
        Stage newWindowStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = loader.load();
        WindowPresenter presenter = loader.getController();

        presenter.setArgs(argInput.getText());

        configureStage(newWindowStage, viewRoot);
        newWindowStage.show();

        presenter.runSimulation();

//        Thread newSimThread = new Thread(presenter);
//        threads.add(newSimThread);
//        executorService.submit(newSimThread);

    }
}
