package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/8/15.
 */
public class RelativeSpectralPowerDistributionLibrary {
   public static RelativeSpectralPowerDistribution D65 = GetD65();

   private static RelativeSpectralPowerDistribution GetD65() {
      RelativeSpectralPowerDistribution curve = new RelativeSpectralPowerDistribution();

      curve.Buckets[0] = 0.014508721f;
      curve.Buckets[1] = 0.015865283f;
      curve.Buckets[2] = 0.024025127f;
      curve.Buckets[3] = 0.026559911f;
      curve.Buckets[4] = 0.027124809f;
      curve.Buckets[5] = 0.025165316f;
      curve.Buckets[6] = 0.030444057f;
      curve.Buckets[7] = 0.033969373f;
      curve.Buckets[8] = 0.034202787f;
      curve.Buckets[9] = 0.033346063f;

      curve.Buckets[10] = 0.033654379f;
      curve.Buckets[11] = 0.031589647f;
      curve.Buckets[12] = 0.031747289f;
      curve.Buckets[13] = 0.031296717f;
      curve.Buckets[14] = 0.030422284f;
      curve.Buckets[15] = 0.031263912f;
      curve.Buckets[16] = 0.030310512f;
      curve.Buckets[17] = 0.030206288f;
      curve.Buckets[18] = 0.029031667f;
      curve.Buckets[19] = 0.027967424f;

      curve.Buckets[20] = 0.027808853f;
      curve.Buckets[21] = 0.025746908f;
      curve.Buckets[22] = 0.0261303f;
      curve.Buckets[23] = 0.026012112f;
      curve.Buckets[24] = 0.025460394f;
      curve.Buckets[25] = 0.024180069f;
      curve.Buckets[26] = 0.024299273f;
      curve.Buckets[27] = 0.023233114f;
      curve.Buckets[28] = 0.023287635f;
      curve.Buckets[29] = 0.023886617f;

      curve.Buckets[30] = 0.022727208f;
      curve.Buckets[31] = 0.020241255f;
      curve.Buckets[32] = 0.020789315f;
      curve.Buckets[33] = 0.021584754f;
      curve.Buckets[34] = 0.017884668f;
      curve.Buckets[35] = 0.020288955f;
      curve.Buckets[36] = 0.021799008f;
      curve.Buckets[37] = 0.018462021f;
      curve.Buckets[38] = 0.013475977f;

      return curve;
   }

   public static RelativeSpectralPowerDistribution Red = GetRed();

   private static RelativeSpectralPowerDistribution GetRed() {
      RelativeSpectralPowerDistribution curve = new RelativeSpectralPowerDistribution();

      curve.Buckets[28] = 0.2f;
      curve.Buckets[29] = 0.2f;
      curve.Buckets[30] = 0.2f;
      curve.Buckets[31] = 0.2f;
      curve.Buckets[32] = 0.2f;

      return curve;
   }


   public static RelativeSpectralPowerDistribution Blue = GetBlue();

   private static RelativeSpectralPowerDistribution GetBlue() {
      RelativeSpectralPowerDistribution curve = new RelativeSpectralPowerDistribution();

      curve.Buckets[6] = 0.33f;
      curve.Buckets[7] = 0.33f;
      curve.Buckets[8] = 0.34f;

      return curve;
   }

   public static RelativeSpectralPowerDistribution Yellow = GetYellow();

   private static RelativeSpectralPowerDistribution GetYellow() {
      RelativeSpectralPowerDistribution curve = new RelativeSpectralPowerDistribution();

      curve.Buckets[18] = 0.33f;
      curve.Buckets[19] = 0.33f;
      curve.Buckets[20] = 0.34f;

      return curve;
   }

   public static RelativeSpectralPowerDistribution Constant = GetConstant();

