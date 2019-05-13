package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a zero-dimensional point in space.
 */
public class Point implements Cloneable {

   public float X;
   public float Y;
   public float Z;

   public Point(float X, float Y, float Z) {
      assert !Float.isNaN(X);
      assert !Float.isNaN(Y);
      assert !Float.isNaN(Z);

      this.X = X;
      this.Y = Y;
      this.Z = Z;
   }

   public Point(float[] xyz) {
      assert !Float.isNaN(xyz[0]);
      assert !Float.isNaN(xyz[1]);
      assert !Float.isNaN(xyz[2]);

      this.X = xyz[0];
      this.Y = xyz[1];
      this.Z = xyz[2];
   }

   public Point(Point p) {
      assert !Float.isNaN(p.X);
      assert !Float.isNaN(p.Y);
      assert !Float.isNaN(p.Z);

      this.X = p.X;
      this.Y = p.Y;
      this.Z = p.Z;
   }

   public Point clone() {
      return new Point(X, Y, Z);
   }

   public float getAxis(KDAxis axis) {
      switch (axis) {
         case X:
            return X;
         case Y:
            return Y;
         default:
            return Z;
      }
   }

   public void setAxis(KDAxis axis, float value) {
      switch (axis) {
         case X:
            X = value;
            break;
         case Y:
            Y = value;
            break;
         default:
            Z = value;
            break;
      }
   }

   public Ray CreateVectorFrom(Point originPoint) {
      float xDirection = X - originPoint.X;
      float yDirection = Y - originPoint.Y;
      float zDirection = Z - originPoint.Z;

      Vector direction = new Vector(xDirection, yDirection, zDirection);
      return new Ray(originPoint, direction);
   }

   public float Dot(Point point) {
      return (X * point.X + Y * point.Y + Z * point.Z);
   }

   public Point Cross(Point point) {
      return new Point(
            Y * point.Z - Z * point.Y,
            Z * point.X - X * point.Z,
            X * point.Y - Y * point.X);
   }

   public void Minus(Point point) {
      X -= point.X;
      Y -= point.Y;
      Z -= point.Z;
   }

   public static Vector Minus(Point point1, Point point2) {
      return new Vector(point1.X - point2.X, point1.Y - point2.Y, point1.Z - point2.Z);
   }

   public static Point Minus(Point point, Vector vector) {
      return new Point(point.X - vector.X, point.Y - vector.Y, point.Z - vector.Z);
   }

   public static Point Interpolate(Point point1, Point point2, float percentage) {
      return Point.Plus(point1, Vector.Scale(Point.Minus(point2, point1), percentage));
   }

   public float SquaredDistanceBetween(Point point) {
      return (point.X - X) * (point.X - X) +
             (point.Y - Y) * (point.Y - Y) +
             (point.Z - Z) * (point.Z - Z);
   }

   public void Plus(Point point) {
      X += point.X;
      Y += point.Y;
      Z += point.Z;
   }

   public void Plus(Vector vector) {
      X += vector.X;
      Y += vector.Y;
      Z += vector.Z;
   }

   public static Point Plus(Point point1, Point point2) {
      return new Point(point1.X + point2.X, point1.Y + point2.Y, point1.Z + point2.Z);
   }

   public static Point Plus(Point point, Vector vector) {
      return new Point(point.X + vector.X, point.Y + vector.Y, point.Z + vector.Z);
   }

   public void Normalize() {
      float lengthMultiplier = 1.0f / Length();
      Scale(lengthMultiplier);
   }


   public void Scale(float t) {
      X *= t;
      Y *= t;
      Z *= t;
   }

   public static Point Scale(Point point, float t) {
      return new Point(t * point.X, t * point.Y, t * point.Z);
   }

   public float Length() {
      return (float) Math.sqrt(X * X + Y * Y + Z * Z);
   }

   public String toString() {
      return "(" + X + ", " + Y + ", " + Z + ")";
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Point))
         return false;

      Point rhs = (Point) obj;

      return (X == rhs.X && Y == rhs.Y && Z == rhs.Z);
   }


}