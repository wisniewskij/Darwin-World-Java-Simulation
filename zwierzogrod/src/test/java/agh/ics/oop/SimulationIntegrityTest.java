package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.RectangularMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.OptionsParser;
import agh.ics.oop.model.util.directions.MoveDirection;
import agh.ics.oop.model.util.directions.Vector2d;
import agh.ics.oop.model.util.exceptions.PositionAlreadyOccupiedException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationIntegrityTest {
    @Test
    public void testInputInterpretation() {
        String[] args1 = {"f", "l", "r", "b"};
        List<MoveDirection> ans1 = List.of(MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.BACKWARD);

        String[] args2 = {"f", "l", "r", "b"};
        List<MoveDirection> ans2 = List.of(MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.BACKWARD);

        String[] args3 = {};
        List<MoveDirection> ans3 = new ArrayList<>(0);

        List<Vector2d> positions = List.of(new Vector2d(2,2));



        List<MoveDirection> directions = OptionsParser.stringToMoveDirectionEnum(args1);
        WorldMap map = new RectangularMap(4, 4);
        Simulation simulation = new Simulation(directions, positions, map);
        assertEquals(ans1, simulation.directions);

        map = new RectangularMap(4, 4);
        directions = OptionsParser.stringToMoveDirectionEnum(args2);
        simulation = new Simulation(directions, positions, map);
        assertEquals(ans2, simulation.directions);

        map = new RectangularMap(4, 4);
        directions = OptionsParser.stringToMoveDirectionEnum(args3);
        simulation = new Simulation(directions, positions, map);
        assertEquals(ans3, simulation.directions);
    }
    @Test
    public void testSimulatedAnimalMovements() {
        String[] args1 = {"f", "f", "f", "f", "f", "f", "f", "f", "l", "r", "f"};
        List<Vector2d> positions1 = List.of(new Vector2d(0,0), new Vector2d(0,1));
        String ans1_1 = "(0,3) W";
        String ans1_2 = "(0,4) E";

        String[] args2 = {"l", "f", "l", "f", "l", "f", "r"};
        List<Vector2d> positions2 = List.of(new Vector2d(0,0));
        String ans2 = "(1,0) S";


        WorldMap map = new RectangularMap(4, 4);
        List<MoveDirection> directions = OptionsParser.stringToMoveDirectionEnum(args1);
        Simulation simulation = new Simulation(directions, positions1, map);
        simulation.run();
        assertEquals(ans1_1, simulation.animals.get(0).getPosition().toString() + " " + simulation.animals.get(0).toString());
        assertEquals(ans1_2, simulation.animals.get(1).getPosition().toString() + " " + simulation.animals.get(1).toString());

        map = new RectangularMap(4, 4);
        directions = OptionsParser.stringToMoveDirectionEnum(args2);
        simulation = new Simulation(directions, positions2, map);
        simulation.run();
        assertEquals(ans2, simulation.animals.get(0).getPosition().toString() + " " + simulation.animals.get(0).toString());
    }
    @Test
    public void testSimulationSpawn() {
        String[] args1 = {"f", "f", "f", "r", "f", "f", "f", "l", "l", "l"};
        List<Vector2d> positions = List.of(new Vector2d(3,3), new Vector2d(3,3), new Vector2d(3,3));
        String ans = "(6,6) S";


        WorldMap map = new RectangularMap(6, 6);
        List<MoveDirection> directions = OptionsParser.stringToMoveDirectionEnum(args1);
        Simulation simulation = new Simulation(directions, positions, map);
        simulation.run();
        assertEquals(simulation.animals.size(), 1);
        assertEquals(simulation.animals.get(0).getPosition() + " " + simulation.animals.get(0).toString(), ans);
    }

    @Test
    public void testGrassFieldSimulation() {
        String[] args1 = {"f", "f", "f", "f", "f", "f", "f", "f", "f", "f"};
        List<Vector2d> positions = List.of(new Vector2d(0,0), new Vector2d(0,1), new Vector2d(0,2));
        String ans = "(0,2) N";
        String ans1 = "(0,3) N";
        String ans2 = "(0,5) N";


        WorldMap map = new GrassField(10);
        List<MoveDirection> directions = OptionsParser.stringToMoveDirectionEnum(args1);
        Simulation simulation = new Simulation(directions, positions, map);
        simulation.run();
        assertEquals(simulation.animals.size(), 3);

        assertEquals(simulation.animals.get(0).getPosition() + " " + simulation.animals.get(0).toString(), ans);
        assertEquals(simulation.animals.get(1).getPosition() + " " + simulation.animals.get(1).toString(), ans1);
        assertEquals(simulation.animals.get(2).getPosition() + " " + simulation.animals.get(2).toString(), ans2);

        Animal testAnimal = new Animal(new Vector2d(0, 5));
        assertThrows(PositionAlreadyOccupiedException.class, ()->{
            map.place(testAnimal);
        });
        assertTrue(map.isOccupied(new Vector2d(0, 5)));
        assertTrue(map.objectAt(new Vector2d(0, 5)) instanceof Animal);
    }

    @Test
    public void additionalRectangularMapTests() {
        String[] args1 = {"f", "f", "f", "f", "f", "f", "f", "f", "l", "r", "f"};
        List<Vector2d> positions1 = List.of(new Vector2d(0,0), new Vector2d(0,1));
        String ans1_1 = "(0,3) W";
        String ans1_2 = "(0,4) E";

        String[] args2 = {"l", "f", "l", "f", "l", "f", "r"};
        List<Vector2d> positions2 = List.of(new Vector2d(0,0));
        String ans2 = "(1,0) S";


        WorldMap map = new RectangularMap(4, 4);
        List<MoveDirection> directions = OptionsParser.stringToMoveDirectionEnum(args1);
        Simulation simulation = new Simulation(directions, positions1, map);
        simulation.run();
        assertEquals(ans1_1, simulation.animals.get(0).getPosition().toString() + " " + simulation.animals.get(0).toString());
        assertEquals(ans1_2, simulation.animals.get(1).getPosition().toString() + " " + simulation.animals.get(1).toString());

        map = new RectangularMap(4, 4);
        directions = OptionsParser.stringToMoveDirectionEnum(args2);
        simulation = new Simulation(directions, positions2, map);
        simulation.run();
        assertEquals(ans2, simulation.animals.get(0).getPosition().toString() + " " + simulation.animals.get(0).toString());

        Animal testAnimal = new Animal(new Vector2d(1, 0));
        WorldMap finalMap = map;
        assertThrows(PositionAlreadyOccupiedException.class ,()->{ finalMap.place(testAnimal); });
        assertTrue(map.isOccupied(new Vector2d(1, 0)));
        assertTrue(map.objectAt(new Vector2d(1, 0)) instanceof Animal);
    }
}
