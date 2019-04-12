package net.danielthompson.danray.films;

import net.danielthompson.danray.structures.Sample;

import java.awt.image.BufferedImage;

/**
 * Created by daniel on 5/7/16.
 */
public class TriangleFilterFilm extends AbstractFilm {

   public TriangleFilterFilm(BufferedImage image) {
      super(image);
   }

   public void AddSamples(float x, float y, Sample[] samples) {
      int xFloor = (int)x;
      int yFloor = (int)y;

      for (Sample sample : samples) {
         // add center
         AddSampleToPixel(xFloor, yFloor, sample, 1.0f);
      }

   }
}