package net.danielthompson.danray.presets;

/**
 * Created by daniel on 11/22/14.
 */
public interface RenderQualityPreset {
   int getX();
   int getY();

   int getMaxDepth();
   int getSamplesPerPixel();
   int getSuperSamplesPerPixel();

   boolean getUseDepthOfField();

   float getConvergenceTerminationThreshold();
}
