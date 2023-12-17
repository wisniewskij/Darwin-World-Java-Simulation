package agh.ics.oop.model;

import agh.ics.oop.model.util.directions.MapDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void testNext() {
        assertEquals(MapDirection.EAST, MapDirection.NORTH.next(2));
        assertEquals(MapDirection.SOUTH, MapDirection.EAST.next(2));
        assertEquals(MapDirection.WEST, MapDirection.SOUTH.next(2));
        assertEquals(MapDirection.NORTH, MapDirection.WEST.next(2));
    }
    @Test
    public void testPrevious() {
        assertEquals(MapDirection.EAST, MapDirection.SOUTH.previous().previous());
        assertEquals(MapDirection.SOUTH, MapDirection.WEST.previous().previous());
        assertEquals(MapDirection.WEST, MapDirection.NORTH.previous().previous());
        assertEquals(MapDirection.NORTH, MapDirection.EAST.previous().previous());
    }
}
