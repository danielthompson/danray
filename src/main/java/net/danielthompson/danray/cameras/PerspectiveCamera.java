package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.*;

/**
 * Created by dthompson on 03 Apr 17.
 */
public class PerspectiveCamera extends Camera {

   private final float OneOverWidth;
   private final float OneOverHeight;

   private final float TwoOverWidth;
   private final float TwoOverHeight;

   private final float aspectRatio;
   private final float tanFOVOver2;

   private final float aspectTimesTanFovOver2;

   public PerspectiveCamera(final CameraSettings settings, final Transform cameraToWorld) {
      super(settings, cameraToWorld);

      OneOverWidth = 1.0f / (float)Settings.x;
      OneOverHeight = 1.0f / (float)Settings.y;

      TwoOverWidth = 2.0f / (float)Settings.x;
      TwoOverHeight = 2.0f / (float)Settings.y;

      aspectRatio = (float) Settings.x * OneOverHeight;
      tanFOVOver2 = (float)Math.tan(Math.toRadians(Settings.fov) * .5f);

      aspectTimesTanFovOver2 = aspectRatio * tanFOVOver2;
   }

   @Override
   public Ray getRay(Point2 pixel) {

      final float pixelCameraX = ((pixel.x + 0.5f) * TwoOverWidth - 1.0f) * aspectTimesTanFovOver2;
      final float pixelCameraY = (1.0f - (pixel.y + 0.5f) * TwoOverHeight) * tanFOVOver2;

      // world space
      final Ray ray = new Ray(new Point3(DefaultOrigin), pixelCameraX, pixelCameraY, -1.0f);

      // camera space
      cameraToWorld.applyInPlace(ray);
      ray.normalize();

      return ray;
   }
}
