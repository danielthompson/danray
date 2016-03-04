package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shapes.Shape;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class DrawableXComparator implements Comparator<Shape> {
   @Override
   public int compare(Shape point1, Shape point2) {
      return (point1.getMedian(KDAxis.X) < point2.getMedian(KDAxis.X) ? -1 : (point1.getMedian(KDAxis.X) == point2.getMedian(KDAxis.X) ? 0 : 1));
   }
}
