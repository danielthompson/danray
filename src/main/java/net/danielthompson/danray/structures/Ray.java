package net.danielthompson.danray.structures;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Ray {

   public Vector Direction; // 8 bytes
   public Point Origin; // 8 bytes
   public Vector DirectionInverse; // 8 bytes
   public boolean FlipNormals = false;

   /**
    * The t-value for the closest intersection found so far.
    * Should be set only by Shape hit() routines.
    */
   public float MinT = Float.MAX_VALUE; // 4 bytes

   /**
    * The maximum t value that an intersection should return true for.
    * Should be set only by the acceleration structure during traversal.
    * Shape hit() routines should compare against this, but should not change it.
    */
   public float MaxT = Float.MAX_VALUE;

   /**
    * Creates a new Ray object.
    * @param origin The origin of the vector.
    * @param direction The absolute direction of the vector (i.e. not relative to the origin). Will be normalized.
    */
   public Ray(Point origin, Vector direction) {
      MinT = Float.MAX_VALUE;;

      Origin = origin;

      float oneOverLength = (float) (1.0f / Math.sqrt(direction.X * direction.X + direction.Y * direction.Y + direction.Z * direction.Z));
      Direction = new Vector(direction.X * oneOverLength, direction.Y * oneOverLength, direction.Z * oneOverLength);
      DirectionInverse = new Vector(1.0f / Direction.X, 1.0f / Direction.Y, 1.0f / Direction.Z);
   }

   public Ray(Point origin, float dx, float dy, float dz) {
      MinT = Float.MAX_VALUE;;

      Origin = origin;

      float oneOverLength = (float) (1.0 / Math.sqrt(dx * dx + dy * dy + dz * dz));
      Direction = new Vector(dx * oneOverLength, dy * oneOverLength, dz * oneOverLength);
      DirectionInverse = new Vector(1.0f / Direction.X, 1.0f / Direction.Y, 1.0f / Direction.Z);
   }

   public void OffsetOriginForward(float offset) {
      Vector offsetV = Vector.Scale(Direction, offset);
      Point newOrigin = Point.Plus(Origin, offsetV);

      Origin.Plus(offsetV);
   }

   public Point ScaleFromOrigin(float t) {
      float x = Origin.X + t * Direction.X;
      float y = Origin.Y + t * Direction.Y;
      float z = Origin.Z + t * Direction.Z;

      return new Point(x, y, z);
   }

   public Vector Scale(float t) {
      return Vector.Scale(Direction, t);
   }

   public Point GetPointAtT(float t) {

      return Point.Plus(Origin, Vector.Scale(Direction, t));
   }

   public float GetTAtPoint(Point p) {
      float tX = -Float.MAX_VALUE;
      float tY = -Float.MAX_VALUE;
      float tZ = -Float.MAX_VALUE;

      if (Direction.X != 0) {
         tX = (p.X - Origin.X) / Direction.X;
         return tX;
      }

      if (Direction.Y != 0) {
         tY = (p.Y - Origin.Y) / Direction.Y;
         return tY;
      }

      if (Direction.Z != 0) {
         tZ = (p.Z - Origin.Z) / Direction.Z;
         return tZ;
      }

      throw new IllegalStateException("can't compute point at t; ray's direction components are all zero");
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Ray))
         return false;

      Ray rhs = (Ray) obj;

      return (Direction.equals(rhs.Direction) && Origin.equals(rhs.Origin));
   }

   public String toString() {
      return "Origin: " + Origin + ", Direction: " + Direction;
   }

   public void OffsetOriginOutwards(Normal intersectionNormal) {
      Origin.X += (intersectionNormal.X + Constants.Epsilon);
      Origin.Y += (intersectionNormal.Y + Constants.Epsilon);
      Origin.Z += (intersectionNormal.Z + Constants.Epsilon);
   }

   public void OffsetOriginInwards(Normal intersectionNormal) {
      Origin.X -= (intersectionNormal.X + Constants.DoubleEpsilon);
      Origin.Y -= (intersectionNormal.Y + Constants.DoubleEpsilon);
      Origin.Z -= (intersectionNormal.Z + Constants.DoubleEpsilon);
   }
}
