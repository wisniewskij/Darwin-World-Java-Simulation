package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.AnimalGenome;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.HellPortal;
import agh.ics.oop.model.map.MapChangeListener;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.AnimalStats;
import agh.ics.oop.model.statistics.MapStats;
import agh.ics.oop.model.statistics.visitor.AnimalVisitor;
import agh.ics.oop.model.statistics.visitor.Visitor;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.directions.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class WindowPresenter implements MapChangeListener {
    @FXML
    private Label moveLabel;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button pauseButton, jungleButton, genomeButton;

    @FXML
    private Label totalAnimalsLabel, totalPlantsLabel, emptyFieldsLabel, mostPopularGenomeLabel, averageEnergyLabel,
            averageLifespanLabel, averageChildrenLabel, mapIDLabel, animPosLabel, animGenLabel, animEnergyLabel, animPlantsLabel, animDescLabel,
            animChildrenLabel, animLifeSpanLabel, animDiedInLabel, ageLabel;

    private WorldMap worldMap;
    private Boolean statsLoggingEnabled;
    private Boolean jungleHighlighted, genomeHighlighted;
    private Animal pickedAnimal;

    AnimalGenome mostPopularGenome;

    private Simulation simulation;

    private int initialEnergy, minMutations, maxMutations, mapWidth, mapHeight, plantEnergyRegain, initialAnimalNumber, initialPlantNumber, plantGrowNumber, reproductionReadyEnergy, reproductionUsedEnergy, genomeLength, simulationSpeed, currentAge;
    private String mapVariant, mutationsVariant;
    public void setWorldMap(WorldMap map){
        worldMap = map;
    }
    public void setArgs(String mapVariant, String mutationsVariant, int mapWidth, int mapHeight, int minMutations, int maxMutations, int initialPlantNumber, int initialAnimalNumber, int plantGrowNumber, int plantEnergyRegain, int reproductionReadyEnergy, int reproductionUsedEnergy, int genomeLength, int initialEnergy, int simulationSpeed, Boolean statsLoggingEnabled) {
        this.mapVariant = mapVariant;
        this.mutationsVariant = mutationsVariant;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.initialAnimalNumber = initialAnimalNumber;
        this.initialPlantNumber = initialPlantNumber;
        this.plantEnergyRegain = plantEnergyRegain;
        this.plantGrowNumber = plantGrowNumber;
        this.reproductionReadyEnergy = reproductionReadyEnergy;
        this.reproductionUsedEnergy = reproductionUsedEnergy;
        this.genomeLength = genomeLength;
        this.initialEnergy = initialEnergy;
        this.simulationSpeed = simulationSpeed;
        this.statsLoggingEnabled = statsLoggingEnabled;
        jungleHighlighted = Boolean.FALSE;
        genomeHighlighted = Boolean.FALSE;
        mostPopularGenome = null;
        pickedAnimal = null;
        currentAge = 0;
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
        mapIDLabel.setText(worldMap.getId().toString());

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
                        Vector2d objectPosition = new Vector2d(boundary.leftLower().getX()+j-1, boundary.rightUpper().getY()-i+1);

                        tmpLabel = new Label(drawObject(objectPosition));
                        mapGrid.add(tmpLabel, j, i, 1, 1);
                        GridPane.setHalignment(tmpLabel, HPos.CENTER);

                        tmpLabel.setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                if(this.worldMap.animalsAt(objectPosition) != null) {
                                    pickedAnimal = this.worldMap.animalsAt(objectPosition).stream().findFirst().orElse(null);
                                    checkAnimalStatsStatus();
                                }

                            }
                        });

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

        if(jungleHighlighted) {
            double goodDistanceFromEquator = (double) mapHeight / 10.0;
            for(Node child : mapGrid.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                Integer columnIndex = GridPane.getColumnIndex(child);

                int j = rowIndex != null ? rowIndex - 1 : 0;
                int i = columnIndex != null ? columnIndex - 1 : 0;
                if ( (i >= 0 && j >= 0) && (mapHeight%2 == 1 && abs(j - (mapHeight - 1) / 2) <= 1) || (mapHeight%2 == 0 && min(abs(j - (mapHeight/ 2)), abs(j - (mapHeight/ 2) + 1)) <= goodDistanceFromEquator))
                    child.setStyle("-fx-background-color: green; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

            }
        }

        if(genomeHighlighted && mostPopularGenome != null) {
            for(Node child : mapGrid.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                Integer columnIndex = GridPane.getColumnIndex(child);
                int j = rowIndex != null ? mapHeight - rowIndex : 0;
                int i = columnIndex != null ? columnIndex - 1 : 0;
                if(worldMap.animalsAt(new Vector2d(i, j)) != null && worldMap.animalsAt(new Vector2d(i, j)).stream().anyMatch(animal -> animal.getGenome().equals(mostPopularGenome)))
                    child.setStyle("-fx-background-color: red; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

            }
        }

        if(pickedAnimal != null) {
            for(Node child : mapGrid.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                Integer columnIndex = GridPane.getColumnIndex(child);
                int j = rowIndex != null ? mapHeight - rowIndex : 0;
                int i = columnIndex != null ? columnIndex - 1 : 0;
                if(worldMap.animalsAt(new Vector2d(i, j)) != null && worldMap.animalsAt(new Vector2d(i, j)).contains(pickedAnimal))
                    if((genomeHighlighted && mostPopularGenome != null) && (worldMap.animalsAt(new Vector2d(i, j)) != null && worldMap.animalsAt(new Vector2d(i, j)).stream().anyMatch(animal -> animal.getGenome().equals(mostPopularGenome)))) {
                        child.setStyle("-fx-background-color: purple; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                    }
                    else {
                        child.setStyle("-fx-background-color: blue; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                    }
            }
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String debugMessage) {
        Platform.runLater(this::drawMap);
    }
    public void runSimulation() {
        WorldMap map;
        if(mapVariant.equals("Infernal Portal")) {
            map = new HellPortal(initialPlantNumber, mapHeight, mapWidth);
        } else {
            map = new Earth(initialPlantNumber, mapHeight, mapWidth);
        }


        setWorldMap(map);
        map.registerObserver(this);


        simulation = new Simulation(map, minMutations, maxMutations, plantEnergyRegain, initialAnimalNumber, plantGrowNumber, reproductionReadyEnergy, reproductionUsedEnergy, genomeLength, initialEnergy, mutationsVariant.equals("A little bit of chaos"), simulationSpeed, statsLoggingEnabled);
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        try {
            simulationEngine.runAsync();
        } catch (InterruptedException e) {
            moveLabel.setText(e.getMessage());
        }
    }

    public void statsChanged(MapStats stats) {
        currentAge = stats.getCurrentDay();
        mostPopularGenome = stats.getAnimals() == 0 ? null : stats.getMostPopularGenome();
        Platform.runLater(() -> {
            ageLabel.setText(String.valueOf(stats.getCurrentDay()));
            totalAnimalsLabel.setText(String.valueOf(stats.getAnimals()));
            totalPlantsLabel.setText(String.valueOf(stats.getPlants()));
            emptyFieldsLabel.setText(String.valueOf(stats.getEmptyFields()));
            mostPopularGenomeLabel.setText( stats.getAnimals() == 0 ? "N/A" : stats.getMostPopularGenome().toString() + " " + stats.getMostPopularGenomeNo());
            averageEnergyLabel.setText(String.format("%.2f", stats.getAverageEnergy()));
            averageLifespanLabel.setText(String.format("%.2f", stats.getAverageLifespan()));
            averageChildrenLabel.setText(String.format("%.2f", stats.getAverageChildren()));
        });

        checkAnimalStatsStatus();
    }

    public void checkAnimalStatsStatus() {
        if (pickedAnimal != null) {
            Visitor animalVisitor = new AnimalVisitor();
            pickedAnimal.accept(animalVisitor);
            AnimalStats animalStats = (AnimalStats) animalVisitor.getStats();
            animalStatsChanged(animalStats);
        }
    }
    public void animalStatsChanged(AnimalStats animStats) {
        Platform.runLater(() -> {
            if(pickedAnimal != null) {
                animPosLabel.setText(pickedAnimal.getPosition().toString());
                animGenLabel.setText(animStats.getAnimalGenome().toString() + " (" + animStats.getActiveGene() + ")");
                animEnergyLabel.setText(String.valueOf(animStats.getEnergy()));
                animPlantsLabel.setText(String.valueOf(animStats.getPlantsEaten()));
                animChildrenLabel.setText(String.valueOf(animStats.getChildren()));
                animDescLabel.setText(String.valueOf(animStats.getDescendants()));
                animLifeSpanLabel.setText(String.valueOf(min(animStats.getDayDiedIn() == -1 ? currentAge : animStats.getDayDiedIn(), currentAge) - animStats.getDayBornIn()));
                animDiedInLabel.setText(animStats.getDayDiedIn() == -1 ? "Still Alive" : String.valueOf(animStats.getDayDiedIn()));
            } else {
                animPosLabel.setText("");
                animGenLabel.setText("");
                animEnergyLabel.setText("");
                animPlantsLabel.setText("");
                animChildrenLabel.setText("");
                animDescLabel.setText("");
                animLifeSpanLabel.setText("");
                animDiedInLabel.setText("");
            }
        });
    }

    public void onPauseClicked() {
        simulation.togglePause();
        if(simulation.isPaused()) {
            pauseButton.setText("Start");
        } else {
            pauseButton.setText("Pause");
        }
    }

    public void onJungleButtonClicked() {
        jungleHighlighted = !jungleHighlighted;
        if(jungleHighlighted) {
            jungleButton.setText("Hide Jungle");
        } else {
            jungleButton.setText("Show Jungle");
        }
        mapChanged(worldMap, "Jungle Button Clicked");
    }
    public void onGenomeButtonClicked() {
        genomeHighlighted = !genomeHighlighted;
        if(genomeHighlighted) {
            genomeButton.setText("Hide Dominating Genome");
        } else {
            genomeButton.setText("Show Dominating Genome");
        }
        mapChanged(worldMap, "Genome Button Clicked");
    }

    public void onAnimalPickButtonButtonClicked() {
        pickedAnimal = null;
        animalStatsChanged(null);
    }


}
