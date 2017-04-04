package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.*;

/**
 * Created by dthompson on 03 Apr 17.
 */
public class TransformCamera extends Camera {
   public CameraSettings Settings;

   public final Vector forward = new Vector(0, 0, -1);
   public final Vector up = new Vector(0, 1, 0);
   public final Vector right = new Vector(1, 0, 0);

   public Point _rearFocalPoint;

   public Transform cameraToWorld;

   public TransformCamera(CameraSettings settings, Transform cameraToWorld) {
      super(settings);
      this.cameraToWorld = cameraToWorld;

      _rearFocalPoint = new Point(0.5f, 0.5f, settings.FocalLength);

   }

   public Ray getRay(float x, float y) {
      float scaledX = x / Settings.X;
      float scaledY = y / Settings.Y;

      Point imagePlanePoint = new Point(scaledX, scaledY, 0);
      Vector direction = Vector.Minus(imagePlanePoint, _rearFocalPoint);

      Ray cameraSpaceRay = new Ray(imagePlanePoint, direction);

      Ray worldSpaceRay = cameraToWorld.Apply(cameraSpaceRay);

      return worldSpaceRay;
   }

   public Ray[] getInitialStochasticRaysForPixel(float x, float y, int samplesPerPixel) {

      Ray r = getRay(x, y);

      return new Ray[] { r };
   }

}
