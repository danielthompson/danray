package net.danielthompson.danray.shading.fullspectrum;

/**
 * Created by daniel on 5/6/15.
 */
public class FullSpectralReflectanceCurve extends FullSpectrum {

   public FullSpectralReflectanceCurve() {
      super();
   }

   public FullSpectralReflectanceCurve(FullSpectrum s) {
      super();
      this.Buckets = s.Buckets;
   }

   public static FullSpectralReflectanceCurve Lerp(FullSpectralReflectanceCurve src1, float w1, FullSpectralReflectanceCurve src2, float w2) {
      FullSpectralReflectanceCurve returnValue = new FullSpectralReflectanceCurve(FullSpectrum.Lerp(src1, w1, src2, w2));

      return returnValue;
   }
}
