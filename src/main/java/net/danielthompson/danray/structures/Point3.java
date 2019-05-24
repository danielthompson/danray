package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a zero-dimensional point in space.
 */
public class Point3 {

   public static final AtomicLong instances = new AtomicLong();

   public float x;
   public float y;
   public float z;

   public Point3(final float x, final float y, final float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      this.x = x;
      this.y = y;
      this.z = z;
      instances.incrementAndGet();
   }

   public Point3(final Point3 p) {
      assert !Float.isNaN(p.x);
      assert !Float.isNaN(p.y);
      assert !Float.isNaN(p.z);

      this.x = p.x;
      this.y = p.y;
      this.z = p.z;
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

   public void setAxis(final KDAxis axis, final float value) {
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

   public Ray createVectorFrom(final Point3 originPoint) {
      float xDirection = x - originPoint.x;
      float yDirection = y - originPoint.y;
      float zDirection = z - originPoint.z;

      final Vector3 direction = new Vector3(xDirection, yDirection, zDirection);
      return new Ray(originPoint, direction);
   }

   public float dot(final Point3 point) {
      return (x * point.x + y * point.y + z * point.z);
   }

   public Point3 cross(final Point3 point) {
      return new Point3(
            y * point.z - z * point.y,
            z * point.x - x * point.z,
            x * point.y - y * point.x);
   }

   public void minus(final Point3 point) {
      x -= point.x;
      y -= point.y;
      z -= point.z;
   }

   public static Vector3 minus(final Point3 p0, final Point3 p1) {
      return new Vector3(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);
   }

   public static Point3 minus(final Point3 p, final Vector3 v) {
      return new Point3(p.x - v.x, p.y - v.y, p.z - v.z);
   }

   public static Point3 lerp(final Point3 p0, final Point3 p1, final float t) {
      return Point3.plus(p0, Vector3.scale(Point3.minus(p1, p0), t));
   }

   public float squaredDistanceBetween(final Point3 p) {
      return (p.x - x) * (p.x - x) +
             (p.y - y) * (p.y - y) +
             (p.z - z) * (p.z - z);
   }

   // TODO - delete this
   public void plus(final Point3 p) {
      x += p.x;
      y += p.y;
      z += p.z;
   }

   public void plus(final Vector3 v) {
      x += v.x;
      y += v.y;
      z += v.z;
   }

   public static Point3 plus(final Point3 p, final Vector3 v) {
      return new Point3(p.x + v.x, p.y + v.y, p.z + v.z);
   }

   public void scale(final float t) {
      x *= t;
      y *= t;
      z *= t;
   }

   public static Point3 scale(final Point3 p, final float t) {
      return new Point3(t * p.x, t * p.y, t * p.z);
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

      final Point3 rhs = (Point3) obj;

      return (x == rhs.x && y == rhs.y && z == rhs.z);
   }
}