package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a direction in space.
 */
public class Vector {

   public static AtomicLong instances = new AtomicLong();

   public float X;
   public float Y;
   public float Z;

   public Vector(float x, float y, float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      X = x;
      Y = y;
      Z = z;
      instances.incrementAndGet();
   }

   public Vector(float[] xyz) {
      assert !Float.isNaN(xyz[0]);
      assert !Float.isNaN(xyz[1]);
      assert !Float.isNaN(xyz[2]);

      X = xyz[0];
      Y = xyz[1];
      Z = xyz[2];
      instances.incrementAndGet();
   }

   public Vector(Normal n) {
      assert !Float.isNaN(n.X);
      assert !Float.isNaN(n.Y);
      assert !Float.isNaN(n.Z);

      X = n.X;
      Y = n.Y;
      Z = n.Z;
      instances.incrementAndGet();
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

   public Vector Cross(Vector vector) {
      return new Vector(
            Y * vector.Z - Z * vector.Y,
            Z * vector.X - X * vector.Z,
            X * vector.Y - Y * vector.X);
   }

   public Vector Cross(Normal normal) {
      return new Vector(
            Y * normal.Z - Z * normal.Y,
            Z * normal.X - X * normal.Z,
            X * normal.Y - Y * normal.X);
   }

   public float Dot(Vector vector) {
      return (X * vector.X + Y * vector.Y + Z * vector.Z);
   }

   public float Dot(Normal normal) {
      return (X * normal.X + Y * normal.Y + Z * normal.Z);
   }


   public static Vector Scale(Vector vector, float t) {
      return new Vector (vector.X * t, vector.Y * t, vector.Z * t);

   }

   public void Scale(float t) {
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

   public float Length() {
      return (float) Math.sqrt(X * X + Y * Y + Z * Z);
   }

   public float LengthSquared() { return X*X + Y*Y + Z*Z; }

   public void Normalize() {
      float lengthMultiplier = 1.0f / Length();
      Scale(lengthMultiplier);
   }

   public static Vector Normalize(Vector vector) {
      Vector v = new Vector(vector.X, vector.Y, vector.Z);
      v.Normalize();
      return v;
   }

   public static Vector Interpolate(Vector vector1, Vector vector2, float percentage) {
      return Vector.Plus(vector1, Vector.Scale(Vector.Minus(vector2, vector1), percentage));
   }

   public static Vector Lerp(Vector v1, float w1, Vector v2, float w2) {

      float x = v1.X * w1 + v2.X * w2;
      float y = v1.Y * w1 + v2.Y * w2;
      float z = v1.Z * w1 + v2.Z * w2;

      Vector v = new Vector(x, y, z);
      v.Normalize();
      return v;
   }

   public static Vector Slerp(Vector v1, float w1, Vector v2, float w2) {

      float cosOmega = v1.Dot(v2);
      float omega = (float)Math.acos(cosOmega);

      float oneOverSinOmega = (float)Math.sin(omega);

      float ww1 = (float)Math.sin(w1 * omega) * oneOverSinOmega;
      float ww2 = (float)Math.sin(w2 * omega) * oneOverSinOmega;


      float x = v1.X * ww1 + v2.X * ww2;
      float y = v1.Y * ww1 + v2.Y * ww2;
      float z = v1.Z * ww1 + v2.Z * ww2;

      Vector v = new Vector(x, y, z);
      v.Normalize();
      return v;
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
