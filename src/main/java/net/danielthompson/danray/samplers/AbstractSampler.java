package net.danielthompson.danray.samplers;

import net.danielthompson.danray.structures.Point2;

/**
 * Created by daniel on 5/15/16.
 */
public abstract class AbstractSampler {

   public int SamplesPerPixel;

   public AbstractSampler(final int samplesPerPixel) {
      SamplesPerPixel = samplesPerPixel;
   }

   public abstract Point2[] GetSamples(int x, int y, int n);
}
