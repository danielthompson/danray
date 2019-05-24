package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a direction in space.
 */
public class Vector3 {

   public static final AtomicLong instances = new AtomicLong();

   public float x;
   public float y;
   public float z;

   public Vector3(final float x, final float y, final float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      this.x = x;
      this.y = y;
      this.z = z;
      instances.incrementAndGet();
   }

   public Vector3(final Normal n) {
      assert !Float.isNaN(n.x);
      assert !Float.isNaN(n.y);
      assert !Float.isNaN(n.z);

      x = n.x;
      y = n.y;
      z = n.z;
      instances.incrementAndGet();
   }

   public float getAxis(final KDAxis axis) {
      switch (axis) {
         case X:
            return x;
         case Y:
            return y;
         default:
            return z;
      }
   }

   public Vector3 cross(final Vector3 v) {
      return new Vector3(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x);
   }

   public Vector3 cross(final Normal n) {
      return new Vector3(
            y * n.z - z * n.y,
            z * n.x - x * n.z,
            x * n.y - y * n.x);
   }

   public float dot(final Vector3 v) {
      return (x * v.x + y * v.y + z * v.z);
   }

   public float dot(final Normal n) {
      return (x * n.x + y * n.y + z * n.z);
   }


   public static Vector3 scale(final Vector3 v, final float t) {
      return new Vector3(v.x * t, v.y * t, v.z * t);
   }

   public void scale(final float t) {
      x *= t;
      y *= t;
      z *= t;
   }

   public static Vector3 plus(final Vector3 v0, final Vector3 v1) {
      return new Vector3(v0.x + v1.x, v0.y + v1.y, v0.z + v1.z);
   }

   public void plus(final Vector3 v) {
      x += v.x;
      y += v.y;
      z += v.z;
   }

   public static Vector3 minus(final Vector3 v0, final Vector3 v1) {
      return new Vector3(v0.x - v1.x, v0.y - v1.y, v0.z - v1.z);
   }

   public static Vector3 minus(final Point3 p, final Vector3 v) {
      return new Vector3(p.x - v.x, p.y - v.y, p.z - v.z);
   }

   public static Vector3 minus(final Point3 p0, final Point3 p1) {
      return new Vector3(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);
   }

   public float length() {
      return (float) Math.sqrt(x * x + y * y + z * z);
   }

   public float lengthSquared() { return x * x + y * y + z * z; }

   public void normalize() {
      final float lengthMultiplier = 1.0f / length();
      scale(lengthMultiplier);
   }

   public static Vector3 normalize(final Vector3 v) {
      Vector3 vector = new Vector3(v.x, v.y, v.z);
      vector.normalize();
      return vector;
   }

   public static Vector3 lerp(final Vector3 v0, final Vector3 v1, final float t) {
      return Vector3.plus(v0, Vector3.scale(Vector3.minus(v1, v0), t));
   }

   public static Vector3 lerp(final Vector3 v0, final float t0, final Vector3 v1, final float t1) {

      final float x = v0.x * t0 + v1.x * t1;
      final float y = v0.y * t0 + v1.y * t1;
      final float z = v0.z * t0 + v1.z * t1;

      Vector3 v = new Vector3(x, y, z);
      v.normalize();
      return v;
   }

   public static Vector3 slerp(final Vector3 v0, final float t0, final Vector3 v1, final float t1) {

      final float cosOmega = v0.dot(v1);
      final float omega = (float)Math.acos(cosOmega);

      final float oneOverSinOmega = (float)Math.sin(omega);

      final float ww1 = (float)Math.sin(t0 * omega) * oneOverSinOmega;
      final float ww2 = (float)Math.sin(t1 * omega) * oneOverSinOmega;

      final float x = v0.x * ww1 + v1.x * ww2;
      final float y = v0.y * ww1 + v1.y * ww2;
      final float z = v0.z * ww1 + v1.z * ww2;

      Vector3 v = new Vector3(x, y, z);
      v.normalize();
      return v;
   }

   public String toString() {
      return "(" + x + ", " + y + ", " + z + ")";
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Vector3))
         return false;

      final Vector3 rhs = (Vector3) obj;

      return (x == rhs.x && y == rhs.y && z == rhs.z);
   }
}
