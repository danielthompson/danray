package net.danielthompson.danray.shading;

/**
 * Created by daniel on 5/6/15.
 */
public class SpectralReflectanceCurve extends Spectrum {

   public SpectralReflectanceCurve() {
      super();
   }

   public SpectralReflectanceCurve(Spectrum s) {
      super();
      this.Buckets = s.Buckets;
   }

   public static SpectralReflectanceCurve Lerp(SpectralReflectanceCurve src1, float w1, SpectralReflectanceCurve src2, float w2) {
      SpectralReflectanceCurve returnValue = new SpectralReflectanceCurve(Spectrum.Lerp(src1, w1, src2, w2));

      return returnValue;
   }
}
