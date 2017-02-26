package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

/**
 * Represents a zero-dimensional point in space.
 */
public class Point implements Cloneable {

   public double X;
   public double Y;
   public double Z;

   public Point(double X, double Y, double Z) {
      this.X = X;
      this.Y = Y;
      this.Z = Z;
   }

   public Point(double[] xyz) {
      this.X = xyz[0];
      this.Y = xyz[1];
      this.Z = xyz[2];
   }

   public Point(Point p) {
      this.X = p.X;
      this.Y = p.Y;
      this.Z = p.Z;
   }

   public Point clone() {
      return new Point(X, Y, Z);
   }

   public double getAxis(KDAxis axis) {
      switch (axis) {
         case X:
            return X;
         case Y:
            return Y;
         default:
            return Z;
      }
   }



   public void setAxis(KDAxis axis, double value) {
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
      double xDirection = X - originPoint.X;
      double yDirection = Y - originPoint.Y;
      double zDirection = Z - originPoint.Z;

      Vector direction = new Vector(xDirection, yDirection, zDirection);
      return new Ray(originPoint, direction);
   }

   public double Dot(Point point) {
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

   public static Point Interpolate(Point point1, Point point2, double percentage) {
      return Point.Plus(point1, Vector.Scale(Point.Minus(point2, point1), percentage));
   }

   public double SquaredDistanceBetween(Point point) {
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
      double lengthMultiplier = 1.0 / Length();
      Scale(lengthMultiplier);
   }


   public void Scale(double t) {
      X *= t;
      Y *= t;
      Z *= t;
   }

   public static Point Scale(Point point, double t) {
      return new Point(t * point.X, t * point.Y, t * point.Z);
   }

   public double Length() {
      return Math.sqrt(X * X + Y * Y + Z * Z);
   }

   public String toString() {
      return "X " + X + ", Y " + Y + ", Z " + Z;
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

   public void Rotate(Ray axis, double theta) {
      theta = Math.toRadians(theta);

      double sinTheta = Math.sin(theta);
      double cosTheta = Math.cos(theta);

      double a = axis.Origin.X;
      double b = axis.Origin.Y;
      double c = axis.Origin.Z;

      double u = axis.Direction.X;
      double v = axis.Direction.Y;
      double w = axis.Direction.Z;

      double x = X;
      double y = Y;
      double z = Z;

      double a1 = u * x - v * y - w * z;

      double newX = (a * (v * v + w * w) - u * (b * v + c * w - a1)) * (1 - cosTheta) + x * cosTheta + (-c * v + b * w - w * y + v * z) * sinTheta;
      double newY = (b * (u * u + w * w) - v * (a * u + c * w - a1)) * (1 - cosTheta) + y * cosTheta + (c * u - a * w + w * x - u * z) * sinTheta;
      double newZ = (c * (u * u + v * v) - w * (a * u + b * v - a1)) * (1 - cosTheta) + z * cosTheta + (-b * u + a * v - v * x + u * y) * sinTheta;

      X = newX;
      Y = newY;
      Z = newZ;
   }

   public static Point Rotate(Point point, Ray axis, double theta) {

      theta = Math.toRadians(theta);

      double sinTheta = Math.sin(theta);
      double cosTheta = Math.cos(theta);

      double a = axis.Origin.X;
      double b = axis.Origin.Y;
      double c = axis.Origin.Z;

      double u = axis.Direction.X;
      double v = axis.Direction.Y;
      double w = axis.Direction.Z;

      double x = point.X;
      double y = point.Y;
      double z = point.Z;

      double a1 = u * x - v * y - w * z;

      double newX = (a * (v * v + w * w) - u * (b * v + c * w - a1)) * (1 - cosTheta) + x * cosTheta + (-c * v + b * w - w * y + v * z) * sinTheta;
      double newY = (b * (u * u + w * w) - v * (a * u + c * w - a1)) * (1 - cosTheta) + y * cosTheta + (c * u - a * w + w * x - u * z) * sinTheta;
      double newZ = (c * (u * u + v * v) - w * (a * u + b * v - a1)) * (1 - cosTheta) + z * cosTheta + (-b * u + a * v - v * x + u * y) * sinTheta;

      return new Point(newX, newY, newZ);
   }

}