package net.danielthompson.danray.presets;

/**
 * Created by daniel on 11/22/14.
 */
public class MediumQuality implements RenderQualityPreset {
   private final int _x = 1280 , _y = 720;
   private final int _maxDepth = 4;
   private final int _samplesPerPixel = 512;
   private final int _superSamplesPerPixel = 0;
   private final int _airIndexOfRefraction = 1;
   private final boolean _useDepthOfField = false;
   private final float _convergenceTerminationThreshold = .9f;

   @Override
   public int getX() {
      return _x;
   }

   @Override
   public int getY() {
      return _y;
   }

   @Override
   public int getMaxDepth() {
      return _maxDepth;
   }

   @Override
   public int getSamplesPerPixel() {
      return _samplesPerPixel;
   }

   @Override
   public int getSuperSamplesPerPixel() {
      return _superSamplesPerPixel;
   }

   @Override
   public int getAirIndexOfRefraction() {
      return _airIndexOfRefraction;
   }

   @Override
   public boolean getUseDepthOfField() {
      return _useDepthOfField;
   }

   @Override
   public float getConvergenceTerminationThreshold() {
      return _convergenceTerminationThreshold;
   }
}
