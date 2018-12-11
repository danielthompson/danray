package net.danielthompson.danray.structures;

/**
 * Represents a vector that is perpendicular to a surface at some point. å
 */
public class Normal {
   public float X;
   public float Y;
   public float Z;
   
   public Normal(float x, float y, float z) {
      X = x;
      Y = y;
      Z = z;
   }

   public Normal(Vector v) {
      X = v.X;
      Y = v.Y;
      Z = v.Z;
   }

   public Normal Cross(Vector vector) {
      return new Normal(
            Y * vector.Z - Z * vector.Y,
            Z * vector.X - X * vector.Z,
            X * vector.Y - Y * vector.X);
   }


   public float Length() {
      return (float) Math.sqrt(X * X + Y * Y + Z * Z);
   }

   public void Normalize() {
      float lengthMultiplier = 1.0f / Length();
      Scale(lengthMultiplier);
   }

   public static Normal Normalize(Normal vector) {
      Normal v = new Normal(vector.X, vector.Y, vector.Z);
      v.Normalize();
      return v;
   }

   public static Normal Scale(Normal vector, float t) {
      return new Normal (vector.X * t, vector.Y * t, vector.Z * t);

   }

   public void Scale(float t) {
      X *= t;
      Y *= t;
      Z *= t;
   }

   public float Dot(Vector vector) {
      return (X * vector.X + Y * vector.Y + Z * vector.Z);
   }

   public float Dot(Normal normal) {
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
