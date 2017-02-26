package net.danielthompson.danray.shading.fullspectrum;

/**
 * Created by daniel on 5/8/15.
 */
public class FullSpectralReflectanceCurveLibrary {
   public static FullSpectralReflectanceCurve LemonSkin = GetLemonSkin();

   private static FullSpectralReflectanceCurve GetLemonSkin() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();
      curve.Buckets[0] = 0.1f;
      curve.Buckets[1] = 0.1f;
      curve.Buckets[2] = 0.08f;
      curve.Buckets[3] = 0.08f;
      curve.Buckets[4] = 0.07f;
      curve.Buckets[5] = 0.07f;
      curve.Buckets[6] = 0.08f;
      curve.Buckets[7] = 0.09f;
      curve.Buckets[8] = 0.12f;
      curve.Buckets[9] = 0.14f;
      curve.Buckets[10] = 0.16f;
      curve.Buckets[11] = 0.17f;
      curve.Buckets[12] = 0.18f;
      curve.Buckets[13] = 0.26f;
      curve.Buckets[14] = 0.37f;
      curve.Buckets[15] = 0.43f;
      curve.Buckets[16] = 0.48f;
      curve.Buckets[17] = 0.53f;
      curve.Buckets[18] = 0.55f;
      curve.Buckets[19] = 0.57f;
      curve.Buckets[20] = 0.59f;
      curve.Buckets[21] = 0.61f;
      curve.Buckets[22] = 0.63f;
      curve.Buckets[23] = 0.63f;
      curve.Buckets[24] = 0.64f;
      curve.Buckets[25] = 0.63f;
      curve.Buckets[26] = 0.64f;
      curve.Buckets[27] = 0.65f;
      curve.Buckets[28] = 0.65f;
      curve.Buckets[29] = 0.61f;
      curve.Buckets[30] = 0.58f;
      curve.Buckets[31] = 0.64f;
      curve.Buckets[32] = 0.7f;
      curve.Buckets[33] = 0.71f;
      curve.Buckets[34] = 0.72f;
      curve.Buckets[35] = 0.72f;
      curve.Buckets[36] = 0.72f;
      curve.Buckets[37] = 0.72f;
      curve.Buckets[38] = 0.72f;
      return curve;
   }

   public static FullSpectralReflectanceCurve Blue = GetBlue();

   private static FullSpectralReflectanceCurve GetBlue() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[6] = 1f;
      curve.Buckets[7] = 1f;
      curve.Buckets[8] = 1f;

      return curve;
   }
   
   public static FullSpectralReflectanceCurve Red = GetRed();

   private static FullSpectralReflectanceCurve GetRed() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[28] = 1f;
      curve.Buckets[29] = 1f;
      curve.Buckets[30] = 1f;
      curve.Buckets[31] = 1f;
      curve.Buckets[32] = 1f;

      return curve;
      
   }

   public static FullSpectralReflectanceCurve Yellow = GetYellow();

   private static FullSpectralReflectanceCurve GetYellow() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[18] = 1f;
      curve.Buckets[19] = 1f;
      curve.Buckets[20] = 1f;

      return curve;

   }


   public static FullSpectralReflectanceCurve Constant = GetConstant();

   private static FullSpectralReflectanceCurve GetConstant() {

      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[0] = 1f;
      curve.Buckets[1] = 1f;
      curve.Buckets[2] = 1f;
      curve.Buckets[3] = 1f;
      curve.Buckets[4] = 1f;
      curve.Buckets[5] = 1f;
      curve.Buckets[6] = 1f;
      curve.Buckets[7] = 1f;
      curve.Buckets[8] = 1f;
      curve.Buckets[9] = 1f;
      curve.Buckets[10] = 1f;
      curve.Buckets[11] = 1f;
      curve.Buckets[12] = 1f;
      curve.Buckets[13] = 1f;
      curve.Buckets[14] = 1f;
      curve.Buckets[15] = 1f;
      curve.Buckets[16] = 1f;
      curve.Buckets[17] = 1f;
      curve.Buckets[18] = 1f;
      curve.Buckets[19] = 1f;
      curve.Buckets[20] = 1f;
      curve.Buckets[21] = 1f;
      curve.Buckets[22] = 1f;
      curve.Buckets[23] = 1f;
      curve.Buckets[24] = 1f;
      curve.Buckets[25] = 1f;
      curve.Buckets[26] = 1f;
      curve.Buckets[27] = 1f;
      curve.Buckets[28] = 1f;
      curve.Buckets[29] = 1f;
      curve.Buckets[30] = 1f;
      curve.Buckets[31] = 1f;
      curve.Buckets[32] = 1f;
      curve.Buckets[33] = 1f;
      curve.Buckets[34] = 1f;
      curve.Buckets[35] = 1f;
      curve.Buckets[36] = 1f;
      curve.Buckets[37] = 1f;
      curve.Buckets[38] = 1f;

      return curve;
   }

   public static FullSpectralReflectanceCurve Grass = GetGrass();

   private static FullSpectralReflectanceCurve GetGrass() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[0] = 0.01f;
      curve.Buckets[1] = 0.01f;
      curve.Buckets[2] = 0.01f;
      curve.Buckets[3] = 0.01f;
      curve.Buckets[4] = 0.02f;
      curve.Buckets[5] = 0.03f;
      curve.Buckets[6] = 0.03f;
      curve.Buckets[7] = 0.03f;
      curve.Buckets[8] = 0.04f;
      curve.Buckets[9] = 0.04f;
      curve.Buckets[10] = 0.05f;
      curve.Buckets[11] = 0.05f;
      curve.Buckets[12] = 0.06f;
      curve.Buckets[13] = 0.07f;
      curve.Buckets[14] = 0.09f;
      curve.Buckets[15] = 0.11f;
      curve.Buckets[16] = 0.12f;
      curve.Buckets[17] = 0.12f;
      curve.Buckets[18] = 0.11f;
      curve.Buckets[19] = 0.09f;
      curve.Buckets[20] = 0.07f;
      curve.Buckets[21] = 0.05f;
      curve.Buckets[22] = 0.04f;
      curve.Buckets[23] = 0.04f;
      curve.Buckets[24] = 0.04f;
      curve.Buckets[25] = 0.02f;
      curve.Buckets[26] = 0.01f;
      curve.Buckets[27] = 0.01f;
      curve.Buckets[28] = 0.01f;
      curve.Buckets[29] = 0.02f;
      curve.Buckets[30] = 0.05f;
      curve.Buckets[31] = 0.08f;
      curve.Buckets[32] = 0.14f;
      curve.Buckets[33] = 0.23f;
      curve.Buckets[34] = 0.31f;
      curve.Buckets[35] = 0.4f;
      curve.Buckets[36] = 0.49f;
      curve.Buckets[37] = 0.55f;
      curve.Buckets[38] = 0.65f;

      return curve;
   }
   
   public static FullSpectralReflectanceCurve Orange = GetOrange();

   private static FullSpectralReflectanceCurve GetOrange() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[0] = 0.00000f;
      curve.Buckets[1] = 0.00000f;
      curve.Buckets[2] = 0.00000f;
      curve.Buckets[3] = 0.00000f;
      curve.Buckets[4] = 0.00000f;
      curve.Buckets[5] = 0.00000f;
      curve.Buckets[6] = 0.00000f;
      curve.Buckets[7] = 0.00000f;
      curve.Buckets[8] = 0.00000f;
      curve.Buckets[9] = 0.00000f;
      curve.Buckets[10] = 0.00000f;
      curve.Buckets[11] = 0.00000f;
      curve.Buckets[12] = 0.00000f;
      curve.Buckets[13] = 0.00004f;
      curve.Buckets[14] = 0.00034f;
      curve.Buckets[15] = 0.00219f;
      curve.Buckets[16] = 0.01111f;
      curve.Buckets[17] = 0.04394f;
      curve.Buckets[18] = 0.13534f;
      curve.Buckets[19] = 0.32465f;
      curve.Buckets[20] = 0.60653f;
      curve.Buckets[21] = 0.88250f;
      curve.Buckets[22] = 1.00000f;
      curve.Buckets[23] = 0.88250f;
      curve.Buckets[24] = 0.60653f;
      curve.Buckets[25] = 0.32465f;
      curve.Buckets[26] = 0.13534f;
      curve.Buckets[27] = 0.04394f;
      curve.Buckets[28] = 0.01111f;
      curve.Buckets[29] = 0.00219f;
      curve.Buckets[30] = 0.00034f;
      curve.Buckets[31] = 0.00004f;
      curve.Buckets[32] = 0.00000f;
      curve.Buckets[33] = 0.00000f;
      curve.Buckets[34] = 0.00000f;
      curve.Buckets[35] = 0.00000f;
      curve.Buckets[36] = 0.00000f;
      curve.Buckets[37] = 0.00000f;
      curve.Buckets[38] = 0.00000f;

      return curve;
   }

   public static FullSpectralReflectanceCurve LightBlue = GetLightBlue();

   private static FullSpectralReflectanceCurve GetLightBlue() {
      FullSpectralReflectanceCurve curve = new FullSpectralReflectanceCurve();

      curve.Buckets[0] = 0.21072f;
      curve.Buckets[1] = 0.24798f;
      curve.Buckets[2] = 0.29007f;
      curve.Buckets[3] = 0.33562f;
      curve.Buckets[4] = 0.38261f;
      curve.Buckets[5] = 0.42849f;
      curve.Buckets[6] = 0.47038f;
      curve.Buckets[7] = 0.50539f;
      curve.Buckets[8] = 0.53087f;
      curve.Buckets[9] = 0.54482f;
      curve.Buckets[10] = 0.54611f;
      curve.Buckets[11] = 0.53463f;
      curve.Buckets[12] = 0.51132f;
      curve.Buckets[13] = 0.47803f;
      curve.Buckets[14] = 0.43728f;
      curve.Buckets[15] = 0.39196f;
      curve.Buckets[16] = 0.34498f;
      curve.Buckets[17] = 0.29896f;
      curve.Buckets[18] = 0.25605f;
      curve.Buckets[19] = 0.21775f;
      curve.Buckets[20] = 0.18498f;
      curve.Buckets[21] = 0.15833f;
      curve.Buckets[22] = 0.13894f;
      curve.Buckets[23] = 0.13036f;
      curve.Buckets[24] = 0.14032f;
      curve.Buckets[25] = 0.17763f;
      curve.Buckets[26] = 0.24020f;
      curve.Buckets[27] = 0.30334f;
      curve.Buckets[28] = 0.32927f;
      curve.Buckets[29] = 0.29889f;
      curve.Buckets[30] = 0.23052f;
      curve.Buckets[31] = 0.16114f;
      curve.Buckets[32] = 0.11460f;
      curve.Buckets[33] = 0.09210f;
      curve.Buckets[34] = 0.08398f;
      curve.Buckets[35] = 0.08175f;
      curve.Buckets[36] = 0.08127f;
      curve.Buckets[37] = 0.08118f;
      curve.Buckets[38] = 0.08116f;

      return curve;
   }


}
