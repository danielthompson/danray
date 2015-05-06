package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;


/**
 * Created by daniel on 1/18/14.
 */
public class SimplePointableCamera extends CameraBase implements Camera {

   public SimplePointableCamera(CameraSettings settings) {
      super(settings);
   }

   @Override
   public Ray[] getInitialStochasticRaysForPixel(int x, int y, int samplesPerPixel) {
      double oneOverSamplesPlusOne = 1.0 / ((double) samplesPerPixel + 1.0);

      Ray[] rays = new Ray[samplesPerPixel * samplesPerPixel];

      int vectorIndex = 0;

      for (double i = 1.0; i <= samplesPerPixel; i++) {

         double newX = x + (i * oneOverSamplesPlusOne);


         for (double j = 1.0; j <= samplesPerPixel; j++) {
            double newY = y + (j * oneOverSamplesPlusOne);

            rays[vectorIndex++] = new Ray(_rearFocalPoint, Point.Minus(getWorldPointForPixel(newX, newY), _rearFocalPoint));
         }
      }

      return rays;
   }

}