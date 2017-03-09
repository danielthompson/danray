package net.danielthompson.danray.structures;

/**
 * Created by daniel on 2/15/15.
 */
public class Matrix4x4 {
   public float[][] matrix;

   public Matrix4x4() {
      matrix = new float[4][4];

      matrix[0] = new float[4];
      matrix[1] = new float[4];
      matrix[2] = new float[4];
      matrix[3] = new float[4];

      matrix[0][0] = 1;
      matrix[1][1] = 1;
      matrix[2][2] = 1;
      matrix[3][3] = 1;
   }

   public Matrix4x4(float values[][]) {
      this();

      matrix[0][0] = values[0][0];
      matrix[0][1] = values[0][1];
      matrix[0][2] = values[0][2];
      matrix[0][3] = values[0][3];

      matrix[1][0] = values[1][0];
      matrix[1][1] = values[1][1];
      matrix[1][2] = values[1][2];
      matrix[1][3] = values[1][3];

      matrix[2][0] = values[2][0];
      matrix[2][1] = values[2][1];
      matrix[2][2] = values[2][2];
      matrix[2][3] = values[2][3];

      matrix[3][0] = values[3][0];
      matrix[3][1] = values[3][1];
      matrix[3][2] = values[3][2];
      matrix[3][3] = values[3][3];
   }

   public Matrix4x4(float t00, float t01, float t02, float t03,
                    float t10, float t11, float t12, float t13,
                    float t20, float t21, float t22, float t23,
                    float t30, float t31, float t32, float t33) {

      this();

      matrix[0][0] = t00;
      matrix[0][1] = t01;
      matrix[0][2] = t02;
      matrix[0][3] = t03;

      matrix[1][0] = t10;
      matrix[1][1] = t11;
      matrix[1][2] = t12;
      matrix[1][3] = t13;

      matrix[2][0] = t20;
      matrix[2][1] = t21;
      matrix[2][2] = t22;
      matrix[2][3] = t23;

      matrix[3][0] = t30;
      matrix[3][1] = t31;
      matrix[3][2] = t32;
      matrix[3][3] = t33;
   }

   public Matrix4x4 Transpose() {
      return new Matrix4x4(
            matrix[0][0], matrix[1][0], matrix[2][0], matrix[3][0],
            matrix[0][1], matrix[1][1], matrix[2][1], matrix[3][1],
            matrix[0][2], matrix[1][2], matrix[2][2], matrix[3][2],
            matrix[0][3], matrix[1][3], matrix[2][3], matrix[3][3]);
   }

   public Matrix4x4 Inverse() {
      int[] indxc = new int[4];
      int[] indxr = new int[4];
      int[] ipiv = { 0, 0, 0, 0 };
      float[][] minv = new float[4][4];
      System.arraycopy(matrix[0], 0, minv[0], 0, 4);
      System.arraycopy(matrix[1], 0, minv[1], 0, 4);
      System.arraycopy(matrix[2], 0, minv[2], 0, 4);
      System.arraycopy(matrix[3], 0, minv[3], 0, 4);
      //memcpy(minv, m, 4*4*sizeof(float));
      for (int i = 0; i < 4; i++) {
         int irow = -1, icol = -1;
         float big = 0.f;
         // Choose pivot
         for (int j = 0; j < 4; j++) {
            if (ipiv[j] != 1) {
               for (int k = 0; k < 4; k++) {
                  if (ipiv[k] == 0) {
                     if (Math.abs(minv[j][k]) >= big) {
                        big = Math.abs(minv[j][k]);
                        irow = j;
                        icol = k;
                     }
                  }
                  else if (ipiv[k] > 1)
                     throw new UnsupportedOperationException("Singular matrix in MatrixInvert");
                  }
               }
            }
         ++ipiv[icol];

         // Swap rows _irow_ and _icol_ for pivot
         if (irow != icol) {
            swap(irow, icol);

            /*for (int k = 0; k < 4; ++k)
               swap(minv[irow][k], minv[icol][k]);*/
         }
         indxr[i] = irow;
         indxc[i] = icol;
         if (minv[icol][icol] == 0.)
            throw new UnsupportedOperationException("Singular matrix in MatrixInvert");
         // Set $m[icol][icol]$ to one by scaling row _icol_ appropriately
         float pivinv = 1.f / minv[icol][icol];
         minv[icol][icol] = 1.f;
         for (int j = 0; j < 4; j++)
            minv[icol][j] *= pivinv;
         // Subtract this row from others to zero out their columns
         for (int j = 0; j < 4; j++) {
            if (j != icol) {
               float save = minv[j][icol];
               minv[j][icol] = 0;
               for (int k = 0; k < 4; k++)
                  minv[j][k] -= minv[icol][k]*save;
               }
            }
         }
      // Swap columns to reflect permutation
      for (int j = 3; j >= 0; j--) {
         if (indxr[j] != indxc[j]) {
            swap(indxr[j], indxc[j]);
            /*
            for (int k = 0; k < 4; k++)
               swap(minv[k][indxr[j]], minv[k][indxc[j]]);
            */

         }
      }
      return new Matrix4x4(minv);
   }

