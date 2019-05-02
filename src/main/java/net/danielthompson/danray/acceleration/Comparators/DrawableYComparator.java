package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shapes.AbstractShape;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class DrawableYComparator implements Comparator<AbstractShape> {
   @Override
   public int compare(AbstractShape point1, AbstractShape point2) {
      return (point1.GetMedian(KDAxis.Y) < point2.GetMedian(KDAxis.Y) ? -1 : (point1.GetMedian(KDAxis.Y) == point2.GetMedian(KDAxis.Y) ? 0 : 1));
   }
}
