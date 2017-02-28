package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.shapes.AbstractShape;


/**
 * Created by daniel on 12/13/14.
 */
public class BoundingEdge implements Comparable {
   public boolean Lower;
   public AbstractShape Shape;
   public double Value;

   @Override
   public int compareTo(Object o) {
      BoundingEdge that = (BoundingEdge)o;

      if (Value < that.Value)
         return -1;
      if (Value > that.Value)
         return 1;
      return 0;


   }
}
