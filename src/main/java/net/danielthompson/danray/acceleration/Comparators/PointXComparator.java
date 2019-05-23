package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.structures.Point3;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class PointXComparator implements Comparator<Point3> {
   @Override
   public int compare(Point3 point1, Point3 point2) {
      return (point1.x < point2.x ? -1 : (point1.x == point2.x ? 0 : 1));
   }
}