   private static RelativeSpectralPowerDistribution GetConstant() {

      RelativeSpectralPowerDistribution curve = new RelativeSpectralPowerDistribution();
      
      curve.Buckets[0] = 0.025641026f;
      curve.Buckets[1] = 0.025641026f;
      curve.Buckets[2] = 0.025641026f;
      curve.Buckets[3] = 0.025641026f;
      curve.Buckets[4] = 0.025641026f;
      curve.Buckets[5] = 0.025641026f;
      curve.Buckets[6] = 0.025641026f;
      curve.Buckets[7] = 0.025641026f;
      curve.Buckets[8] = 0.025641026f;
      curve.Buckets[9] = 0.025641026f;
      curve.Buckets[10] = 0.025641026f;
      curve.Buckets[11] = 0.025641026f;
      curve.Buckets[12] = 0.025641026f;
      curve.Buckets[13] = 0.025641026f;
      curve.Buckets[14] = 0.025641026f;
      curve.Buckets[15] = 0.025641026f;
      curve.Buckets[16] = 0.025641026f;
      curve.Buckets[17] = 0.025641026f;
      curve.Buckets[18] = 0.025641026f;
      curve.Buckets[19] = 0.025641026f;
      curve.Buckets[20] = 0.025641026f;
      curve.Buckets[21] = 0.025641026f;
      curve.Buckets[22] = 0.025641026f;
      curve.Buckets[23] = 0.025641026f;
      curve.Buckets[24] = 0.025641026f;
      curve.Buckets[25] = 0.025641026f;
      curve.Buckets[26] = 0.025641026f;
      curve.Buckets[27] = 0.025641026f;
      curve.Buckets[28] = 0.025641026f;
      curve.Buckets[29] = 0.025641026f;
      curve.Buckets[30] = 0.025641026f;
      curve.Buckets[31] = 0.025641026f;
      curve.Buckets[32] = 0.025641026f;
      curve.Buckets[33] = 0.025641026f;
      curve.Buckets[34] = 0.025641026f;
      curve.Buckets[35] = 0.025641026f;
      curve.Buckets[36] = 0.025641026f;
      curve.Buckets[37] = 0.025641026f;
      curve.Buckets[38] = 0.025641026f;
      
      return curve;
   }

   public static RelativeSpectralPowerDistribution Sunset = GetSunset();

   private static RelativeSpectralPowerDistribution GetSunset() {
      RelativeSpectralPowerDistribution curve = new RelativeSpectralPowerDistribution();

      curve.Buckets[0] = 0.004522159f;
      curve.Buckets[1] = 0.006029545f;
      curve.Buckets[2] = 0.010551703f;
      curve.Buckets[3] = 0.012360567f;
      curve.Buckets[4] = 0.013566476f;
      curve.Buckets[5] = 0.012662044f;
      curve.Buckets[6] = 0.016581248f;
      curve.Buckets[7] = 0.018993066f;
      curve.Buckets[8] = 0.020198975f;
      curve.Buckets[9] = 0.020801929f;
      curve.Buckets[10] = 0.022610793f;
      curve.Buckets[11] = 0.022309316f;
      curve.Buckets[12] = 0.024118179f;
      curve.Buckets[13] = 0.024721134f;
      curve.Buckets[14] = 0.025324088f;
      curve.Buckets[15] = 0.025927043f;
      curve.Buckets[16] = 0.030147724f;
      curve.Buckets[17] = 0.030147724f;
      curve.Buckets[18] = 0.030147724f;
      curve.Buckets[19] = 0.030449201f;
      curve.Buckets[20] = 0.030449201f;
      curve.Buckets[21] = 0.029544769f;
      curve.Buckets[22] = 0.030449201f;
      curve.Buckets[23] = 0.031353633f;
      curve.Buckets[24] = 0.031353633f;
      curve.Buckets[25] = 0.031353633f;
      curve.Buckets[26] = 0.032258065f;
      curve.Buckets[27] = 0.031956587f;
      curve.Buckets[28] = 0.032861019f;
      curve.Buckets[29] = 0.034066928f;
      curve.Buckets[30] = 0.034368405f;
      curve.Buckets[31] = 0.031956587f;
      curve.Buckets[32] = 0.032559542f;
      curve.Buckets[33] = 0.032861019f;
      curve.Buckets[34] = 0.030147724f;
      curve.Buckets[35] = 0.030750678f;
      curve.Buckets[36] = 0.031353633f;
      curve.Buckets[37] = 0.029544769f;
      curve.Buckets[38] = 0.028640338f;
      
      return curve;
   }

}
