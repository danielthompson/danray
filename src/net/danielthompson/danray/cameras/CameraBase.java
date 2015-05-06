package net.danielthompson.danray.cameras;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

import java.text.NumberFormat;

/**
 * Created by daniel on 5/3/15.
 */
public abstract class CameraBase implements Camera {
   protected CameraSettings Settings;
   protected Vector _implicitDirection = new Vector(0, 0, -1);
   protected Vector zDir = new Vector(0, 0, 1);
   protected Ray _currentOrientation;

   protected int _currentFrame;
   protected Point _rearFocalPoint;

   public CameraBase(CameraSettings settings) {
      Settings = settings;
      _currentOrientation = settings.Orientation;

      if (Settings.FocalLength > 0.0) {
         _rearFocalPoint = calculateRearFocalPoint(settings.Orientation, Settings.FocalLength);
      }
   }

   private Point calculateRearFocalPoint(Ray orientation, double focalLength) {
      return orientation.GetPointAtT(-focalLength);
   }

   @Override
   public Vector getDirection() {
      if (_currentOrientation != null)
         return _currentOrientation.Direction;
      return Settings.Orientation.Direction;
   }

   public void setFrame(int frame) {
      _currentFrame = frame;

      if (Settings.Movement != null) {

         double percentage = (double) _currentFrame / (double) Settings.Movement.frame;

         Point newOrigin = Point.Interpolate(Settings.Orientation.Origin, Settings.Movement.orientation.Origin, percentage);
         Vector newDirection = Vector.Interpolate(Settings.Orientation.Direction, Settings.Movement.orientation.Direction, percentage);

         _currentOrientation = new Ray(newOrigin, newDirection);
         _rearFocalPoint = calculateRearFocalPoint(_currentOrientation, Settings.FocalLength);

         NumberFormat percentFormatter = NumberFormat.getPercentInstance();
         String percentOut = percentFormatter.format(percentage);

         Logger.Log("frame = [" + _currentFrame + "], movement frames = [" + Settings.Movement.frame + "], percentage = [" + percentOut + "]");
         Logger.Log("origin = [" + newOrigin + "], direction = [" + newDirection + "]");
      }
   }

   public void moveOrigin(Vector offset) {
      Settings.Orientation.Origin.Plus(offset);
      _rearFocalPoint = calculateRearFocalPoint(Settings.Orientation, Settings.FocalLength);
   }

   public Point getOrigin() {
      if (_currentOrientation != null)
         return _currentOrientation.Origin;
      return Settings.Orientation.Origin;
   }

   public Point getWorldPointForPixel(double x, double y) {
      // we define the absolute midpoint of the camera's screen to be at _currentOrientation.location.
      // we arbitrarily assume that our camera is pointed at [0, 0, -1] -> straight down the z axis.

      // point is defined to be the midpoint of the camera in x and y: NDC (.5, .5):
      //Point point = new Point(_currentOrientation.Origin.X, _currentOrientation.Origin.Y, _currentOrientation.Origin.Z);

      // first, find the world coordinate for the given pixel with this default camera orientation:
      //int offsetX =

      Point point = getDefaultOrientationWorldPointForPixel(x, y);

      ConvertToWorldCoordinates(point);

      return point;
   }

   protected void ConvertToWorldCoordinates(Point point) {
      double dot = _currentOrientation.Direction.Dot(_implicitDirection);

      if (!Constants.WithinDelta(dot, 1))
      {
         Vector rotationDirection = _currentOrientation.Direction.Cross(zDir);
         rotationDirection.Normalize();
         Ray rotationAxis = new Ray(_currentOrientation.Origin, rotationDirection);

         double theta = Math.toDegrees(Math.acos(_currentOrientation.Direction.Dot(_implicitDirection)));

         point.Rotate(rotationAxis, theta);
         point.Rotate(_currentOrientation, Settings.Rotation);
      }
   }

   protected Point getDefaultOrientationWorldPointForPixel(double x, double y) {
      double Cx = ( _currentOrientation.Origin.X + Settings.ZoomFactor * (x - (double)Settings.X * .5));
      double Cy = ( _currentOrientation.Origin.Y + Settings.ZoomFactor * ((double)Settings.Y * .5 - y));
      double Cz = _currentOrientation.Origin.Z;

      return new Point(Cx, Cy, Cz);
   }

   @Override
   public Ray getStochasticRayForPixel(double x, double y) {
      return null;
   }

   @Override
   public Ray[] getInitialStochasticRaysForPixel(double x, double y, int samplesPerPixel) {
      return new Ray[0];
   }

   @Override
   public Point getWorldPointForPixel(int x, int y) {
      return getWorldPointForPixel((double) x, (double) y);
   }
}
