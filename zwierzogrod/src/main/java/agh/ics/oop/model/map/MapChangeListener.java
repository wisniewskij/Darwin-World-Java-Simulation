package agh.ics.oop.model.map;

import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.MapStats;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String debugMessage);

    void statsChanged(MapStats stats);
}
