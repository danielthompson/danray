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
      int xFloorMinus1 = (xFloor == 0) ? 0 : xFloor - 1;
      int yFloor = (int)y;
      int yFloorMinus1 = (yFloor == 0) ? 0 : yFloor - 1;

      if (xFloor == 156 && yFloor == 278) {
         int i = 0;
      }

      for (Sample sample : samples) {

         float xDist = (x - xFloor + 0.5f) % 1.0f;
         float yDist = (y - yFloor + 0.5f) % 1.0f;

         float topLeftDistance = xDist * xDist + yDist * yDist;
         float topRightDistance = (1.0f - xDist) * (1.0f - xDist) + yDist * yDist;
         float bottomLeftDistance = xDist * xDist + (1.0f - yDist) * (1.0f - yDist);
         float bottomRightDistance = (1.0f - xDist) * (1.0f - xDist) + (1.0f - yDist) * (1.0f - yDist);

         float sum = 2;
         float oneOverSum = 1.0f / sum;

         float topLeftPercentage = topLeftDistance * oneOverSum;
         float topRightPercentage = topRightDistance * oneOverSum;
         float bottomLeftPercentage = bottomLeftDistance * oneOverSum;
         float bottomRightPercentage = bottomRightDistance * oneOverSum;

         // top left
         AddSampleToPixel(xFloorMinus1, yFloorMinus1, sample, topLeftPercentage);
         // top right
         AddSampleToPixel(xFloor, yFloorMinus1, sample, topRightPercentage);
         // bottom left
         AddSampleToPixel(xFloorMinus1, yFloor, sample, bottomLeftPercentage);
         // bottom right
         AddSampleToPixel(xFloor, yFloor, sample, bottomRightPercentage);
      }
   }
}
