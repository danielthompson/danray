package net.danielthompson.danray.presets;

/**
 * Created by daniel on 11/22/14.
 */
public class HighQuality implements RenderQualityPreset {
   private final int _x = 1920, _y = 1280;
   private final int _maxDepth = 6;
   private final int _samplesPerPixel = 4;
   private final int _superSamplesPerPixel = 500;
   private final int _airIndexOfRefraction = 1;
   private final boolean _useDepthOfField = false;
   private final float _convergenceTerminationThreshold = .99999f;

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
