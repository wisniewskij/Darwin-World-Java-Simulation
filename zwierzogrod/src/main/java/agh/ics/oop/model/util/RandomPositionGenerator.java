package agh.ics.oop.model.util;

import agh.ics.oop.model.util.directions.Vector2d;

import java.util.*;


public class RandomPositionGenerator implements Iterable<Vector2d>{
    private final int n;
    List<Vector2d> vectorList = new ArrayList<Vector2d>(0);

    public RandomPositionGenerator(int minX, int maxX, int minY, int maxY, int n) {
        this.n = n;
        for(int i = 0; i < n; i++){
            double random = Math.random();
            boolean onEquatorTerrain = random >= 0.2;
            int x, y;
            x = (int) (Math.random() * (maxX-minX + 1) + minX);
            if (onEquatorTerrain) {
                y = (int) (Math.random() * (maxY / 4 - minY / 4 + 1) + minY / 4);
            } else {
                y = (int) (Math.random() * (maxY - minY + 1) + minY);
            }
            vectorList.add(new Vector2d(x, y));
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
