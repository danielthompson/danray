package net.danielthompson.danray.structures;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a direction in space.
 */
public class Vector2 {

   public static AtomicLong instances = new AtomicLong();

   public float x;
   public float y;

   public Vector2(float x, float y) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);

      this.x = x;
      this.y = y;
      instances.incrementAndGet();
   }

   public Vector2(float[] xy) {
      assert !Float.isNaN(xy[0]);
      assert !Float.isNaN(xy[1]);

      x = xy[0];
      y = xy[1];
      instances.incrementAndGet();
   }

   public Vector2(Normal n) {
      assert !Float.isNaN(n.X);
      assert !Float.isNaN(n.Y);

      x = n.X;
      y = n.Y;
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
      if (!(obj instanceof Vector2))
         return false;

      Vector2 rhs = (Vector2) obj;

      return (x == rhs.x && y == rhs.y);
   }
}
