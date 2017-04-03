package net.danielthompson.danray.cameras;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

import java.text.NumberFormat;

/**
 * Created by daniel on 1/12/14.
 */
public abstract class Camera {

   public CameraSettings Settings;
   protected final Vector _implicitDirection = new Vector(0, 0, -1);
   protected final Vector zDir = new Vector(0, 0, 1);
   public Ray _currentOrientation;

   protected int _currentFrame;
   public Point _rearFocalPoint;

   public Camera(CameraSettings settings) {
      Settings = settings;
      _currentOrientation = settings.Orientation;

      if (Settings.FocalLength > 0.0) {
         _rearFocalPoint = calculateRearFocalPoint(settings.Orientation, Settings.FocalLength);
      }
   }

   private Point calculateRearFocalPoint(Ray orientation, float focalLength) {
      return orientation.GetPointAtT(-focalLength);
   }


   public Vector getDirection() {
      if (_currentOrientation != null)
         return _currentOrientation.Direction;
      return Settings.Orientation.Direction;
   }

   public void setFrame(int frame) {
      _currentFrame = frame;

      if (Settings.Movement != null) {

         float percentage = (float) _currentFrame / (float) Settings.Movement.frame;

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

   public Point getWorldPointForPixel(float x, float y) {
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
      float dot = _currentOrientation.Direction.Dot(_implicitDirection);

      if (Constants.WithinEpsilon(dot, -1)) {
         point.Z = -point.Z;
      }

      else if (!Constants.WithinEpsilon(dot, 1)) {
         Vector rotationDirection = _currentOrientation.Direction.Cross(zDir);
         rotationDirection.Normalize();
         Ray rotationAxis = new Ray(_currentOrientation.Origin, rotationDirection);

         float theta = (float) Math.toDegrees(Math.acos(_currentOrientation.Direction.Dot(_implicitDirection)));

         point.Rotate(rotationAxis, theta);
         point.Rotate(_currentOrientation, Settings.Rotation);
      }
   }

   protected Point getDefaultOrientationWorldPointForPixel(float x, float y) {
      float Cx = _currentOrientation.Origin.X + Settings.ZoomFactor * (x - (float) Settings.X * .5f);
      float Cy = _currentOrientation.Origin.Y + Settings.ZoomFactor * ((float) Settings.Y * .5f - y);
      float Cz = _currentOrientation.Origin.Z;

      return new Point(Cx, Cy, Cz);
   }

   public Ray getStochasticRayForPixel(float x, float y) {
      return null;
   }

   public Ray[] getInitialStochasticRaysForPixel(float x, float y, int samplesPerPixel) {
      return new Ray[0];
   }

   public Point getWorldPointForPixel(int x, int y) {
      return getWorldPointForPixel((float) x, (float) y);
   }
}
