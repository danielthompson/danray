package net.danielthompson.danray.structures;

import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 2/1/15.
 */
public class Transform {
   private Matrix4x4 _matrix, _inverse;

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
   public Transform(double[][] values) {
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
   public static Transform Translate(Vector delta) {
      Matrix4x4 matrix = new Matrix4x4(
            1, 0, 0, delta.X,
            0, 1, 0, delta.Y,
            0, 0, 1, delta.Z,
            0, 0, 0, 1);

      Matrix4x4 inverse = new Matrix4x4(
            1, 0, 0, -delta.X,
            0, 1, 0, -delta.Y,
            0, 0, 1, -delta.Z,
            0, 0, 0, 1);

      Transform transform = new Transform(matrix, inverse);

      return transform;
   }

   public static Transform Scale(double x, double y, double z) {
      Matrix4x4 matrix = new Matrix4x4(
            x, 0, 0, 0,
            0, y, 0, 0,
            0, 0, z, 0,
            0, 0, 0, 1);

      Matrix4x4 inverse = new Matrix4x4(
            1./x, 0,    0,    0,
            0,    1./y, 0,    0,
            0,    0,    1./z, 0,
            0,    0,    0,    1);

      Transform transform = new Transform(matrix, inverse);

      return transform;
   }

   public boolean HasScale() {
      double lengthX = Apply(new Vector(1, 0, 0)).LengthSquared();
      double lengthY = Apply(new Vector(0, 1, 0)).LengthSquared();
      double lengthZ = Apply(new Vector(0, 0, 1)).LengthSquared();

      return (lengthX < .999 || lengthX > 1.001
         || lengthY < .999 || lengthY > 1.001
         || lengthZ < .999 || lengthZ > 1.001);
   }

   public static Transform RotateX(double angle) {
      double sin_t = FastMath.sin(FastMath.toRadians(angle));
      double cos_t = FastMath.cos(FastMath.toRadians(angle));

      Matrix4x4 matrix = new Matrix4x4(
            1,     0,      0, 0,
            0, cos_t, -sin_t, 0,
            0, sin_t, cos_t,  0,
            0,     0,     0,  1);

      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform RotateY(double angle) {
      double sin_t = FastMath.sin(FastMath.toRadians(angle));
      double cos_t = FastMath.cos(FastMath.toRadians(angle));

      Matrix4x4 matrix = new Matrix4x4(
             cos_t, 0, sin_t, 0,
                 0, 1,     0, 0,
            -sin_t, 0, cos_t, 0,
                 0, 0,     0, 1);

      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);
   }


   public static Transform RotateZ(double angle) {
      double sin_t = FastMath.sin(FastMath.toRadians(angle));
      double cos_t = FastMath.cos(FastMath.toRadians(angle));

      Matrix4x4 matrix = new Matrix4x4(
            cos_t, -sin_t, 0, 0,
            sin_t,  cos_t, 0, 0,
                0,      0, 1, 0,
                0,      0, 0, 1);

      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);
   }

   public static Transform Rotate(double angle, Vector axis) {
      Vector a = Vector.Normalize(axis);
      double s = FastMath.sin(FastMath.toRadians(angle));
      double c = FastMath.cos(FastMath.toRadians(angle));
      
      double[][] m = new double[4][4];

      m[0] = new double[4];
      m[1] = new double[4];
      m[2] = new double[4];
      m[3] = new double[4];

      m[0][0] = a.X * a.X + (1. - a.X * a.X) * c;
      m[0][1] = a.X * a.Y * (1. - c) - a.Z * s;
      m[0][2] = a.X * a.Z * (1. - c) + a.Y * s;
      m[0][3] = 0;

      m[1][0] = a.X * a.Y * (1. - c) + a.Z * s;
      m[1][1] = a.Y + a.Y + (1. - a.Y * a.Y) * c;
      m[1][2] = a.Y * a.Z * (1. - c) - a.X * c;
      m[1][3] = 0;

      m[2][0] = a.X * a.Z * (1. - c) - a.Y * s;
      m[2][1] = a.Y * a.Z * (1. - c) + a.X * s;
      m[2][2] = a.Z * a.Z + (1. - a.Z * a.Z) * c;
      m[2][3] = 0;

      m[3][0] = 0;
      m[3][1] = 0;
      m[3][2] = 0;
      m[3][3] = 1;

      Matrix4x4 matrix = new Matrix4x4(m);
      Matrix4x4 inverse = matrix.Transpose();

      return new Transform(matrix, inverse);

   }
   
   public static Transform LookAt(Point position, Point toLookAt, Vector up) {
      double[][] m = new double[4][4];

      m[0] = new double[4];
      m[1] = new double[4];
      m[2] = new double[4];
      m[3] = new double[4];

      m[0][3] = position.X;
      m[1][3] = position.Y;
      m[2][3] = position.Z;
      m[3][3] = 1;

      Vector dir = Vector.Minus(position, toLookAt);
      dir.Normalize();

      up.Normalize();
      Vector left = up.Cross(dir);

      if (left.Length() == 0) {
         throw new IllegalArgumentException("up vector (" + up.X + ", " + up.Y + ", " + up.Z + ") and viewing direction " +
               "(" + dir.X + ", " + dir.Y + ", " + dir.Z + ") passed to LookAt are pointing in the same direction.  Using " +
               "the identity transformation.");
      }

      left.Normalize();

      Vector newUp = dir.Cross(left);

      m[0][0] = left.X;
      m[1][0] = left.Y;
      m[2][0] = left.Z;
      m[3][0] = 0;

      m[0][1] = newUp.X;
      m[1][1] = newUp.Y;
      m[2][1] = newUp.Z;
      m[3][1] = 0;

      m[0][2] = dir.X;
      m[1][2] = dir.Y;
      m[2][2] = dir.Z;
      m[3][2] = 0;

      Matrix4x4 matrix = new Matrix4x4(m);
      Matrix4x4 inverse = matrix.Inverse();

      return new Transform(inverse, matrix);
   }

