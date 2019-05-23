package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.structures.Point3;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class PointZComparator implements Comparator<Point3> {
   @Override
   public int compare(Point3 point1, Point3 point2) {
      return (point1.z < point2.z ? -1 : (point1.z == point2.z ? 0 : 1));
   }
}
