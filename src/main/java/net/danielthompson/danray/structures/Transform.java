package net.danielthompson.danray.structures;

import net.danielthompson.danray.utility.FloatUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 2/1/15.
 */
public class Transform {
   public Matrix4x4 _matrix, _inverse;

   public static Transform identity = getIdentity();

   /**
    * Creates a new Transform with an identity matrix.
    */
   public Transform() {
      _matrix = new Matrix4x4();
      _inverse = new Matrix4x4();
   }

   /**
    * Creates a new Transform with the given values. Calculates the inverse.
    * @param values
    */
   public Transform(final float[][] values) {
      _matrix = new Matrix4x4(values);
      _inverse = _matrix.inverse();
   }

   /**
    * Creates a new Transform with the given matrix. Calculates the inverse.
    * @param matrix
    */
   public Transform(final Matrix4x4 matrix) {
      _matrix = matrix;
      _inverse = matrix.inverse();
   }

   /**
    * Creates a new Transform with the given matrix and its inverse.
    * @param matrix
    * @param inverse
    */
   public Transform(final Matrix4x4 matrix, final Matrix4x4 inverse) {
      _matrix = matrix;
      _inverse = inverse;
   }

   /**
    * Returns a new Transform that is the inverse of this one.
    * @return
    */
   public Transform invert() {
      final Transform inversion = new Transform(_inverse, _matrix);
      return inversion;
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Matrix4x4))
         return false;

      final Transform rhs = (Transform) obj;

      return (_matrix.equals(rhs._matrix) && _inverse.equals(rhs._inverse));
   }

   /**
    * Returns true if this is an identity transform, false otherwise.
    * @return
    */
   public boolean IsIdentity() {
      return (
            _matrix.matrix[0][0] == 1 &&
            _matrix.matrix[0][1] == 0 &&
            _matrix.matrix[0][2] == 0 &&
            _matrix.matrix[0][3] == 0 &&

            _matrix.matrix[1][0] == 0 &&
            _matrix.matrix[1][1] == 1 &&
            _matrix.matrix[1][2] == 0 &&
            _matrix.matrix[1][3] == 0 &&

            _matrix.matrix[2][0] == 0 &&
            _matrix.matrix[2][1] == 0 &&
            _matrix.matrix[2][2] == 1 &&
            _matrix.matrix[2][3] == 0 &&

            _matrix.matrix[3][0] == 0 &&
            _matrix.matrix[3][1] == 0 &&
            _matrix.matrix[3][2] == 0 &&
            _matrix.matrix[3][3] == 1);
   }

   /**
    * Returns a new Transform that represents a translation by the given Vector.
    * Should affect points but not vectors (implemented via homogenous coordinates with
    * Vectors getting the 4th coord = 0).
    * @param delta
    * @return
    */
   public static Transform translate(final Vector3 delta) {
      return translate(delta.x, delta.y, delta.z);
   }

   public static Transform translate(final float x, final float y, final float z) {
      final Matrix4x4 matrix = new Matrix4x4(
            1, 0, 0, x,
            0, 1, 0, y,
            0, 0, 1, z,
            0, 0, 0, 1);

      final Matrix4x4 inverse = new Matrix4x4(
            1, 0, 0, -x,
            0, 1, 0, -y,
            0, 0, 1, -z,
            0, 0, 0, 1);

      Transform transform = new Transform(matrix, inverse);

      return transform;
   }

   public static Transform scale(final float t) {
      return scale(t, t, t);
   }

   public static Transform scale(final float tx, final float ty, final float tz) {
      final Matrix4x4 matrix = new Matrix4x4(
            tx, 0, 0, 0,
            0, ty, 0, 0,
            0, 0, tz, 0,
            0, 0, 0, 1);

      final Matrix4x4 inverse = new Matrix4x4(
            1.f/tx, 0,    0,    0,
            0,    1.f/ty, 0,    0,
            0,    0,    1.f/tz, 0,
            0,    0,    0,    1);

      final Transform transform = new Transform(matrix, inverse);

      return transform;
   }

   public boolean hasScale() {
      final float lengthX = apply(new Vector3(1, 0, 0)).lengthSquared();
      final float lengthY = apply(new Vector3(0, 1, 0)).lengthSquared();
      float lengthZ = apply(new Vector3(0, 0, 1)).lengthSquared();

      return (lengthX < .999 || lengthX > 1.001
         || lengthY < .999 || lengthY > 1.001
         || lengthZ < .999 || lengthZ > 1.001);
   }

   public static Transform rotateX(final float angle) {

      final float radians = (float)FastMath.toRadians(angle);
      final float sin_t = (float) FastMath.sin(radians);
      final float cos_t = (float) FastMath.cos(radians);

      final Matrix4x4 matrix = new Matrix4x4(
            1,     0,      0, 0,
            0, cos_t, -sin_t, 0,
            0, sin_t, cos_t,  0,
            0,     0,     0,  1);

      final Matrix4x4 inverse = matrix.transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform rotateY(final float angle) {
      final float radians = (float)FastMath.toRadians(angle);
      final float sin_t = (float) FastMath.sin(radians);
      final float cos_t = (float) FastMath.cos(radians);

      final Matrix4x4 matrix = new Matrix4x4(
             cos_t, 0, sin_t, 0,
            0, 1, 0, 0,
            -sin_t, 0, cos_t, 0,
            0, 0, 0, 1);

      final Matrix4x4 inverse = matrix.transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform rotateZ(final float angle) {
      final float radians = (float)FastMath.toRadians(angle);
      final float sin_t = (float) FastMath.sin(radians);
      final float cos_t = (float) FastMath.cos(radians);

      final Matrix4x4 matrix = new Matrix4x4(
            cos_t, -sin_t, 0, 0,
            sin_t,  cos_t, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

      final Matrix4x4 inverse = matrix.transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform rotate(final float angle, final Vector3 axis) {
      final Vector3 a = Vector3.normalize(axis);
      final float radians = (float)FastMath.toRadians(angle);
      final float s = (float) FastMath.sin(radians);
      final float c = (float) FastMath.cos(radians);
      
      final float[][] m = new float[4][4];

      m[0] = new float[4];
      m[1] = new float[4];
      m[2] = new float[4];
      m[3] = new float[4];

      m[0][0] = a.x * a.x + (1.f - a.x * a.x) * c;
      m[0][1] = a.x * a.y * (1.f - c) - a.z * s;
      m[0][2] = a.x * a.z * (1.f - c) + a.y * s;
      m[0][3] = 0;

      m[1][0] = a.x * a.y * (1.f - c) + a.z * s;
      m[1][1] = a.y + a.y + (1.f - a.y * a.y) * c;
      m[1][2] = a.y * a.z * (1.f - c) - a.x * c;
      m[1][3] = 0;

      m[2][0] = a.x * a.z * (1.f - c) - a.y * s;
      m[2][1] = a.y * a.z * (1.f - c) + a.x * s;
      m[2][2] = a.z * a.z + (1.f - a.z * a.z) * c;
      m[2][3] = 0;

      m[3][0] = 0;
      m[3][1] = 0;
      m[3][2] = 0;
      m[3][3] = 1;

      final Matrix4x4 matrix = new Matrix4x4(m);
      final Matrix4x4 inverse = matrix.transpose();

      return new Transform(matrix, inverse);
   }
   
   public static Transform lookAt(final Point3 position, final Point3 toLookAt, final Vector3 up) {
      final float[][] m = new float[4][4];

      m[0] = new float[4];
      m[1] = new float[4];
      m[2] = new float[4];
      m[3] = new float[4];

      m[0][3] = position.x;
      m[1][3] = position.y;
      m[2][3] = position.z;
      m[3][3] = 1;

      final Vector3 dir = Vector3.minus(position, toLookAt);
      dir.normalize();

      up.normalize();
      final Vector3 left = up.cross(dir);

      if (left.length() == 0) {
         throw new IllegalArgumentException("up vector (" + up.x + ", " + up.y + ", " + up.z + ") and viewing direction " +
               "(" + dir.x + ", " + dir.y + ", " + dir.z + ") passed to lookAt are pointing in the same direction.  Using " +
               "the identity transformation.");
      }

      left.normalize();

      final Vector3 newUp = dir.cross(left);

      m[0][0] = left.x;
      m[1][0] = left.y;
      m[2][0] = left.z;
      m[3][0] = 0;

      m[0][1] = newUp.x;
      m[1][1] = newUp.y;
      m[2][1] = newUp.z;
      m[3][1] = 0;

      m[0][2] = dir.x;
      m[1][2] = dir.y;
      m[2][2] = dir.z;
      m[3][2] = 0;

      final Matrix4x4 matrix = new Matrix4x4(m);
      final Matrix4x4 inverse = matrix.inverse();

      return new Transform(inverse, matrix);
   }

   public void applyInPlace(final Point3 p) {
      final float x = p.x;
      final float y = p.y;
      final float z = p.z;

      final float newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      final float newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      final float newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      final float w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

      if (w == 1) {
         p.x = newX;
         p.y = newY;
         p.z = newZ;
      }
      else {
         float divisor = 1.f / w;
         p.x = newX * divisor;
         p.y = newY * divisor;
         p.z = newZ * divisor;
      }
   }

   public Point3 apply(final Point3 p) {
      final float x = p.x;
      final float y = p.y;
      final float z = p.z;

      final float newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      final float newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      final float newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      final float w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

      if (w == 1)
         return new Point3(newX, newY, newZ);
      else {
         final float divisor = 1.f / w;
         return new Point3(newX * divisor, newY * divisor, newZ * divisor);
      }
   }

   public Point3 apply(final Point3 p, final Vector3 error) {
      final float x = p.x;
      final float y = p.y;
      final float z = p.z;

      final float newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      final float newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      final float newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      final float w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

      final float xAbsSum = (Math.abs(_matrix.matrix[0][0] * x) + Math.abs(_matrix.matrix[0][1] * y) +
            Math.abs(_matrix.matrix[0][2] * z) + Math.abs(_matrix.matrix[0][3]));
      final float yAbsSum = (Math.abs(_matrix.matrix[1][0] * x) + Math.abs(_matrix.matrix[1][1] * y) +
            Math.abs(_matrix.matrix[1][2] * z) + Math.abs(_matrix.matrix[1][3]));
      final float zAbsSum = (Math.abs(_matrix.matrix[2][0] * x) + Math.abs(_matrix.matrix[2][1] * y) +
            Math.abs(_matrix.matrix[2][2] * z) + Math.abs(_matrix.matrix[2][3]));

      error.x = xAbsSum * Constants.Gamma3;
      error.y = yAbsSum * Constants.Gamma3;
      error.z = zAbsSum * Constants.Gamma3;

      if (w == 1)
         return new Point3(newX, newY, newZ);
      else {
         final float divisor = 1.f / w;
         return new Point3(newX * divisor, newY * divisor, newZ * divisor);
      }
   }

   public void applyInPlace(final Vector3 v) {
      final float newX = v.x * _matrix.matrix[0][0] + v.y * _matrix.matrix[0][1] + v.z * _matrix.matrix[0][2];
      final float newY = v.x * _matrix.matrix[1][0] + v.y * _matrix.matrix[1][1] + v.z * _matrix.matrix[1][2];
      final float newZ = v.x * _matrix.matrix[2][0] + v.y * _matrix.matrix[2][1] + v.z * _matrix.matrix[2][2];

      v.x = newX;
      v.y = newY;
      v.z = newZ;
   }

   public Vector3 apply(final Vector3 v) {
      final float newX = v.x * _matrix.matrix[0][0] + v.y * _matrix.matrix[0][1] + v.z * _matrix.matrix[0][2];
      final float newY = v.x * _matrix.matrix[1][0] + v.y * _matrix.matrix[1][1] + v.z * _matrix.matrix[1][2];
      final float newZ = v.x * _matrix.matrix[2][0] + v.y * _matrix.matrix[2][1] + v.z * _matrix.matrix[2][2];

      return new Vector3(newX, newY, newZ);
   }

   public void applyInPlace(final Normal n) {
      final float newX = n.x * _matrix.matrix[0][0] + n.y * _matrix.matrix[0][1] + n.z * _matrix.matrix[0][2];
      final float newY = n.x * _matrix.matrix[1][0] + n.y * _matrix.matrix[1][1] + n.z * _matrix.matrix[1][2];
      final float newZ = n.x * _matrix.matrix[2][0] + n.y * _matrix.matrix[2][1] + n.z * _matrix.matrix[2][2];

      n.x = newX;
      n.y = newY;
      n.z = newZ;
   }

   public Normal apply(final Normal n) {
      final float newX = n.x * _inverse.matrix[0][0] + n.y * _inverse.matrix[1][0] + n.z * _inverse.matrix[2][0];
      final float newY = n.x * _inverse.matrix[0][1] + n.y * _inverse.matrix[1][1] + n.z * _inverse.matrix[2][1];
      final float newZ = n.x * _inverse.matrix[0][2] + n.y * _inverse.matrix[1][2] + n.z * _inverse.matrix[2][2];

      return new Normal(newX, newY, newZ);
   }

   public void applyInPlace(final Ray r) {
      applyInPlace(r.Origin);
      applyInPlace(r.Direction);
   }

   public Ray apply(final Ray r) {
      return new Ray(apply(r.Origin), apply(r.Direction));
   }

   public Transform apply(final Transform t) {
      final Matrix4x4 matrix = _matrix.multiply(t._matrix);
      final Matrix4x4 inverse = t._inverse.multiply(_inverse);

      return new Transform(matrix, inverse);
   }

   public boolean SwapsHandedness() {
      final float det = (_matrix.matrix[0][0] * _matrix.matrix[1][1] + _matrix.matrix[2][2] +
            _matrix.matrix[0][1] * _matrix.matrix[1][2] + _matrix.matrix[2][0] +
            _matrix.matrix[0][2] * _matrix.matrix[1][0] + _matrix.matrix[2][1]) -
            (_matrix.matrix[0][2] * _matrix.matrix[1][1] + _matrix.matrix[2][0] +
             _matrix.matrix[0][1] * _matrix.matrix[1][0] + _matrix.matrix[2][2] +
             _matrix.matrix[0][0] * _matrix.matrix[1][2] + _matrix.matrix[2][1]);
      return det < 0.f;

   }

   public BoundingBox apply(final BoundingBox b) {
      final Point3 p1 = new Point3(b.point1.x, b.point1.y, b.point1.z);
      applyInPlace(p1);

      final Point3 p2 = new Point3(b.point1.x, b.point1.y, b.point2.z);
      applyInPlace(p2);

      BoundingBox transformed = new BoundingBox(p1, p2);

      final Point3 p3 = apply(new Point3(b.point1.x, b.point2.y, b.point1.z));

      transformed = BoundingBox.GetBoundingBox(transformed, p3);

      final Point3 p4 = apply(new Point3(b.point1.x, b.point2.y, b.point2.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p4);
      final Point3 p5 = apply(new Point3(b.point2.x, b.point1.y, b.point1.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p5);
      final Point3 p6 = apply(new Point3(b.point2.x, b.point1.y, b.point2.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p6);
      final Point3 p7 = apply(new Point3(b.point2.x, b.point2.y, b.point1.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p7);
      final Point3 p8 = apply(new Point3(b.point2.x, b.point2.y, b.point2.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p8);

      return transformed;
   }


   /**
    * Returns the composite Object-To-World and World-To-Object transforms of the list of transforms.
    * @param list
    * @return
    */
   public static Transform[] composite(final java.util.List<Transform> list) {
      return composite(list.toArray(new Transform[list.size()]));
   }

   public static Transform[] composite(final Transform... transforms) {
      Transform objectToWorld = new Transform();

      for (int i = 0; i < transforms.length; i++) {
         objectToWorld = objectToWorld.apply(transforms[i]);
      }

      final Transform worldToObject = new Transform(objectToWorld._inverse, objectToWorld._matrix);

      return new Transform[] {objectToWorld, worldToObject};
   }

   private static Transform getIdentity() {
      return new Transform(Matrix4x4.identity, Matrix4x4.identity);
   }
}
