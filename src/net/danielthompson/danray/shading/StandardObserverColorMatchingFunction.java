package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/7/15.
 */
public class StandardObserverColorMatchingFunction {
   public float[] Buckets;

   private StandardObserverColorMatchingFunction() {
      Buckets = new float[48];
   }

   public static StandardObserverColorMatchingFunction XBar = GetXBar();

   private static StandardObserverColorMatchingFunction GetXBar() {
      StandardObserverColorMatchingFunction xBar = new StandardObserverColorMatchingFunction();
      xBar.Buckets[0] = 0.000000122f;
      xBar.Buckets[1] = 0.000005959f;
      xBar.Buckets[2] = 0.000159952f;
      xBar.Buckets[3] = 0.002361600f;
      xBar.Buckets[4] = 0.019109700f;
      xBar.Buckets[5] = 0.084736000f;
      xBar.Buckets[6] = 0.204492000f;
      xBar.Buckets[7] = 0.314679000f;
      xBar.Buckets[8] = 0.383734000f;
      xBar.Buckets[9] = 0.370702000f;
      xBar.Buckets[10] = 0.302273000f;
      xBar.Buckets[11] = 0.195618000f;
      xBar.Buckets[12] = 0.080507000f;
      xBar.Buckets[13] = 0.016172000f;
      xBar.Buckets[14] = 0.003816000f;
      xBar.Buckets[15] = 0.037465000f;
      xBar.Buckets[16] = 0.117749000f;
      xBar.Buckets[17] = 0.236491000f;
      xBar.Buckets[18] = 0.376772000f;
      xBar.Buckets[19] = 0.529826000f;
      xBar.Buckets[20] = 0.705224000f;
      xBar.Buckets[21] = 0.878655000f;
      xBar.Buckets[22] = 1.014160000f;
      xBar.Buckets[23] = 1.118520000f;
      xBar.Buckets[24] = 1.123990000f;
      xBar.Buckets[25] = 1.030480000f;
      xBar.Buckets[26] = 0.856297000f;
      xBar.Buckets[27] = 0.647467000f;
      xBar.Buckets[28] = 0.431567000f;
      xBar.Buckets[29] = 0.268329000f;
      xBar.Buckets[30] = 0.152568000f;
      xBar.Buckets[31] = 0.081260600f;
      xBar.Buckets[32] = 0.040850800f;
      xBar.Buckets[33] = 0.019941300f;
      xBar.Buckets[34] = 0.009576880f;
      xBar.Buckets[35] = 0.004552630f;
      xBar.Buckets[36] = 0.002174960f;
      xBar.Buckets[37] = 0.001044760f;
      xBar.Buckets[38] = 0.000508258f;
      xBar.Buckets[39] = 0.000250969f;
      xBar.Buckets[40] = 0.000126390f;
      xBar.Buckets[41] = 0.000064526f;
      xBar.Buckets[42] = 0.000033412f;
      xBar.Buckets[43] = 0.000017612f;
      xBar.Buckets[44] = 0.000009414f;
      xBar.Buckets[45] = 0.000005093f;
      xBar.Buckets[46] = 0.000002795f;
      xBar.Buckets[47] = 0.000001553f;

      return xBar;
   }

   public static StandardObserverColorMatchingFunction YBar = GetYBar();

