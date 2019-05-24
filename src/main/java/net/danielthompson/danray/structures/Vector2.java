package net.danielthompson.danray.structures;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a direction in space.
 */
public class Vector2 {

   public static final AtomicLong instances = new AtomicLong();

   public float x;
   public float y;

   public Vector2(final float x, final float y) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);

      this.x = x;
      this.y = y;
      instances.incrementAndGet();
   }

   public Vector2(final Vector2 v) {
      assert !Float.isNaN(v.x);
      assert !Float.isNaN(v.y);

      x = v.x;
      y = v.y;
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

      final Vector2 rhs = (Vector2) obj;

      return (x == rhs.x && y == rhs.y);
   }
}
