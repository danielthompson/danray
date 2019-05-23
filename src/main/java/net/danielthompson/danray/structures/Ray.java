package net.danielthompson.danray.structures;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Ray {

   public Vector3 Direction; // 8 bytes
   public Point3 Origin; // 8 bytes
   public Vector3 DirectionInverse; // 8 bytes
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
   public Ray(Point3 origin, Vector3 direction) {
      MinT = Float.MAX_VALUE;;

      Origin = origin;

      float oneOverLength = (float) (1.0f / Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z));
      Direction = new Vector3(direction.x * oneOverLength, direction.y * oneOverLength, direction.z * oneOverLength);
      DirectionInverse = new Vector3(1.0f / Direction.x, 1.0f / Direction.y, 1.0f / Direction.z);
   }

   public Ray(Point3 origin, float dx, float dy, float dz) {
      MinT = Float.MAX_VALUE;;

      Origin = origin;

      float oneOverLength = (float) (1.0 / Math.sqrt(dx * dx + dy * dy + dz * dz));
      Direction = new Vector3(dx * oneOverLength, dy * oneOverLength, dz * oneOverLength);
      DirectionInverse = new Vector3(1.0f / Direction.x, 1.0f / Direction.y, 1.0f / Direction.z);
   }

   public void OffsetOriginForward(float offset) {
      Vector3 offsetV = Vector3.scale(Direction, offset);
      Point3 newOrigin = Point3.plus(Origin, offsetV);

      Origin.plus(offsetV);
   }

   public Point3 ScaleFromOrigin(float t) {
      float x = Origin.x + t * Direction.x;
      float y = Origin.y + t * Direction.y;
      float z = Origin.z + t * Direction.z;

      return new Point3(x, y, z);
   }

   public Vector3 Scale(float t) {
      return Vector3.scale(Direction, t);
   }

   public Point3 GetPointAtT(float t) {

      return Point3.plus(Origin, Vector3.scale(Direction, t));
   }

   public float GetTAtPoint(Point3 p) {
      float tX = -Float.MAX_VALUE;
      float tY = -Float.MAX_VALUE;
      float tZ = -Float.MAX_VALUE;

      if (Direction.x != 0) {
         tX = (p.x - Origin.x) / Direction.x;
         return tX;
      }

      if (Direction.y != 0) {
         tY = (p.y - Origin.y) / Direction.y;
         return tY;
      }

      if (Direction.z != 0) {
         tZ = (p.z - Origin.z) / Direction.z;
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
      Origin.x += (intersectionNormal.X + Constants.Epsilon);
      Origin.y += (intersectionNormal.Y + Constants.Epsilon);
      Origin.z += (intersectionNormal.Z + Constants.Epsilon);
   }

   public void OffsetOriginInwards(Normal intersectionNormal) {
      Origin.x -= (intersectionNormal.X + Constants.DoubleEpsilon);
      Origin.y -= (intersectionNormal.Y + Constants.DoubleEpsilon);
      Origin.z -= (intersectionNormal.Z + Constants.DoubleEpsilon);
   }
}
