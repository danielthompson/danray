package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;


/**
 * Created by daniel on 1/18/14.
 */
public class SimplePointableCamera extends Camera {

   public SimplePointableCamera(CameraSettings settings) {
      super(settings);
   }

   @Override
   public Ray[] getRays(float x, float y, int samplesPerPixel) {
      float oneOverSamplesPlusOne = 1.0f / ((float) samplesPerPixel + 1.0f);

      Ray[] rays = new Ray[samplesPerPixel * samplesPerPixel];

      int vectorIndex = 0;

      for (float i = 1.0f; i <= samplesPerPixel; i++) {

         float newX = x + (i * oneOverSamplesPlusOne);


         for (float j = 1.0f; j <= samplesPerPixel; j++) {
            float newY = y + (j * oneOverSamplesPlusOne);

            Point worldPoint = getWorldPointForPixel(newX, newY);

            Vector direction = Point.Minus(worldPoint, _rearFocalPoint);

            rays[vectorIndex++] = new Ray(_rearFocalPoint, direction);
         }
      }

      return rays;
   }

}