package net.danielthompson.danray.structures;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a zero-dimensional point in space.
 */
public class Point2 {

   public static AtomicLong instances = new AtomicLong();

   public float x;
   public float y;

   public Point2(float x, float y) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);

      this.x = x;
      this.y = y;
      instances.incrementAndGet();
   }

   public Point2(float[] xy) {
      assert !Float.isNaN(xy[0]);
      assert !Float.isNaN(xy[1]);

      this.x = xy[0];
      this.y = xy[1];
      instances.incrementAndGet();
   }

   public Point2(Point2 p) {
      assert !Float.isNaN(p.x);
      assert !Float.isNaN(p.y);

      this.x = p.x;
      this.y = p.y;
      instances.incrementAndGet();
   }

   public String toString() {
      return "(" + x + ", " + y + ")";
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Point2))
         return false;

      Point2 rhs = (Point2) obj;

      return (x == rhs.x && y == rhs.y);
   }
}
