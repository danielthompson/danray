package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 2/27/14.
 */
public class DepthOfFieldCamera extends CameraBase implements Camera {

   private Point _rearFocalPoint;

   public DepthOfFieldCamera(CameraSettings settings) {
      super(settings);

      _rearFocalPoint = _currentOrientation.GetPointAtT(-Settings.FocalLength);
      //_rearFocalPoint.Plus(_currentOrientation.Origin);
   }


   @Override
   public Point getWorldPointForPixel(int x, int y) {
      return getWorldPointForPixel((double) x, (double) y);
   }

   @Override
   public Point getWorldPointForPixel(double x, double y) {

      Point point = getDefaultOrientationWorldPointForPixel(x, y);

      ConvertToWorldCoordinates(point);

      return point;
   }

   public Point GetStochasticWorldPointInApertureForPixel(double x, double y) {
      Point point = getDefaultOrientationWorldPointForPixel(x, y);

      if (Settings.Aperture == null) {
         double xOffset = (Math.random() - .5);
         double yOffset = (Math.random() - .5);

         point = new Point(point.X + xOffset, point.Y + yOffset, point.Z);
      }
      else {
         Point originPoint = Settings.Aperture.GetOriginPoint();
         point.Plus(originPoint);
      }

      ConvertToWorldCoordinates(point);

      return point;
   }

   public Point GetStochasticWorldPointForPixel(double x, double y) {
      Point point = getDefaultOrientationWorldPointForPixel(x, y);

      double xOffset = (Math.random() - .5);
      double yOffset = (Math.random() - .5);

      point = new Point(point.X + xOffset, point.Y + yOffset, point.Z);

      ConvertToWorldCoordinates(point);

      return point;

   }

   @Override
   public Ray[] getInitialStochasticRaysForPixel(double x, double y, int samplesPerPixel) {

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

   @Override
   public Ray[] getInitialStochasticRaysForPixel(int x, int y, int samplesPerPixel) {
      return getInitialStochasticRaysForPixel((double) x, (double) y, samplesPerPixel);
   }

   @Override
   public Ray getStochasticRayForPixel(double x, double y) {
      Point centerPoint = GetStochasticWorldPointForPixel(x, y);
      Vector direction = Vector.Minus(centerPoint, _rearFocalPoint);
      direction.Normalize();
      direction.Scale(Settings.FocusDistance);
      Point focalPoint = Point.Plus(centerPoint, direction);
      return new Ray(centerPoint, Point.Minus(focalPoint, centerPoint));
   }





}