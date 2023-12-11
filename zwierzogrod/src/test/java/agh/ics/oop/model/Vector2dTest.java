package agh.ics.oop.model;

import agh.ics.oop.model.util.directions.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void testEquals() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 2);
        Vector2d v3 = new Vector2d(2, 2);

        String s = new String("dis");

        assertTrue(v1.equals(v2));
        assertTrue(v1.equals(v1));
        assertFalse(v1.equals(v3));
        assertFalse(v1.equals(s));
    }
    @Test
    public void testToString() {
        Vector2d v1 = new Vector2d(1, 2);
        assertEquals(v1.toString(), "(1,2)");
    }
    @Test
    public void testPrecedes() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 3);
        Vector2d v3 = new Vector2d(2, 2);
        Vector2d v4 = new Vector2d(5, 6);

        assertTrue(v1.precedes(v1));
        assertTrue(v1.precedes(v2));
        assertTrue(v1.precedes(v3));
        assertTrue(v1.precedes(v4));
        assertFalse(v4.precedes(v1));
    }
    @Test
    public void testFollows() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 3);
        Vector2d v3 = new Vector2d(2, 2);
        Vector2d v4 = new Vector2d(5, 6);

        assertTrue(v1.follows(v1));
        assertTrue(v2.follows(v1));
        assertTrue(v3.follows(v1));
        assertTrue(v4.follows(v1));
        assertFalse(v1.follows(v4));
    }
    @Test
    public void testUpperRight() {
        Vector2d v1 = new Vector2d(-1, 1);
        Vector2d v2 = new Vector2d(1, -1);
        Vector2d v3 = new Vector2d(1, 1);

        assertEquals(v1.upperRight(v1), v1);
        assertEquals(v1.upperRight(v2), v3);
    }
    @Test
    public void testLowerLeft() {
        Vector2d v1 = new Vector2d(-1, 1);
        Vector2d v2 = new Vector2d(1, -1);
        Vector2d v3 = new Vector2d(-1, -1);

        assertEquals(v1.lowerLeft(v1), v1);
        assertEquals(v1.lowerLeft(v2), v3);
    }
    @Test
    public void testAdd() {
        Vector2d v1 = new Vector2d(-1, 1);
        Vector2d v2 = new Vector2d(1, -1);
        Vector2d v3 = new Vector2d(-2, 2);
        Vector2d v4 = new Vector2d(0, 0);

        assertEquals(v1.add(v1), v3);
        assertEquals(v1.add(v2), v4);
    }
    @Test
    public void testSubtract() {
        Vector2d v1 = new Vector2d(-1, 1);
        Vector2d v2 = new Vector2d(1, -1);
        Vector2d v3 = new Vector2d(0, 0);
        Vector2d v4 = new Vector2d(-2, 2);

        assertEquals(v1.subtract(v1), v3);
        assertEquals(v1.subtract(v2), v4);
    }
    @Test
    public void testOpposite() {
        Vector2d v1 = new Vector2d(-1, 1);
        Vector2d v2 = new Vector2d(1, -1);

        assertEquals(v1, v2.opposite());
        assertEquals(v2, v1.opposite());
        assertEquals(v1, v1.opposite().opposite());
    }
}
