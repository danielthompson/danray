package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 2/27/14.
 */
public class DepthOfFieldCamera extends Camera {

   public DepthOfFieldCamera(CameraSettings settings) {
      super(settings);

      _rearFocalPoint = _currentOrientation.GetPointAtT(-Settings.FocalLength);
      //_rearFocalPoint.Plus(_currentOrientation.Origin);
   }


   @Override
   public Point getWorldPointForPixel(int x, int y) {
      return getWorldPointForPixel((float) x, (float) y);
   }

   @Override
   public Point getWorldPointForPixel(float x, float y) {

      Point point = getDefaultOrientationWorldPointForPixel(x, y);
      // comment test
      ConvertToWorldCoordinates(point);

      return point;
   }

   public Point GetStochasticWorldPointInApertureForPixel(float x, float y) {
      Point point = getDefaultOrientationWorldPointForPixel(x, y);

      if (Settings.Aperture == null) {
         float xOffset = (float) (Math.random() - .5);
         float yOffset = (float) (Math.random() - .5);

         point = new Point(point.X + xOffset, point.Y + yOffset, point.Z);
      }
      else {
         Point originPoint = Settings.Aperture.GetOriginPoint();
         point.Plus(originPoint);
      }

      ConvertToWorldCoordinates(point);

      return point;
   }

   public Point GetStochasticWorldPointForPixel(float x, float y) {
      Point point = getDefaultOrientationWorldPointForPixel(x, y);

      float xOffset = (float) (Math.random() - .5);
      float yOffset = (float) (Math.random() - .5);

      point = new Point(point.X + xOffset, point.Y + yOffset, point.Z);

      ConvertToWorldCoordinates(point);

      return point;

   }

   @Override
   public Ray[] getInitialStochasticRaysForPixel(float x, float y, int samplesPerPixel) {

      Ray[] rays = new Ray[samplesPerPixel];

      if (samplesPerPixel == 1) {
         rays[0] = getStochasticRayForPixel(x, y);
      }
      else {
         for (int i = 0; i < samplesPerPixel; i++) {

            Point centerPoint = GetStochasticWorldPointForPixel(x, y);

            Vector direction = Vector.Minus(centerPoint, _rearFocalPoint);

            direction.Normalize();

            direction.Scale(Settings.FocusDistance);

            Point focalPoint = Point.Plus(centerPoint, direction);

            Point origin = GetStochasticWorldPointInApertureForPixel(x, y);
            Vector direction2 = Vector.Minus(focalPoint, origin);
            rays[i] = new Ray(origin, direction2);
         }
      }
      return rays;
   }

   public Ray[] getInitialStochasticRaysForPixel(int x, int y, int samplesPerPixel) {
      return getInitialStochasticRaysForPixel((float) x, (float) y, samplesPerPixel);
   }

   @Override
   public Ray getStochasticRayForPixel(float x, float y) {
      Point centerPoint = GetStochasticWorldPointForPixel(x, y);
      Vector direction = Vector.Minus(centerPoint, _rearFocalPoint);
      direction.Normalize();
      direction.Scale(Settings.FocusDistance);
      Point focalPoint = Point.Plus(centerPoint, direction);
      return new Ray(centerPoint, Point.Minus(focalPoint, centerPoint));
   }





}