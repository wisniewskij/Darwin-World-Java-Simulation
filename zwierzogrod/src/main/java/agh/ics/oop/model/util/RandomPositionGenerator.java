package agh.ics.oop.model.util;

import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;


public class RandomPositionGenerator implements Iterable<Vector2d>{
    private final int n;
    List<Vector2d> vectorList = new ArrayList<Vector2d>(0);

    public RandomPositionGenerator(int minX, int maxX, int minY, int maxY, int n) {
        if (maxX<minX || maxY<minY || (maxX - minX + 1) * (maxY - minY + 1) < n ) {
            throw new java.util.NoSuchElementException();
        }
        this.n = n;
        for(int i=minX; i<=maxX; i++)
            for(int j=minY; j<=maxY; j++)
                vectorList.add(new Vector2d(i, j));
    }

    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>() {
            int currentIndex = 0;
            {
                Collections.shuffle(vectorList);
            }
            @Override
            public boolean hasNext() {
                return currentIndex < n ;
            }
            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return vectorList.get(currentIndex++);
            }
        };
    }
}
