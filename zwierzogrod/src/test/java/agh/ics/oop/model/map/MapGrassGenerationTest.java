package agh.ics.oop.model.map;

import agh.ics.oop.model.util.directions.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapGrassGenerationTest {
    @Test
    public void testHellPortal() {
        WorldMap map = new HellPortal(10, 10, 10);
        int counter = 0;
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                if(map.grassAt(new Vector2d(i, j)) != null) counter++;

        assertEquals(counter, 10);
    }
    @Test
    public void testEarth() {
        WorldMap map = new Earth(10, 10, 10);
        int counter = 0;
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                if(map.grassAt(new Vector2d(i, j)) != null) counter++;

        assertEquals(counter, 10);
    }
    @Test
    public void testEarth2() {
        WorldMap map = new Earth(100, 10, 10);
        int counter = 0;
        for(int i=0;i<10;i++)
            for(int j=0;j<10;j++)
                if(map.grassAt(new Vector2d(i, j)) != null) counter++;

        assertEquals(counter, 100);
    }
}
