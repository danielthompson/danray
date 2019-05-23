package net.danielthompson.danray.structures;

/**
 * Represents a vector that is perpendicular to a surface at some point. å
 */
public class Normal {
   public float x;
   public float y;
   public float z;
   
   public Normal(float x, float y, float z) {
      assert !Float.isNaN(x);
      assert !Float.isNaN(y);
      assert !Float.isNaN(z);

      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Normal(Vector3 n) {
      assert !Float.isNaN(n.x);
      assert !Float.isNaN(n.y);
      assert !Float.isNaN(n.z);

      x = n.x;
      y = n.y;
      z = n.z;
   }

   public Normal Cross(Vector3 vector) {
      return new Normal(
            y * vector.z - z * vector.y,
            z * vector.x - x * vector.z,
            x * vector.y - y * vector.x);
   }

   public float Length() {
      return (float) Math.sqrt(x * x + y * y + z * z);
   }

   public void Normalize() {
      float lengthMultiplier = 1.0f / Length();
      Scale(lengthMultiplier);
   }

   public static Normal Normalize(Normal normal) {
      Normal n = new Normal(normal.x, normal.y, normal.z);
      n.Normalize();
      return n;
   }

   public static Normal Scale(Normal vector, float t) {
      return new Normal(vector.x * t, vector.y * t, vector.z * t);
   }

   public void Scale(float t) {
      x *= t;
      y *= t;
      z *= t;
   }

   public float Dot(Vector3 vector) {
      return (x * vector.x + y * vector.y + z * vector.z);
   }

   public float Dot(Normal normal) {
      return (x * normal.x + y * normal.y + z * normal.z);
   }

   public String toString() {
      return "(" + x + ", " + y + ", " + z + ")";
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Normal))
         return false;

      Normal rhs = (Normal) obj;

      return (x == rhs.x && y == rhs.y && z == rhs.z);
   }
}
