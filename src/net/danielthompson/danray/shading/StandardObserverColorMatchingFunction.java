package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/7/15.
 */
public class StandardObserverColorMatchingFunction extends Spectrum {
   public static StandardObserverColorMatchingFunction XBar = GetXBar();

   private static StandardObserverColorMatchingFunction GetXBar() {
      StandardObserverColorMatchingFunction xBar = new StandardObserverColorMatchingFunction();
      xBar.Buckets[0] = 0.00016f;
      xBar.Buckets[1] = 0.00236f;
      xBar.Buckets[2] = 0.01911f;
      xBar.Buckets[3] = 0.08474f;
      xBar.Buckets[4] = 0.20449f;
      xBar.Buckets[5] = 0.31468f;
      xBar.Buckets[6] = 0.38373f;
      xBar.Buckets[7] = 0.37070f;
      xBar.Buckets[8] = 0.30227f;
      xBar.Buckets[9] = 0.19562f;
      xBar.Buckets[10] = 0.08051f;
      xBar.Buckets[11] = 0.01617f;
      xBar.Buckets[12] = 0.00382f;
      xBar.Buckets[13] = 0.03747f;
      xBar.Buckets[14] = 0.11775f;
      xBar.Buckets[15] = 0.23649f;
      xBar.Buckets[16] = 0.37677f;
      xBar.Buckets[17] = 0.52983f;
      xBar.Buckets[18] = 0.70522f;
      xBar.Buckets[19] = 0.87866f;
      xBar.Buckets[20] = 1.01416f;
      xBar.Buckets[21] = 1.11852f;
      xBar.Buckets[22] = 1.12399f;
      xBar.Buckets[23] = 1.03048f;
      xBar.Buckets[24] = 0.85630f;
      xBar.Buckets[25] = 0.64747f;
      xBar.Buckets[26] = 0.43157f;
      xBar.Buckets[27] = 0.26833f;
      xBar.Buckets[28] = 0.15257f;
      xBar.Buckets[29] = 0.08126f;
      xBar.Buckets[30] = 0.04085f;
      xBar.Buckets[31] = 0.01994f;
      xBar.Buckets[32] = 0.00958f;
      xBar.Buckets[33] = 0.00455f;
      xBar.Buckets[34] = 0.00217f;
      xBar.Buckets[35] = 0.00104f;
      xBar.Buckets[36] = 0.00051f;
      xBar.Buckets[37] = 0.00025f;
      xBar.Buckets[38] = 0.00013f;
      return xBar;
   }

   public static StandardObserverColorMatchingFunction YBar = GetYBar();

   private static StandardObserverColorMatchingFunction GetYBar() {
      StandardObserverColorMatchingFunction yBar = new StandardObserverColorMatchingFunction();
      yBar.Buckets[0] = 0.000017364f;
      yBar.Buckets[1] = 0.0002534f;
      yBar.Buckets[2] = 0.0020044f;
      yBar.Buckets[3] = 0.008756f;
      yBar.Buckets[4] = 0.021391f;
      yBar.Buckets[5] = 0.038676f;
      yBar.Buckets[6] = 0.062077f;
      yBar.Buckets[7] = 0.089456f;
      yBar.Buckets[8] = 0.128201f;
      yBar.Buckets[9] = 0.18519f;
      yBar.Buckets[10] = 0.253589f;
      yBar.Buckets[11] = 0.339133f;
      yBar.Buckets[12] = 0.460777f;
      yBar.Buckets[13] = 0.606741f;
      yBar.Buckets[14] = 0.761757f;
      yBar.Buckets[15] = 0.875211f;
      yBar.Buckets[16] = 0.961988f;
      yBar.Buckets[17] = 0.991761f;
      yBar.Buckets[18] = 0.99734f;
      yBar.Buckets[19] = 0.955552f;
      yBar.Buckets[20] = 0.868934f;
      yBar.Buckets[21] = 0.777405f;
      yBar.Buckets[22] = 0.658341f;
      yBar.Buckets[23] = 0.527963f;
      yBar.Buckets[24] = 0.398057f;
      yBar.Buckets[25] = 0.283493f;
      yBar.Buckets[26] = 0.179828f;
      yBar.Buckets[27] = 0.107633f;
      yBar.Buckets[28] = 0.060281f;
      yBar.Buckets[29] = 0.0318004f;
      yBar.Buckets[30] = 0.0159051f;
      yBar.Buckets[31] = 0.0077488f;
      yBar.Buckets[32] = 0.00371774f;
      yBar.Buckets[33] = 0.00176847f;
      yBar.Buckets[34] = 0.00084619f;
      yBar.Buckets[35] = 0.00040741f;
      yBar.Buckets[36] = 0.00019873f;
      yBar.Buckets[37] = 0.000098428f;
      yBar.Buckets[38] = 0.000049737f;

      return yBar;
   }

   public static float OneOverYBarSum = GetYBarSum();

   private static float GetYBarSum() {
      float value = 0f;
      for (int i = 0; i < YBar.Buckets.length; i++) {
         value += YBar.Buckets[i];
      }
      return 1.0f / value;
   }

   public static StandardObserverColorMatchingFunction ZBar = GetZBar();

   private static StandardObserverColorMatchingFunction GetZBar() {
      StandardObserverColorMatchingFunction zBar = new StandardObserverColorMatchingFunction();
      zBar.Buckets[0] = 0.000704776f;
      zBar.Buckets[1] = 0.0104822f;
      zBar.Buckets[2] = 0.0860109f;
      zBar.Buckets[3] = 0.389366f;
      zBar.Buckets[4] = 0.972542f;
      zBar.Buckets[5] = 1.55348f;
      zBar.Buckets[6] = 1.96728f;
      zBar.Buckets[7] = 1.9948f;
      zBar.Buckets[8] = 1.74537f;
      zBar.Buckets[9] = 1.31756f;
      zBar.Buckets[10] = 0.772125f;
      zBar.Buckets[11] = 0.415254f;
      zBar.Buckets[12] = 0.218502f;
      zBar.Buckets[13] = 0.112044f;
      zBar.Buckets[14] = 0.060709f;
      zBar.Buckets[15] = 0.030451f;
      zBar.Buckets[16] = 0.013676f;
      zBar.Buckets[17] = 0.003988f;
      zBar.Buckets[18] = 0f;
      zBar.Buckets[19] = 0f;
      zBar.Buckets[20] = 0f;
      zBar.Buckets[21] = 0f;
      zBar.Buckets[22] = 0f;
      zBar.Buckets[23] = 0f;
      zBar.Buckets[24] = 0f;
      zBar.Buckets[25] = 0f;
      zBar.Buckets[26] = 0f;
      zBar.Buckets[27] = 0f;
      zBar.Buckets[28] = 0f;
      zBar.Buckets[29] = 0f;
      zBar.Buckets[30] = 0f;
      zBar.Buckets[31] = 0f;
      zBar.Buckets[32] = 0f;
      zBar.Buckets[33] = 0f;
      zBar.Buckets[34] = 0f;
      zBar.Buckets[35] = 0f;
      zBar.Buckets[36] = 0f;
      zBar.Buckets[37] = 0f;
      zBar.Buckets[38] = 0f;

      return zBar;
   }
}
