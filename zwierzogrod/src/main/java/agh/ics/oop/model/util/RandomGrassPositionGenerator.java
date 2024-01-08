package agh.ics.oop.model.util;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;


public class RandomGrassPositionGenerator implements Iterable<Vector2d>{
    private final static int EQUATOR_AMOUNT_DIVISOR = 10;
    List<Vector2d> vectorList = new ArrayList<Vector2d>(0);
    HashSet<Vector2d> alreadyPicked = new HashSet<>(0);

    public RandomGrassPositionGenerator(int mapHeight, int mapWidth, Map<Vector2d, Grass> occupiedPositions) {
//        if (mapHeight <= 0 || mapWidth <= 0) throw new java.util.NoSuchElementException();
        double goodDistanceFromEquator = (double) mapHeight / EQUATOR_AMOUNT_DIVISOR;
        for(int i=0; i<mapWidth; i++)
            for(int j=0; j<mapHeight; j++) {
                if (occupiedPositions.containsKey(new Vector2d(i, j))) continue;
                int upperBound = (((mapHeight%2 == 1 && abs(j - (mapHeight - 1) / 2) <= goodDistanceFromEquator) || (mapHeight%2 == 0 && min(abs(j - (mapHeight/ 2)), abs(j - (mapHeight/ 2) + 1)) <= goodDistanceFromEquator))?4:1);
                for(int k=0;k<upperBound;k++)
                    vectorList.add(new Vector2d(i, j));
            }
    }

    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>() {
            int currentIndex = 0;
            {
                Collections.shuffle(vectorList);
            }
            @Override
            public boolean hasNext() {
                return currentIndex < vectorList.size();
            }
            @Override
            public Vector2d next() {
                if (!hasNext())
                    throw new java.util.NoSuchElementException();
                alreadyPicked.add(vectorList.get(currentIndex));
                int tmpIndex = currentIndex ++;
                while(hasNext() && alreadyPicked.contains(vectorList.get(currentIndex))) currentIndex++;
                return vectorList.get(tmpIndex);
            }
        };
    }
}
