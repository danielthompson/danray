package net.danielthompson.danray.structures;

import java.util.concurrent.atomic.AtomicLong;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Ray {

   public static final AtomicLong instances = new AtomicLong();

   public Vector3 Direction; // 8 bytes
   public Point3 Origin; // 8 bytes
   public Vector3 DirectionInverse; // 8 bytes
   public boolean FlipNormals = false;

   /**
    * The t-value for the closest intersection found so far.
    * Should be set only by shape hit() routines.
    */
   public float MinT = Float.MAX_VALUE; // 4 bytes

   /**
    * The maximum t value that an intersection should return true for.
    * Should be set only by the acceleration structure during traversal.
    * shape hit() routines should compare against this, but should not change it.
    */
   public float MaxT = Float.MAX_VALUE;

   /**
    * Creates a new Ray object.
    * @param origin The origin of the vector.
    * @param direction The absolute direction of the vector (i.e. not relative to the origin). Will be normalized.
    */
   public Ray(final Point3 origin, final Vector3 direction) {
      Origin = origin;

      final float oneOverLength = (float) (1.0f / Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z));

      Direction = direction;
      Direction.x *= oneOverLength;
      Direction.y *= oneOverLength;
      Direction.z *= oneOverLength;

      DirectionInverse = new Vector3(1.0f / Direction.x, 1.0f / Direction.y, 1.0f / Direction.z);

      instances.incrementAndGet();
   }

   public Ray(final Point3 origin, final float dx, final float dy, final float dz) {
      Origin = origin;

      final float oneOverLength = (float) (1.0 / Math.sqrt(dx * dx + dy * dy + dz * dz));
      Direction = new Vector3(dx * oneOverLength, dy * oneOverLength, dz * oneOverLength);
      DirectionInverse = new Vector3(1.0f / Direction.x, 1.0f / Direction.y, 1.0f / Direction.z);

      instances.incrementAndGet();
   }

   public void offsetOriginForward(final float offset) {
      final Vector3 offsetV = Vector3.scale(Direction, offset);
      //Point3 newOrigin = Point3.plus(Origin, offsetV);

      Origin.plus(offsetV);
   }

   public Point3 scaleFromOrigin(final float t) {
      final float x = Origin.x + t * Direction.x;
      final float y = Origin.y + t * Direction.y;
      final float z = Origin.z + t * Direction.z;

      return new Point3(x, y, z);
   }

   public Vector3 scale(final float t) {
      return Vector3.scale(Direction, t);
   }

   public Point3 getPointAtT(final float t) {
      return Point3.plus(Origin, Vector3.scale(Direction, t));
   }

   public float getTAtPoint(final Point3 p) {
      final float temp;

      final float absX = Math.abs(Direction.x);
      final float absY = Math.abs(Direction.y);
      final float absZ = Math.abs(Direction.z);

      if (absX >= absY && absX >= absZ) {
         temp = (p.x - Origin.x) / Direction.x;
         return temp;
      }

      if (absY >= absX && absY >= absZ) {
         temp = (p.y - Origin.y) / Direction.y;
         return temp;
      }

      if (absZ >= absX && absZ >= absY) {
         temp = (p.z - Origin.z) / Direction.z;
         return temp;
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

      final Ray rhs = (Ray) obj;

      return (Direction.equals(rhs.Direction) && Origin.equals(rhs.Origin));
   }

   public String toString() {
      return "O: " + Origin + ", D: " + Direction;
   }

   public void offsetOriginOutwards(final Normal intersectionNormal) {
      Origin.x += (intersectionNormal.x + Constants.Epsilon);
      Origin.y += (intersectionNormal.y + Constants.Epsilon);
      Origin.z += (intersectionNormal.z + Constants.Epsilon);
   }

   public void offsetOriginInwards(final Normal intersectionNormal) {
      Origin.x -= (intersectionNormal.x + Constants.DoubleEpsilon);
      Origin.y -= (intersectionNormal.y + Constants.DoubleEpsilon);
      Origin.z -= (intersectionNormal.z + Constants.DoubleEpsilon);
   }
}
