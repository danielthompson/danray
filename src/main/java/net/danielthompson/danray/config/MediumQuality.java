package net.danielthompson.danray.config;

/**
 * Created by daniel on 11/22/14.
 */
public class MediumQuality extends RenderQuality {
   public MediumQuality() {
      x = 1280;
      y = 720;
      maxDepth = 5;
      samplesPerPixel = 4;
      superSamplesPerPixel = 0;
      depthOfField = false;
      convergenceTerminationThreshold = .8f;
   }
}
