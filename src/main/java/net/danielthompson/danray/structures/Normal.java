package net.danielthompson.danray.structures;

/**
 * Represents a vector that is perpendicular to a surface at some point. å
 */
public class Normal {
   public float x;
   public float y;
   public float z;
   
   public Normal(final float x, final float y, final float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Normal(final Vector3 n) {
      assert !Float.isNaN(n.x);
      assert !Float.isNaN(n.y);
      assert !Float.isNaN(n.z);

      x = n.x;
      y = n.y;
      z = n.z;
   }

   public Normal cross(final Vector3 v) {
      return new Normal(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x);
   }

   public float length() {
      return (float) Math.sqrt(x * x + y * y + z * z);
   }

   public void normalize() {
      final float t = 1.0f / length();
      scale(t);
   }

   public static Normal normalize(final Normal n) {
      final Normal normal = new Normal(n.x, n.y, n.z);
      normal.normalize();
      return normal;
   }

   public static Normal scale(final Normal n, final float t) {
      return new Normal(n.x * t, n.y * t, n.z * t);
   }

   public void scale(final float t) {
      x *= t;
      y *= t;
      z *= t;
   }

   public float dot(final Vector3 v) {
      return (x * v.x + y * v.y + z * v.z);
   }

   public float dot(final Normal n) {
      return (x * n.x + y * n.y + z * n.z);
   }

   public String toString() {
      return "(" + x + ", " + y + ", " + z + ")";
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Normal))
         return false;

      final Normal rhs = (Normal) obj;

      return (x == rhs.x && y == rhs.y && z == rhs.z);
   }
}
