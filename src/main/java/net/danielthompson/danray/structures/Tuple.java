package net.danielthompson.danray.structures;

/**
 * User: daniel
 * Date: 7/2/13
 * Time: 20:37
 */
public class Tuple {

   public float X;
   public float Y;
   public float Z;

   public Tuple(float X, float Y, float Z) {
      this.X = X;
      this.Y = Y;
      this.Z = Z;
   }

   public float Dot(Tuple tuple) {
      return (X * tuple.X + Y * tuple.Y + Z * tuple.Z);
   }

   public static Tuple Plus(Tuple tuple1, Tuple tuple2) {
      return new Tuple(tuple1.X + tuple2.X, tuple1.Y + tuple2.Y, tuple1.Z + tuple2.Z);
   }

   public void Plus(Tuple tuple) {
      X += tuple.X;
      Y += tuple.Y;
      Z += tuple.Z;
   }

   public static Tuple Minus(Tuple tuple1, Tuple tuple2) {
      return new Tuple(tuple1.X - tuple2.X, tuple1.Y - tuple2.Y, tuple1.Z - tuple2.Z);
   }

   public void Minus(Tuple tuple) {
      X -= tuple.X;
      Y -= tuple.Y;
      Z -= tuple.Z;
   }

   public static Tuple Scale(Tuple tuple, float t) {
      return new Tuple(t * tuple.X, t * tuple.Y, t * tuple.Z);
   }

   public void Scale(float t) {
      X *= t;
      Y *= t;
      Z *= t;
   }

   public Tuple Cross(Tuple tuple) {
      return new Tuple(
            Y*tuple.Z - Z*tuple.Y,
            Z*tuple.X - X*tuple.Z,
            X*tuple.Y - Y*tuple.X
      );
   }

   public static Tuple Cross(Tuple tuple1, Tuple tuple2) {
      return new Tuple(
            tuple1.Y * tuple2.Z - tuple1.Z * tuple2.Y,
            tuple1.Z * tuple2.X - tuple1.X * tuple2.Z,
            tuple1.X * tuple2.Y - tuple1.Y * tuple2.X
      );
   }

   public void Normalize() {
      float lengthMultiplier = 1.0f / Length();
      Scale(lengthMultiplier);
   }

   public static Tuple Normalize(Tuple tuple) {
      float lengthMultiplier = 1.0f / tuple.Length();
      return new Tuple(tuple.X * lengthMultiplier, tuple.Y * lengthMultiplier, tuple.Z * lengthMultiplier);
   }

   public float Length() {
      return (float) Math.sqrt(X * X + Y * Y + Z * Z);
   }


   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Tuple))
         return false;

      Tuple rhs = (Tuple) obj;
      return (X == rhs.X && Y == rhs.Y && Z == rhs.Z);
   }

   public String toString() {
      return "X " + X + ", Y " + Y + ", Z " + Z;
   }

   public static Tuple Interpolate(Tuple tuple1, Tuple tuple2, float percentage) {
      return Tuple.Plus(tuple1, Tuple.Scale(Tuple.Minus(tuple2, tuple1), percentage));
   }
}
