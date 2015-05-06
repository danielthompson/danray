package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shapes.Drawable;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class DrawableYComparator implements Comparator<Drawable> {
   @Override
   public int compare(Drawable point1, Drawable point2) {
      return (point1.getMedian(KDAxis.Y) < point2.getMedian(KDAxis.Y) ? -1 : (point1.getMedian(KDAxis.Y) == point2.getMedian(KDAxis.Y) ? 0 : 1));
   }
}
