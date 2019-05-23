package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a direction in space.
 */
public class Vector3 {

   public static AtomicLong instances = new AtomicLong();

   public float x;
   public float y;
   public float z;

   public Vector3(float x, float y, float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      this.x = x;
      this.y = y;
      this.z = z;
      instances.incrementAndGet();
   }

   public Vector3(float[] xyz) {
      assert !Float.isNaN(xyz[0]);
      assert !Float.isNaN(xyz[1]);
      assert !Float.isNaN(xyz[2]);

      x = xyz[0];
      y = xyz[1];
      z = xyz[2];
      instances.incrementAndGet();
   }

   public Vector3(Normal n) {
      assert !Float.isNaN(n.x);
      assert !Float.isNaN(n.y);
      assert !Float.isNaN(n.z);

      x = n.x;
      y = n.y;
      z = n.z;
      instances.incrementAndGet();
   }

   public float getAxis(KDAxis axis) {
      switch (axis) {
         case X:
            return x;
         case Y:
            return y;
         default:
            return z;
      }
   }

   public Vector3 cross(Vector3 vector) {
      return new Vector3(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x);
   }

   public Vector3 cross(Normal normal) {
      return new Vector3(
            y * normal.z - z * normal.y,
            z * normal.x - x * normal.z,
            x * normal.y - y * normal.x);
   }

   public float dot(Vector3 vector) {
      return (x * vector.x + y * vector.y + z * vector.z);
   }

   public float dot(Normal normal) {
      return (x * normal.x + y * normal.y + z * normal.z);
   }


   public static Vector3 scale(Vector3 vector, float t) {
      return new Vector3(vector.x * t, vector.y * t, vector.z * t);
   }

   public void scale(float t) {
      x *= t;
      y *= t;
      z *= t;
   }

   public static Vector3 plus(Vector3 vector1, Vector3 vector2) {
      return new Vector3(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);
   }

   public void plus(Vector3 vector) {
      x += vector.x;
      y += vector.y;
      z += vector.z;
   }

   public static Vector3 minus(Vector3 vector1, Vector3 vector2) {
      return new Vector3(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);
   }

   public static Vector3 minus(Point3 point, Vector3 vector) {
      return new Vector3(point.x - vector.x, point.y - vector.y, point.z - vector.z);
   }

   public static Vector3 minus(Point3 point1, Point3 point2) {
      return new Vector3(point1.x - point2.x, point1.y - point2.y, point1.z - point2.z);
   }

   public float length() {
      return (float) Math.sqrt(x * x + y * y + z * z);
   }

   public float lengthSquared() { return x * x + y * y + z * z; }

   public void normalize() {
      float lengthMultiplier = 1.0f / length();
      scale(lengthMultiplier);
   }

   public static Vector3 normalize(Vector3 vector) {
      Vector3 v = new Vector3(vector.x, vector.y, vector.z);
      v.normalize();
      return v;
   }

   public static Vector3 lerp(Vector3 vector1, Vector3 vector2, float percentage) {
      return Vector3.plus(vector1, Vector3.scale(Vector3.minus(vector2, vector1), percentage));
   }

   public static Vector3 lerp(Vector3 v1, float w1, Vector3 v2, float w2) {

      float x = v1.x * w1 + v2.x * w2;
      float y = v1.y * w1 + v2.y * w2;
      float z = v1.z * w1 + v2.z * w2;

      Vector3 v = new Vector3(x, y, z);
      v.normalize();
      return v;
   }

   public static Vector3 slerp(Vector3 v1, float w1, Vector3 v2, float w2) {

      float cosOmega = v1.dot(v2);
      float omega = (float)Math.acos(cosOmega);

      float oneOverSinOmega = (float)Math.sin(omega);

      float ww1 = (float)Math.sin(w1 * omega) * oneOverSinOmega;
      float ww2 = (float)Math.sin(w2 * omega) * oneOverSinOmega;

      float x = v1.x * ww1 + v2.x * ww2;
      float y = v1.y * ww1 + v2.y * ww2;
      float z = v1.z * ww1 + v2.z * ww2;

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

      Vector3 rhs = (Vector3) obj;

      return (x == rhs.x && y == rhs.y && z == rhs.z);
   }
}
