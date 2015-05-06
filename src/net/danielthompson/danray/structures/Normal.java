package net.danielthompson.danray.structures;

/**
 * Represents a vector that is perpendicular to a surface at some point. å
 */
public class Normal {
   public double X;
   public double Y;
   public double Z;
   
   public Normal(double x, double y, double z) {
      X = x;
      Y = y;
      Z = z;
   }

   public Normal(Vector v) {
      X = v.X;
      Y = v.Y;
      Z = v.Z;
   }

   public double Length() {
      return Math.sqrt(X * X + Y * Y + Z * Z);
   }

   public void Normalize() {
      double lengthMultiplier = 1.0 / Length();
      Scale(lengthMultiplier);
   }

   public static Normal Normalize(Normal vector) {
      Normal v = new Normal(vector.X, vector.Y, vector.Z);
      v.Normalize();
      return v;
   }

   public static Normal Scale(Normal vector, double t) {
      return new Normal (vector.X * t, vector.Y * t, vector.Z * t);

   }

   public void Scale(double t) {
      X *= t;
      Y *= t;
      Z *= t;
   }

   public double Dot(Vector vector) {
      return (X * vector.X + Y * vector.Y + Z * vector.Z);
   }

   public double Dot(Normal normal) {
      return (X * normal.X + Y * normal.Y + Z * normal.Z);
   }


   public String toString() {
      return "X " + X + ", Y " + Y + ", Z " + Z;
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Normal))
         return false;

      Normal rhs = (Normal) obj;

      return (X == rhs.X && Y == rhs.Y && Z == rhs.Z);
   }

}
