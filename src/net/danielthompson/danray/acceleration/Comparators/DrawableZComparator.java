package net.danielthompson.danray.acceleration.Comparators;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shapes.Drawable;

import java.util.Comparator;

/**
 * Created by daniel on 12/6/14.
 */
public class DrawableZComparator implements Comparator<Drawable> {
   @Override
   public int compare(Drawable point1, Drawable point2) {
      return (point1.getMedian(KDAxis.Z) < point2.getMedian(KDAxis.Z) ? -1 : (point1.getMedian(KDAxis.Z) == point2.getMedian(KDAxis.Z) ? 0 : 1));
   }
}
