package net.danielthompson.danray.structures;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Ray {

   public Vector Direction;
   public Point Origin;

   public Vector DirectionInverse;

   public float MinT = Float.MAX_VALUE;;
   public float MaxT = Float.MAX_VALUE;

   /**
    * Creates a new Vector object.
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

   public void OffsetOrigin(Normal intersectionNormal) {
      Origin.X += (intersectionNormal.X + Constants.DoubleEpsilon);
      Origin.Y += (intersectionNormal.Y + Constants.DoubleEpsilon);
      Origin.Z += (intersectionNormal.Z + Constants.DoubleEpsilon);
   }
}
