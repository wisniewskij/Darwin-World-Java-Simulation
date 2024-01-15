package agh.ics.oop.model.util;

import agh.ics.oop.model.Grass;
import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;


public class RandomGrassPositionGenerator implements Iterable<Vector2d>{
    private final static int EQUATOR_AMOUNT_DIVISOR = 10;
    List<Vector2d> vectorList = new ArrayList<>(0);
    HashSet<Vector2d> alreadyPicked = new HashSet<>(0);

    public RandomGrassPositionGenerator(int mapHeight, int mapWidth, Map<Vector2d, Grass> occupiedPositions) {
        double goodDistanceFromEquator = (double) mapHeight / EQUATOR_AMOUNT_DIVISOR;
        for(int x=0; x<mapWidth; x++)
            for(int y=0; y<mapHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                if (occupiedPositions.containsKey(position)) continue;

                int distanceFromEquator = mapHeight % 2 == 0
                        ? Math.min(Math.abs(y - mapHeight / 2), Math.abs(y - (mapHeight / 2) + 1))
                        : Math.abs(y - (mapHeight - 1) / 2);

                int upperBound = distanceFromEquator <= goodDistanceFromEquator ? 4 : 1;
                vectorList.addAll(Collections.nCopies(upperBound, position));
            }
    }

    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {
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
