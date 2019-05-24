package net.danielthompson.danray.config;

/**
 * Created by daniel on 11/22/14.
 */
public class HighQuality extends RenderQuality {
   public HighQuality() {
      x = 1920;
      y = 1080;
      maxDepth = 16;
      samplesPerPixel = 256;
      superSamplesPerPixel = 0;
      depthOfField = false;
      convergenceTerminationThreshold = .8f;
   }
}