   public Point Apply(Point p) {
      if (p == null) {
         double temp = 0.0;
      }

      double x = p.X;
      double y = p.Y;
      double z = p.Z;

      double newX = x * _matrix.matrix[0][0] + y * _matrix.matrix[0][1] + z * _matrix.matrix[0][2] + _matrix.matrix[0][3];
      double newY = x * _matrix.matrix[1][0] + y * _matrix.matrix[1][1] + z * _matrix.matrix[1][2] + _matrix.matrix[1][3];
      double newZ = x * _matrix.matrix[2][0] + y * _matrix.matrix[2][1] + z * _matrix.matrix[2][2] + _matrix.matrix[2][3];

      double w = x * _matrix.matrix[3][0] + y * _matrix.matrix[3][1] + z * _matrix.matrix[3][2] + _matrix.matrix[3][3];

      if (w == 1)
         return new Point(newX, newY, newZ);
      else {
         double divisor = 1. / w;
         return new Point(newX * divisor, newY * divisor, newZ * divisor);
      }
   }

   public Vector Apply(Vector v) {
      double newX = v.X * _matrix.matrix[0][0] + v.Y * _matrix.matrix[0][1] + v.Z * _matrix.matrix[0][2];
      double newY = v.X * _matrix.matrix[1][0] + v.Y * _matrix.matrix[1][1] + v.Z * _matrix.matrix[1][2];
      double newZ = v.X * _matrix.matrix[2][0] + v.Y * _matrix.matrix[2][1] + v.Z * _matrix.matrix[2][2];

      return new Vector(newX, newY, newZ);
   }

   public Normal Apply(Normal n) {
      if (n == null || _inverse == null) {
         double temp = 0.0;
      }
      double newX = n.X * _inverse.matrix[0][0] + n.Y * _inverse.matrix[1][0] + n.Z * _inverse.matrix[2][0];
      double newY = n.X * _inverse.matrix[0][1] + n.Y * _inverse.matrix[1][1] + n.Z * _inverse.matrix[2][1];
      double newZ = n.X * _inverse.matrix[0][2] + n.Y * _inverse.matrix[1][2] + n.Z * _inverse.matrix[2][2];

      return new Normal(newX, newY, newZ);
   }

   public Ray Apply(Ray r) {

      return new Ray(Apply(r.Origin), Apply(r.Direction));
   }

   public Transform Apply(Transform t) {
      Matrix4x4 matrix = _matrix.Multiply(t._matrix);
      Matrix4x4 inverse = _inverse.Multiply(t._inverse);

      return new Transform(matrix, inverse);
   }

   public boolean SwapsHandedness() {
      double det = (_matrix.matrix[0][0] * _matrix.matrix[1][1] + _matrix.matrix[2][2] +
            _matrix.matrix[0][1] * _matrix.matrix[1][2] + _matrix.matrix[2][0] +
            _matrix.matrix[0][2] * _matrix.matrix[1][0] + _matrix.matrix[2][1]) -
            (_matrix.matrix[0][2] * _matrix.matrix[1][1] + _matrix.matrix[2][0] +
             _matrix.matrix[0][1] * _matrix.matrix[1][0] + _matrix.matrix[2][2] +
             _matrix.matrix[0][0] * _matrix.matrix[1][2] + _matrix.matrix[2][1]);
      return det < 0.;

   }

   public BoundingBox Apply(BoundingBox b) {
      Point p1 = Apply(new Point(b.point1.X, b.point1.Y, b.point1.Z));
      Point p2 = Apply(new Point(b.point1.X, b.point1.Y, b.point2.Z));

      BoundingBox transformed = new BoundingBox(p1, p2);

      Point p3 = Apply(new Point(b.point1.X, b.point2.Y, b.point1.Z));

      transformed = BoundingBox.GetBoundingBox(transformed, p3);

      Point p4 = Apply(new Point(b.point1.X, b.point2.Y, b.point2.Z));
      transformed = BoundingBox.GetBoundingBox(transformed, p4);
      Point p5 = Apply(new Point(b.point2.X, b.point1.Y, b.point1.Z));
      transformed = BoundingBox.GetBoundingBox(transformed, p5);
      Point p6 = Apply(new Point(b.point2.X, b.point1.Y, b.point2.Z));
      transformed = BoundingBox.GetBoundingBox(transformed, p6);
      Point p7 = Apply(new Point(b.point2.X, b.point2.Y, b.point1.Z));
      transformed = BoundingBox.GetBoundingBox(transformed, p7);
      Point p8 = Apply(new Point(b.point2.X, b.point2.Y, b.point2.Z));
      transformed = BoundingBox.GetBoundingBox(transformed, p8);

      return transformed;
   }
}
