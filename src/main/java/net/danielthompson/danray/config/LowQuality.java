package net.danielthompson.danray.config;

/**
 * Created by daniel on 11/22/14.
 */
public class LowQuality extends RenderQuality {
   public LowQuality() {
      x = 640;
      y = 480;
      maxDepth = 5;
      samplesPerPixel = 1;
      superSamplesPerPixel = 0;
      depthOfField = false;
      convergenceTerminationThreshold = .8f;
   }
}