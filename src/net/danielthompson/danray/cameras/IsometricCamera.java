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
   public Ray[] getInitialStochasticRaysForPixel(double x, double y, int samplesPerPixel) {

      double oneOverSamplesPlusOne = 1.0 / ((double) samplesPerPixel + 1.0);

      Ray[] rays = new Ray[samplesPerPixel * samplesPerPixel];

      int vectorIndex = 0;

      for (double i = 0.0; i < samplesPerPixel; i++) {

         double newX = x + ((i + 1.0) / ((double) samplesPerPixel + 1.0));


         for (double j = 0.0; j < samplesPerPixel; j++) {
            double newY = y + ((j + 1.0) / ((double) samplesPerPixel + 1.0));

            Point worldPoint = getWorldPointForPixel(newX, newY);

            Point newRearFocalPoint = Point.Plus(worldPoint, Vector.Scale(Settings.Orientation.Direction, -1));

            rays[vectorIndex++] = new Ray(newRearFocalPoint, Settings.Orientation.Direction);
         }
      }

      return rays;
   }


}