package agh.ics.oop.model.util;

import agh.ics.oop.model.map.MapChangeListener;
import agh.ics.oop.model.map.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        worldMap.incrementUpdateCounter();
        synchronized (this) {
            System.out.println("\nMapa: " + worldMap.getId());
            System.out.println(message);
            System.out.print(worldMap);
            System.out.println("Update number: %d".formatted(worldMap.getUpdateCounter()));
        }
    }
}