   private static StandardObserverColorMatchingFunction GetYBar() {
      StandardObserverColorMatchingFunction yBar = new StandardObserverColorMatchingFunction();
      yBar.Buckets[0] = 0.000000013f;
      yBar.Buckets[1] = 0.000000651f;
      yBar.Buckets[2] = 0.000017364f;
      yBar.Buckets[3] = 0.000253400f;
      yBar.Buckets[4] = 0.002004400f;
      yBar.Buckets[5] = 0.008756000f;
      yBar.Buckets[6] = 0.021391000f;
      yBar.Buckets[7] = 0.038676000f;
      yBar.Buckets[8] = 0.062077000f;
      yBar.Buckets[9] = 0.089456000f;
      yBar.Buckets[10] = 0.128201000f;
      yBar.Buckets[11] = 0.185190000f;
      yBar.Buckets[12] = 0.253589000f;
      yBar.Buckets[13] = 0.339133000f;
      yBar.Buckets[14] = 0.460777000f;
      yBar.Buckets[15] = 0.606741000f;
      yBar.Buckets[16] = 0.761757000f;
      yBar.Buckets[17] = 0.875211000f;
      yBar.Buckets[18] = 0.961988000f;
      yBar.Buckets[19] = 0.991761000f;
      yBar.Buckets[20] = 0.997340000f;
      yBar.Buckets[21] = 0.955552000f;
      yBar.Buckets[22] = 0.868934000f;
      yBar.Buckets[23] = 0.777405000f;
      yBar.Buckets[24] = 0.658341000f;
      yBar.Buckets[25] = 0.527963000f;
      yBar.Buckets[26] = 0.398057000f;
      yBar.Buckets[27] = 0.283493000f;
      yBar.Buckets[28] = 0.179828000f;
      yBar.Buckets[29] = 0.107633000f;
      yBar.Buckets[30] = 0.060281000f;
      yBar.Buckets[31] = 0.031800400f;
      yBar.Buckets[32] = 0.015905100f;
      yBar.Buckets[33] = 0.007748800f;
      yBar.Buckets[34] = 0.003717740f;
      yBar.Buckets[35] = 0.001768470f;
      yBar.Buckets[36] = 0.000846190f;
      yBar.Buckets[37] = 0.000407410f;
      yBar.Buckets[38] = 0.000198730f;
      yBar.Buckets[39] = 0.000098428f;
      yBar.Buckets[40] = 0.000049737f;
      yBar.Buckets[41] = 0.000025486f;
      yBar.Buckets[42] = 0.000013249f;
      yBar.Buckets[43] = 0.000007013f;
      yBar.Buckets[44] = 0.000003765f;
      yBar.Buckets[45] = 0.000002046f;
      yBar.Buckets[46] = 0.000001128f;
      yBar.Buckets[47] = 0.000000630f;

      return yBar;
   }

   public static StandardObserverColorMatchingFunction ZBar = GetYBar();

   private static StandardObserverColorMatchingFunction GetZBar() {
      StandardObserverColorMatchingFunction zBar = new StandardObserverColorMatchingFunction();
      zBar.Buckets[0] = 0.000000535f;
      zBar.Buckets[1] = 0.000026144f;
      zBar.Buckets[2] = 0.000704776f;
      zBar.Buckets[3] = 0.010482200f;
      zBar.Buckets[4] = 0.086010900f;
      zBar.Buckets[5] = 0.389366000f;
      zBar.Buckets[6] = 0.972542000f;
      zBar.Buckets[7] = 1.553480000f;
      zBar.Buckets[8] = 1.967280000f;
      zBar.Buckets[9] = 1.994800000f;
      zBar.Buckets[10] = 1.745370000f;
      zBar.Buckets[11] = 1.317560000f;
      zBar.Buckets[12] = 0.772125000f;
      zBar.Buckets[13] = 0.415254000f;
      zBar.Buckets[14] = 0.218502000f;
      zBar.Buckets[15] = 0.112044000f;
      zBar.Buckets[16] = 0.060709000f;
      zBar.Buckets[17] = 0.030451000f;
      zBar.Buckets[18] = 0.013676000f;
      zBar.Buckets[19] = 0.003988000f;
      zBar.Buckets[20] = 0.000000000f;
      zBar.Buckets[21] = 0.000000000f;
      zBar.Buckets[22] = 0.000000000f;
      zBar.Buckets[23] = 0.000000000f;
      zBar.Buckets[24] = 0.000000000f;
      zBar.Buckets[25] = 0.000000000f;
      zBar.Buckets[26] = 0.000000000f;
      zBar.Buckets[27] = 0.000000000f;
      zBar.Buckets[28] = 0.000000000f;
      zBar.Buckets[29] = 0.000000000f;
      zBar.Buckets[30] = 0.000000000f;
      zBar.Buckets[31] = 0.000000000f;
      zBar.Buckets[32] = 0.000000000f;
      zBar.Buckets[33] = 0.000000000f;
      zBar.Buckets[34] = 0.000000000f;
      zBar.Buckets[35] = 0.000000000f;
      zBar.Buckets[36] = 0.000000000f;
      zBar.Buckets[37] = 0.000000000f;
      zBar.Buckets[38] = 0.000000000f;
      zBar.Buckets[39] = 0.000000000f;
      zBar.Buckets[40] = 0.000000000f;
      zBar.Buckets[41] = 0.000000000f;
      zBar.Buckets[42] = 0.000000000f;
      zBar.Buckets[43] = 0.000000000f;
      zBar.Buckets[44] = 0.000000000f;
      zBar.Buckets[45] = 0.000000000f;
      zBar.Buckets[46] = 0.000000000f;
      zBar.Buckets[47] = 0.000000000f;

      return zBar;
   }
}
