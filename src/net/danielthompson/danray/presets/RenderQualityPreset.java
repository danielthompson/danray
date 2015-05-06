package net.danielthompson.danray.presets;

/**
 * Created by daniel on 11/22/14.
 */
public interface RenderQualityPreset {
   public int getX();
   public int getY();

   public int getMaxDepth();
   public int getSamplesPerPixel();
   public int getSuperSamplesPerPixel();
   public int getAirIndexOfRefraction();
   public int getNumberOfThreads();
   public boolean getUseDepthOfField();

   float getConvergenceTerminationThreshold();
}
