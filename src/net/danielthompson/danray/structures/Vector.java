package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

/**
 * Represents a direction in space.
 */
public class Vector {
   public double X;
   public double Y;
   public double Z;

   public Vector(double x, double y, double z) {
      X = x;
      Y = y;
      Z = z;
   }

   public Vector(Normal n) {
      X = n.X;
      Y = n.Y;
      Z = n.Z;
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

   public Vector Cross(Vector vector) {
      return new Vector(
            Y * vector.Z - Z * vector.Y,
            Z * vector.X - X * vector.Z,
            X * vector.Y - Y * vector.X);
   }

   public double Dot(Vector vector) {
      return (X * vector.X + Y * vector.Y + Z * vector.Z);
   }

   public double Dot(Normal normal) {
      return (X * normal.X + Y * normal.Y + Z * normal.Z);
   }


   public static Vector Scale(Vector vector, double t) {
      return new Vector (vector.X * t, vector.Y * t, vector.Z * t);

   }

   public void Scale(double t) {
      X *= t;
      Y *= t;
      Z *= t;
   }

   public static Vector Plus(Vector vector1, Vector vector2) {
      return new Vector(vector1.X + vector2.X, vector1.Y + vector2.Y, vector1.Z + vector2.Z);
   }

   public void Plus(Vector vector) {
      X += vector.X;
      Y += vector.Y;
      Z += vector.Z;
   }

   public static Vector Minus(Vector vector1, Vector vector2) {
      return new Vector(vector1.X - vector2.X, vector1.Y - vector2.Y, vector1.Z - vector2.Z);
   }

   public static Vector Minus(Point point, Vector vector) {
      return new Vector(point.X - vector.X, point.Y - vector.Y, point.Z - vector.Z);
   }

   public static Vector Minus(Point point1, Point point2) {
      return new Vector(point1.X - point2.X, point1.Y - point2.Y, point1.Z - point2.Z);
   }

   public double Length() {
      return Math.sqrt(X * X + Y * Y + Z * Z);
   }

   public double LengthSquared() { return X*X + Y*Y + Z*Z; }

   public void Normalize() {
      double lengthMultiplier = 1.0 / Length();
      Scale(lengthMultiplier);
   }

   public static Vector Normalize(Vector vector) {
      Vector v = new Vector(vector.X, vector.Y, vector.Z);
      v.Normalize();
      return v;
   }

   public static Vector Interpolate(Vector vector1, Vector vector2, double percentage) {
      return Vector.Plus(vector1, Vector.Scale(Vector.Minus(vector2, vector1), percentage));
   }

   public String toString() {
      return "X " + X + ", Y " + Y + ", Z " + Z;
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Vector))
         return false;

      Vector rhs = (Vector) obj;

      return (X == rhs.X && Y == rhs.Y && Z == rhs.Z);
   }

}
