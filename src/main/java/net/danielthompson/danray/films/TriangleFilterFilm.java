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

      if (x > 0 && y > 0 && x < Width - 1 && y < Height - 1) {
         for (int i = 0; i < samples.length; i++) {
            // add left
            AddSampleToPixel(xFloor - 1, yFloor, samples[i], 0.15f);

            // add top
            AddSampleToPixel(xFloor, yFloor - 1, samples[i], 0.15f);

            // add right
            AddSampleToPixel(xFloor + 1, yFloor, samples[i], 0.15f);

            // add bottom
            AddSampleToPixel(xFloor, yFloor + 1, samples[i], 0.15f);

            // add center
            AddSampleToPixel(xFloor, yFloor, samples[i], 1.0f);
         }
      }
   }
}