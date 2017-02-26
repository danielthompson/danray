package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.structures.Point;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class PointXComparator implements Comparator<Point> {
   @Override
   public int compare(Point point1, Point point2) {
      return (point1.X < point2.X ? -1 : (point1.X == point2.X ? 0 : 1));
   }
}
