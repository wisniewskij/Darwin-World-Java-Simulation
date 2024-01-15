package agh.ics.oop.model.statistics.visitor;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.statistics.Stats;

public interface Visitor {
    void visit(Animal animal);
    void visit(WorldMap map);
    Stats getStats();
}
