package agh.ics.oop.model;

import agh.ics.oop.model.util.directions.MoveDirection;
import org.junit.jupiter.api.Test;

import static agh.ics.oop.model.util.OptionsParser.stringToMoveDirectionEnum;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionsParserTest {
    @Test
    public void testStringToMoveDirectionEnum() {
        String[] s1 = {"f", "f", "l", "r", "b"};
        MoveDirection[] e1 = {MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.BACKWARD};
        String[] s2 = {"f", "l"};
        MoveDirection[] e2 = {MoveDirection.FORWARD, MoveDirection.LEFT};
        String[] s3 = {};
        MoveDirection[] e3 = {};

        assertArrayEquals(stringToMoveDirectionEnum(s1).toArray(), e1);
        assertArrayEquals(stringToMoveDirectionEnum(s2).toArray(), e2);
        assertArrayEquals(stringToMoveDirectionEnum(s3).toArray(), e3);
    }

    @Test
    public void testInvalidData() {
        String[] s1 = {"f", "f", "l", "r", "b", "dis"};
        MoveDirection[] e1 = {MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.BACKWARD};
        assertThrows(IllegalArgumentException.class, () -> {stringToMoveDirectionEnum(s1);});
    }
}
