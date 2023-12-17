package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.MapChangeListener;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class WindowPresenter implements MapChangeListener {
    @FXML
    private Label moveLabel;
    @FXML
    private GridPane mapGrid;

    private WorldMap worldMap;
    private String args;
    public void setWorldMap(WorldMap map){
        worldMap = map;
    }
    public void setArgs(String args) {
        this.args = args;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    private String drawObject(Vector2d currentPosition) {
        if (this.worldMap.isOccupied(currentPosition)) {
            Object object = this.worldMap.objectAt(currentPosition);
            if (object != null) {
                return object.toString();
            }
        }

        return " ";
    }
    public void drawMap() {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        int map_height = boundary.rightUpper().getY() - boundary.leftLower().getY() + 1;
        int map_width = boundary.rightUpper().getX() - boundary.leftLower().getX() + 1;

        Label tmpLabel = new Label("y/x");
        mapGrid.add(tmpLabel, 0, 0, 1, 1);
        GridPane.setHalignment(tmpLabel, HPos.CENTER);
        for (int i = 0; i <= map_height; i++) {
            if (i == 0) {
                for (int j = 1; j <= map_width; j++) {
                    tmpLabel = new Label(String.valueOf(j - 1 + boundary.leftLower().getX()));
                    mapGrid.add(tmpLabel, j, 0, 1, 1);
                    GridPane.setHalignment(tmpLabel, HPos.CENTER);
                }
            }
            else {
                for (int j = 0; j <= map_width; j++) {
                    if(j == 0) {
                        tmpLabel = new Label(String.valueOf(boundary.rightUpper().getY()-i+1));
                        mapGrid.add(tmpLabel, j, i, 1, 1);
                        GridPane.setHalignment(tmpLabel, HPos.CENTER);
                    }
                    else {
                        tmpLabel = new Label(drawObject(new Vector2d(boundary.leftLower().getX()+j-1, boundary.rightUpper().getY()-i+1)));
                        mapGrid.add(tmpLabel, j, i, 1, 1);
                        GridPane.setHalignment(tmpLabel, HPos.CENTER);
                    }
                }
            }
        }

        for (int col = 0; col <= map_width; col++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0/(map_width+4));
            mapGrid.getColumnConstraints().add(colConstraints);
        }

        for (int row = 0; row <= map_height; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0/(map_height+4));
            mapGrid.getRowConstraints().add(rowConstraints);
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            moveLabel.setText(message);
        });
    }
    public void runSimulation() {
        GrassField grassField = new GrassField(10);
        setWorldMap(grassField);
        grassField.registerObserver(this);
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        Simulation simulation = new Simulation(positions, worldMap);
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        try {
            simulationEngine.runAsync();
        } catch (InterruptedException e) {
            moveLabel.setText(e.getMessage());
        }
    }
}
