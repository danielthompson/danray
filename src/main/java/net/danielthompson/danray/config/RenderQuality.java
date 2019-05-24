package net.danielthompson.danray.config;

/**
 * Created by daniel on 11/22/14.
 */
public abstract class RenderQuality {
   public int x, y;
   public int maxDepth;
   public int samplesPerPixel;
   public int superSamplesPerPixel;
   public boolean depthOfField;
   public float convergenceTerminationThreshold;
}
