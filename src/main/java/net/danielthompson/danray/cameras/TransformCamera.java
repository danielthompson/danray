package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.*;

/**
 * Created by dthompson on 03 Apr 17.
 */
public class TransformCamera extends Camera {

   public static final Point Origin = new Point(0, 0, 0);

   public Transform cameraToWorld;

   private float OneOverWidth;

   private float OneOverHeight;

   public TransformCamera(CameraSettings settings, Transform cameraToWorld) {
      super(settings);
      this.cameraToWorld = cameraToWorld;

      OneOverWidth = 1.0f / (float)Settings.X;
      OneOverHeight = 1.0f / (float)Settings.Y;
   }

   public Ray getRay(float x, float y) {

      float pixelNDCx = (x + 0.5f) * OneOverWidth;
      float pixelNDCy = (y + 0.5f) * OneOverHeight;

      float aspectRatio = (float) Settings.X * OneOverHeight;

      float tanFOVOver2 = (float)Math.tan(Math.toRadians(Settings.FieldOfView) * .5f);

      float pixelCameraX = (2 * pixelNDCx - 1) * aspectRatio * tanFOVOver2;
      float pixelCameraY = (1 - 2 * pixelNDCy) * tanFOVOver2;

      Point imagePlanePixelInCameraSpace = new Point(pixelCameraX, pixelCameraY, -1);

      Vector direction = new Vector (imagePlanePixelInCameraSpace.X, imagePlanePixelInCameraSpace.Y, imagePlanePixelInCameraSpace.Z);

      Ray cameraSpaceRay = new Ray(Origin, direction);

      Ray worldSpaceRay = cameraToWorld.Apply(cameraSpaceRay);

      return worldSpaceRay;
   }

   public Ray[] getRays(float x, float y, int samples) {

      Ray[] rays = new Ray[samples];

      for (int i = 0; i < samples; i++) {
         rays[i] = getRay(x, y);
      }

      return rays;
   }

   @Override
   public Ray[] getRays(float[][] sampleLocations, int samples) {

      Ray[] rays = new Ray[samples];

      for (int i = 0; i < samples; i++) {
         rays[i] = getRay(sampleLocations[i][0], sampleLocations[i][1]);
      }

      return rays;
   }


}
