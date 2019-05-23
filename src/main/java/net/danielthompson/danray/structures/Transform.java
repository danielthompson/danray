package net.danielthompson.danray.structures;

import net.danielthompson.danray.utility.FloatUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 2/1/15.
 */
public class Transform {
   public Matrix4x4 _matrix, _inverse;

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
   public Transform(float[][] values) {
      _matrix = new Matrix4x4(values);
      _inverse = _matrix.Inverse();
   }

   /**
    * Creates a new Transform with the given matrix. Calculates the inverse.
    * @param matrix
    */
   public Transform(Matrix4x4 matrix) {
      _matrix = matrix;
      _inverse = matrix.Inverse();
   }

   /**
    * Creates a new Transform with the given matrix and its inverse.
    * @param matrix
    * @param inverse
    */
   public Transform(Matrix4x4 matrix, Matrix4x4 inverse) {
      _matrix = matrix;
      _inverse = inverse;
   }

   /**
    * Returns a new Transform that is the inverse of this one.
    * @return
    */
   public Transform Invert() {
      Transform inversion = new Transform(_inverse, _matrix);
      return inversion;
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Matrix4x4))
         return false;

      Transform rhs = (Transform) obj;

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
    * Vectors getting the 4th coord = 0.
    * @param delta
    * @return
    */
   public static Transform Translate(Vector3 delta) {
      return Translate(delta.x, delta.y, delta.z);
   }

   public static Transform Translate(float x, float y, float z) {
      Matrix4x4 matrix = new Matrix4x4(
            1, 0, 0, x,
            0, 1, 0, y,
            0, 0, 1, z,
            0, 0, 0, 1);

      Matrix4x4 inverse = new Matrix4x4(
            1, 0, 0, -x,
            0, 1, 0, -y,
            0, 0, 1, -z,
            0, 0, 0, 1);

      Transform transform = new Transform(matrix, inverse);

      return transform;
   }

   public static Transform Scale(float n) {
      return Scale(n, n, n);
   }

   public static Transform Scale(float x, float y, float z) {
      Matrix4x4 matrix = new Matrix4x4(
            x, 0, 0, 0,
            0, y, 0, 0,
            0, 0, z, 0,
            0, 0, 0, 1);

      Matrix4x4 inverse = new Matrix4x4(
            1.f/x, 0,    0,    0,
            0,    1.f/y, 0,    0,
            0,    0,    1.f/z, 0,
            0,    0,    0,    1);

      Transform transform = new Transform(matrix, inverse);

      return transform;
   }

   public boolean HasScale() {
      float lengthX = Apply(new Vector3(1, 0, 0)).lengthSquared();
      float lengthY = Apply(new Vector3(0, 1, 0)).lengthSquared();
      float lengthZ = Apply(new Vector3(0, 0, 1)).lengthSquared();

      return (lengthX < .999 || lengthX > 1.001
         || lengthY < .999 || lengthY > 1.001
         || lengthZ < .999 || lengthZ > 1.001);
   }

   public static Transform RotateX(float angle) {
      float sin_t = (float) FastMath.sin(FastMath.toRadians(angle));
      float cos_t = (float) FastMath.cos(FastMath.toRadians(angle));

      Matrix4x4 matrix = new Matrix4x4(
            1,     0,      0, 0,
            0, cos_t, -sin_t, 0,
            0, sin_t, cos_t,  0,
            0,     0,     0,  1);

      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform RotateY(float angle) {
      float sin_t = (float) FastMath.sin(FastMath.toRadians(angle));
      float cos_t = (float) FastMath.cos(FastMath.toRadians(angle));

      Matrix4x4 matrix = new Matrix4x4(
             cos_t, 0, sin_t, 0,
                 0, 1,     0, 0,
            -sin_t, 0, cos_t, 0,
                 0, 0,     0, 1);

      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);
   }


   public static Transform RotateZ(float angle) {
      float sin_t = (float) FastMath.sin(FastMath.toRadians(angle));
      float cos_t = (float) FastMath.cos(FastMath.toRadians(angle));

      Matrix4x4 matrix = new Matrix4x4(
            cos_t, -sin_t, 0, 0,
            sin_t,  cos_t, 0, 0,
                0,      0, 1, 0,
                0,      0, 0, 1);

      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform Rotate(float angle, Vector3 axis) {
      Vector3 a = Vector3.normalize(axis);
      float s = (float) FastMath.sin(FastMath.toRadians(angle));
      float c = (float) FastMath.cos(FastMath.toRadians(angle));
      
      float[][] m = new float[4][4];

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

      Matrix4x4 matrix = new Matrix4x4(m);
      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);

   }
   
   public static Transform LookAt(Point3 position, Point3 toLookAt, Vector3 up) {
      float[][] m = new float[4][4];

      m[0] = new float[4];
      m[1] = new float[4];
      m[2] = new float[4];
      m[3] = new float[4];

      m[0][3] = position.x;
      m[1][3] = position.y;
      m[2][3] = position.z;
      m[3][3] = 1;

      Vector3 dir = Vector3.minus(position, toLookAt);
      dir.normalize();

      up.normalize();
      Vector3 left = up.cross(dir);

      if (left.length() == 0) {
         throw new IllegalArgumentException("up vector (" + up.x + ", " + up.y + ", " + up.z + ") and viewing direction " +
               "(" + dir.x + ", " + dir.y + ", " + dir.z + ") passed to LookAt are pointing in the same direction.  Using " +
               "the identity transformation.");
      }

      left.normalize();

      Vector3 newUp = dir.cross(left);

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

      Matrix4x4 matrix = new Matrix4x4(m);
      Matrix4x4 inverse = matrix.Inverse();

      return new Transform(inverse, matrix);
   }

   public void ApplyInPlace(Point3 p) {

      float x = p.x;
      float y = p.y;
      float z = p.z;

      float newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      float newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      float newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      float w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

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

   public Point3 Apply(Point3 p) {

      float x = p.x;
      float y = p.y;
      float z = p.z;

      float newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      float newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      float newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      float w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

      if (w == 1)
         return new Point3(newX, newY, newZ);
      else {
         float divisor = 1.f / w;
         return new Point3(newX * divisor, newY * divisor, newZ * divisor);
      }
   }

   public Point3 Apply(Point3 p, Vector3 error) {

      float x = p.x;
      float y = p.y;
      float z = p.z;

      float newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      float newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      float newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      float w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

      float xAbsSum = (Math.abs(_matrix.matrix[0][0] * x) + Math.abs(_matrix.matrix[0][1] * y) +
            Math.abs(_matrix.matrix[0][2] * z) + Math.abs(_matrix.matrix[0][3]));
      float yAbsSum = (Math.abs(_matrix.matrix[1][0] * x) + Math.abs(_matrix.matrix[1][1] * y) +
            Math.abs(_matrix.matrix[1][2] * z) + Math.abs(_matrix.matrix[1][3]));
      float zAbsSum = (Math.abs(_matrix.matrix[2][0] * x) + Math.abs(_matrix.matrix[2][1] * y) +
            Math.abs(_matrix.matrix[2][2] * z) + Math.abs(_matrix.matrix[2][3]));

      error.x = xAbsSum * FloatUtils.gamma(3);
      error.y = yAbsSum * FloatUtils.gamma(3);;
      error.z = zAbsSum * FloatUtils.gamma(3);;

      if (w == 1)
         return new Point3(newX, newY, newZ);
      else {
         float divisor = 1.f / w;
         return new Point3(newX * divisor, newY * divisor, newZ * divisor);
      }
   }

   public void ApplyInPlace(Vector3 v) {
      float newX = v.x * _matrix.matrix[0][0] + v.y * _matrix.matrix[0][1] + v.z * _matrix.matrix[0][2];
      float newY = v.x * _matrix.matrix[1][0] + v.y * _matrix.matrix[1][1] + v.z * _matrix.matrix[1][2];
      float newZ = v.x * _matrix.matrix[2][0] + v.y * _matrix.matrix[2][1] + v.z * _matrix.matrix[2][2];

      v.x = newX;
      v.y = newY;
      v.z = newZ;
   }

   public Vector3 Apply(Vector3 v) {
      float newX = v.x * _matrix.matrix[0][0] + v.y * _matrix.matrix[0][1] + v.z * _matrix.matrix[0][2];
      float newY = v.x * _matrix.matrix[1][0] + v.y * _matrix.matrix[1][1] + v.z * _matrix.matrix[1][2];
      float newZ = v.x * _matrix.matrix[2][0] + v.y * _matrix.matrix[2][1] + v.z * _matrix.matrix[2][2];

      return new Vector3(newX, newY, newZ);
   }

   public void ApplyInPlace(Normal n) {
      float newX = n.X * _matrix.matrix[0][0] + n.Y * _matrix.matrix[0][1] + n.Z * _matrix.matrix[0][2];
      float newY = n.X * _matrix.matrix[1][0] + n.Y * _matrix.matrix[1][1] + n.Z * _matrix.matrix[1][2];
      float newZ = n.X * _matrix.matrix[2][0] + n.Y * _matrix.matrix[2][1] + n.Z * _matrix.matrix[2][2];

      n.X = newX;
      n.Y = newY;
      n.Z = newZ;
   }

   public Normal Apply(Normal n) {

      float newX = n.X * _inverse.matrix[0][0] + n.Y * _inverse.matrix[1][0] + n.Z * _inverse.matrix[2][0];
      float newY = n.X * _inverse.matrix[0][1] + n.Y * _inverse.matrix[1][1] + n.Z * _inverse.matrix[2][1];
      float newZ = n.X * _inverse.matrix[0][2] + n.Y * _inverse.matrix[1][2] + n.Z * _inverse.matrix[2][2];

      return new Normal(newX, newY, newZ);
   }

   public void ApplyInPlace(Ray r) {
      ApplyInPlace(r.Origin);
      ApplyInPlace(r.Direction);
   }

   public Ray Apply(Ray r) {

      return new Ray(Apply(r.Origin), Apply(r.Direction));
   }

   public Transform Apply(Transform t) {
      Matrix4x4 matrix = _matrix.Multiply(t._matrix);
      Matrix4x4 inverse = t._inverse.Multiply(_inverse);

      return new Transform(matrix, inverse);
   }

   public boolean SwapsHandedness() {
      float det = (_matrix.matrix[0][0] * _matrix.matrix[1][1] + _matrix.matrix[2][2] +
            _matrix.matrix[0][1] * _matrix.matrix[1][2] + _matrix.matrix[2][0] +
            _matrix.matrix[0][2] * _matrix.matrix[1][0] + _matrix.matrix[2][1]) -
            (_matrix.matrix[0][2] * _matrix.matrix[1][1] + _matrix.matrix[2][0] +
             _matrix.matrix[0][1] * _matrix.matrix[1][0] + _matrix.matrix[2][2] +
             _matrix.matrix[0][0] * _matrix.matrix[1][2] + _matrix.matrix[2][1]);
      return det < 0.f;

   }

   public BoundingBox Apply(BoundingBox b) {
      Point3 p1 = new Point3(b.point1.x, b.point1.y, b.point1.z);
      ApplyInPlace(p1);

      Point3 p2 = new Point3(b.point1.x, b.point1.y, b.point2.z);
      ApplyInPlace(p2);

      BoundingBox transformed = new BoundingBox(p1, p2);

      Point3 p3 = Apply(new Point3(b.point1.x, b.point2.y, b.point1.z));

      transformed = BoundingBox.GetBoundingBox(transformed, p3);

      Point3 p4 = Apply(new Point3(b.point1.x, b.point2.y, b.point2.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p4);
      Point3 p5 = Apply(new Point3(b.point2.x, b.point1.y, b.point1.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p5);
      Point3 p6 = Apply(new Point3(b.point2.x, b.point1.y, b.point2.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p6);
      Point3 p7 = Apply(new Point3(b.point2.x, b.point2.y, b.point1.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p7);
      Point3 p8 = Apply(new Point3(b.point2.x, b.point2.y, b.point2.z));
      transformed = BoundingBox.GetBoundingBox(transformed, p8);

      return transformed;
   }


   /**
    * Returns the composite Object-To-World and World-To-Object transforms of the list of transforms.
    * @param list
    * @return
    */
   public static Transform[] composite(java.util.List<Transform> list) {
      return composite(list.toArray(new Transform[list.size()]));
   }

   public static Transform[] composite(Transform... transforms) {
      Transform objectToWorld = new Transform();

      for (int i = 0; i < transforms.length; i++) {
         objectToWorld = objectToWorld.Apply(transforms[i]);
      }

      Transform worldToObject = new Transform(objectToWorld._inverse, objectToWorld._matrix);

      return new Transform[] {objectToWorld, worldToObject};
   }

   private static Transform getIdentity() {
      return new Transform(Matrix4x4.identity, Matrix4x4.identity);
   }

   public static Transform identity = getIdentity();

}
