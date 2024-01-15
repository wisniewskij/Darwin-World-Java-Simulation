package agh.ics.oop.model.map;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.directions.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTests {
    private WorldMap map;

    @BeforeEach
    void setUp() {
        map = new Earth();
    }

    @Test
    void testMapInitialization() {
        assertNotNull(map);
    }

    @Test
    void testGetUpdateCounter() {
        assertEquals(0, map.getUpdateCounter());
    }

    @Test
    void testIncrementUpdateCounter() {
        map.incrementUpdateCounter();
        assertEquals(1, map.getUpdateCounter());
    }

    @Test
    void testSpawnNewGrasses() {
        int initialGrassCount = map.getGrass().size();
        map.spawnNewGrasses(5);
        assertEquals(initialGrassCount + 5, map.getGrass().size());
    }

    @Test
    void testGetMapArea() {
        int expectedArea = 10 * 10; // assuming default constructor dimensions
        assertEquals(expectedArea, map.getMapArea());
    }

    @Test
    void testAnimalsAt() {
        Vector2d position = new Vector2d(1, 1);
        assertNull(map.animalsAt(position));
        Animal animal = new Animal(position, 0, 0, 0, 0, 1, true, 0);
        map.placeNewAnimal(animal);
        assertEquals(map.animalsAt(position).stream().findFirst().get(), animal);
    }

}
