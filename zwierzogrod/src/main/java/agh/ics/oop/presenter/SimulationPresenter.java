package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

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
