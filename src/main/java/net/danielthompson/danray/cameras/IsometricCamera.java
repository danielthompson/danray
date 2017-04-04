package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 12/14/14.
 */
public class IsometricCamera extends Camera {

   public IsometricCamera(CameraSettings settings) {
      super(settings);
   }

   @Override
   public Ray[] getRays(float x, float y, int samplesPerPixel) {

      Ray[] rays = new Ray[samplesPerPixel * samplesPerPixel];

      int vectorIndex = 0;

      for (float i = 0.0f; i < samplesPerPixel; i++) {

         float newX = x + ((i + 1.0f) / ((float) samplesPerPixel + 1.0f));


         for (float j = 0.0f; j < samplesPerPixel; j++) {
            float newY = y + ((j + 1.0f) / ((float) samplesPerPixel + 1.0f));

            Point worldPoint = getWorldPointForPixel(newX, newY);

            Point newRearFocalPoint = Point.Plus(worldPoint, Vector.Scale(Settings.Orientation.Direction, -1));

            rays[vectorIndex++] = new Ray(newRearFocalPoint, Settings.Orientation.Direction);
         }
      }

      return rays;
   }


}