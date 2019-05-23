package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a zero-dimensional point in space.
 */
public class Point3 implements Cloneable {

   public static AtomicLong instances = new AtomicLong();

   public float x;
   public float y;
   public float z;

   public Point3(float x, float y, float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      this.x = x;
      this.y = y;
      this.z = z;
      instances.incrementAndGet();
   }

   public Point3(float[] xyz) {
      assert !Float.isNaN(xyz[0]);
      assert !Float.isNaN(xyz[1]);
      assert !Float.isNaN(xyz[2]);

      this.x = xyz[0];
      this.y = xyz[1];
      this.z = xyz[2];
      instances.incrementAndGet();
   }

   public Point3(Point3 p) {
      assert !Float.isNaN(p.x);
      assert !Float.isNaN(p.y);
      assert !Float.isNaN(p.z);

      this.x = p.x;
      this.y = p.y;
      this.z = p.z;
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

   public void setAxis(KDAxis axis, float value) {
      switch (axis) {
         case X:
            x = value;
            break;
         case Y:
            y = value;
            break;
         default:
            z = value;
            break;
      }
   }

   public Ray createVectorFrom(Point3 originPoint) {
      float xDirection = x - originPoint.x;
      float yDirection = y - originPoint.y;
      float zDirection = z - originPoint.z;

      Vector3 direction = new Vector3(xDirection, yDirection, zDirection);
      return new Ray(originPoint, direction);
   }

   public float dot(Point3 point) {
      return (x * point.x + y * point.y + z * point.z);
   }

   public Point3 cross(Point3 point) {
      return new Point3(
            y * point.z - z * point.y,
            z * point.x - x * point.z,
            x * point.y - y * point.x);
   }

   public void minus(Point3 point) {
      x -= point.x;
      y -= point.y;
      z -= point.z;
   }

   public static Vector3 minus(Point3 point1, Point3 point2) {
      return new Vector3(point1.x - point2.x, point1.y - point2.y, point1.z - point2.z);
   }

   public static Point3 minus(Point3 point, Vector3 vector) {
      return new Point3(point.x - vector.x, point.y - vector.y, point.z - vector.z);
   }

   public static Point3 lerp(Point3 point1, Point3 point2, float percentage) {
      return Point3.plus(point1, Vector3.scale(Point3.minus(point2, point1), percentage));
   }

   public float squaredDistanceBetween(Point3 point) {
      return (point.x - x) * (point.x - x) +
             (point.y - y) * (point.y - y) +
             (point.z - z) * (point.z - z);
   }

   public void plus(Point3 point) {
      x += point.x;
      y += point.y;
      z += point.z;
   }

   public void plus(Vector3 vector) {
      x += vector.x;
      y += vector.y;
      z += vector.z;
   }

   public static Point3 plus(Point3 point1, Point3 point2) {
      return new Point3(point1.x + point2.x, point1.y + point2.y, point1.z + point2.z);
   }

   public static Point3 plus(Point3 point, Vector3 vector) {
      return new Point3(point.x + vector.x, point.y + vector.y, point.z + vector.z);
   }

   public void scale(float t) {
      x *= t;
      y *= t;
      z *= t;
   }

   public static Point3 scale(Point3 point, float t) {
      return new Point3(t * point.x, t * point.y, t * point.z);
   }

   public String toString() {
      return "(" + x + ", " + y + ", " + z + ")";
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Point3))
         return false;

      Point3 rhs = (Point3) obj;

      return (x == rhs.x && y == rhs.y && z == rhs.z);
   }
}