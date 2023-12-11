package agh.ics.oop.model.map;

import agh.ics.oop.model.map.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}
