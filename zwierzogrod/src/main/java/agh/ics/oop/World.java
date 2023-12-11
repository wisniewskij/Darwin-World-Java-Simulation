package agh.ics.oop;

import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.RectangularMap;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class World {
    static void run(List<MoveDirection> args) {
        for(MoveDirection arg: args){
            switch (arg){
                case FORWARD:
                    System. out. println("Zwierzak idzie do przodu");
                    break;
                case BACKWARD:
                    System. out. println("Zwierzak idzie do tylu");
                    break;
                case RIGHT:
                    System. out. println("Zwierzak skreca w prawo");
                    break;
                case LEFT:
                    System. out. println("Zwierzak skreca w lewo");
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
//        System. out. println("System wystartowal");
//        run(OptionsParser.stringToMoveDirectionEnum(args));

        List<MoveDirection> directions = OptionsParser.stringToMoveDirectionEnum(args);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4), new Vector2d(3,4), new Vector2d(3,3));

        List<Simulation> simulations = new ArrayList<>();

        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();

        for(int i=0;i<3;i++) {
            GrassField grassField = new GrassField(10);
            grassField.registerObserver(consoleMapDisplay);
            RectangularMap rectangularMap = new RectangularMap(10, 10);
            rectangularMap.registerObserver(consoleMapDisplay);
            simulations.add(new Simulation(directions, positions, grassField));
            simulations.add(new Simulation(directions, positions, rectangularMap));
        }

        SimulationEngine simEngine = new SimulationEngine(simulations);

        long start = System.currentTimeMillis();
        try {
            simEngine.runAsync();
//            simEngine.runAsyncInThreadPool();
            simEngine.awaitSimulationsEnd();
        } catch (InterruptedException e) {
            System. out. println("system wykrzaczyl");
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        System. out. println("system zakonczyl dzialanie " + timeElapsed);
    }
}