   public void swap(int row1, int row2) {
      float[] temp = matrix[row1];
      matrix[row1] = matrix[row2];
      matrix[row2] = temp;
   }

   public Matrix4x4 Multiply(Matrix4x4 m) {
      Matrix4x4 ret = new Matrix4x4();

      ret.matrix[0][0] = matrix[0][0] * m.matrix[0][0] + matrix[0][1] * m.matrix[1][0] + matrix[0][2] * m.matrix[2][0] + matrix[0][3] * m.matrix[3][0];
      ret.matrix[0][1] = matrix[0][0] * m.matrix[0][1] + matrix[0][1] * m.matrix[1][1] + matrix[0][2] * m.matrix[2][1] + matrix[0][3] * m.matrix[3][1];
      ret.matrix[0][2] = matrix[0][0] * m.matrix[0][2] + matrix[0][1] * m.matrix[1][2] + matrix[0][2] * m.matrix[2][2] + matrix[0][3] * m.matrix[3][2];
      ret.matrix[0][3] = matrix[0][0] * m.matrix[0][3] + matrix[0][1] * m.matrix[1][3] + matrix[0][2] * m.matrix[2][3] + matrix[0][3] * m.matrix[3][3];

      ret.matrix[1][0] = matrix[1][0] * m.matrix[0][0] + matrix[1][1] * m.matrix[1][0] + matrix[1][2] * m.matrix[2][0] + matrix[1][3] * m.matrix[3][0];
      ret.matrix[1][1] = matrix[1][0] * m.matrix[0][1] + matrix[1][1] * m.matrix[1][1] + matrix[1][2] * m.matrix[2][1] + matrix[1][3] * m.matrix[3][1];
      ret.matrix[1][2] = matrix[1][0] * m.matrix[0][2] + matrix[1][1] * m.matrix[1][2] + matrix[1][2] * m.matrix[2][2] + matrix[1][3] * m.matrix[3][2];
      ret.matrix[1][3] = matrix[1][0] * m.matrix[0][3] + matrix[1][1] * m.matrix[1][3] + matrix[1][2] * m.matrix[2][3] + matrix[1][3] * m.matrix[3][3];

      ret.matrix[2][0] = matrix[2][0] * m.matrix[0][0] + matrix[2][1] * m.matrix[1][0] + matrix[2][2] * m.matrix[2][0] + matrix[2][3] * m.matrix[3][0];
      ret.matrix[2][1] = matrix[2][0] * m.matrix[0][1] + matrix[2][1] * m.matrix[1][1] + matrix[2][2] * m.matrix[2][1] + matrix[2][3] * m.matrix[3][1];
      ret.matrix[2][2] = matrix[2][0] * m.matrix[0][2] + matrix[2][1] * m.matrix[1][2] + matrix[2][2] * m.matrix[2][2] + matrix[2][3] * m.matrix[3][2];
      ret.matrix[2][3] = matrix[2][0] * m.matrix[0][3] + matrix[2][1] * m.matrix[1][3] + matrix[2][2] * m.matrix[2][3] + matrix[2][3] * m.matrix[3][3];

      ret.matrix[3][0] = matrix[3][0] * m.matrix[0][0] + matrix[3][1] * m.matrix[1][0] + matrix[3][2] * m.matrix[2][0] + matrix[3][3] * m.matrix[3][0];
      ret.matrix[3][1] = matrix[3][0] * m.matrix[0][1] + matrix[3][1] * m.matrix[1][1] + matrix[3][2] * m.matrix[2][1] + matrix[3][3] * m.matrix[3][1];
      ret.matrix[3][2] = matrix[3][0] * m.matrix[0][2] + matrix[3][1] * m.matrix[1][2] + matrix[3][2] * m.matrix[2][2] + matrix[3][3] * m.matrix[3][2];
      ret.matrix[3][3] = matrix[3][0] * m.matrix[0][3] + matrix[3][1] * m.matrix[1][3] + matrix[3][2] * m.matrix[2][3] + matrix[3][3] * m.matrix[3][3];

      return ret;
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Matrix4x4))
         return false;

      Matrix4x4 rhs = (Matrix4x4) obj;

      return (
            matrix[0][0] == rhs.matrix[0][0] &&
            matrix[0][1] == rhs.matrix[0][1] &&
            matrix[0][2] == rhs.matrix[0][2] &&
            matrix[0][3] == rhs.matrix[0][3] &&

            matrix[1][0] == rhs.matrix[1][0] &&
            matrix[1][1] == rhs.matrix[1][1] &&
            matrix[1][2] == rhs.matrix[1][2] &&
            matrix[1][3] == rhs.matrix[1][3] &&

            matrix[2][0] == rhs.matrix[2][0] &&
            matrix[2][1] == rhs.matrix[2][1] &&
            matrix[2][2] == rhs.matrix[2][2] &&
            matrix[2][3] == rhs.matrix[2][3] &&

            matrix[3][0] == rhs.matrix[3][0] &&
            matrix[3][1] == rhs.matrix[3][1] &&
            matrix[3][2] == rhs.matrix[3][2] &&
            matrix[3][3] == rhs.matrix[3][3]);
   }
}
