package agh.ics.oop.model.util;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.util.directions.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomPositionGeneratorTest {
    @Test
    public void TestRandomPositionGenerator() {
        RandomGrassPositionGenerator rand = new RandomGrassPositionGenerator(100, 100, new HashMap<Vector2d, Grass >());
        Map<Vector2d, Integer> map = new HashMap<>();

        for (Vector2d elem : rand)
            map.put(elem, map.containsKey(elem) ? map.get(elem) + 1 : 1);

        for(Integer elem : map.values()) {
            assertEquals(elem, 1);
        }
    }
}
