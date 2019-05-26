package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.*;

/**
 * Created by daniel on 1/12/14.
 */
public abstract class Camera {

   public final CameraSettings Settings;

   private static final Vector3 DefaultDirection = new Vector3(0, 0, -1);
   static final Point3 DefaultOrigin = new Point3(0, 0, 0);

   public Transform cameraToWorld;

   public Camera(final CameraSettings settings, final Transform cameraToWorld) {
      Settings = settings;
      this.cameraToWorld = cameraToWorld;
   }

   public Vector3 getDirection() {
      return cameraToWorld.apply(DefaultDirection);
   }

   public Point3 getOrigin() {
      return cameraToWorld.apply(DefaultOrigin);
   }

   public void setFrame(final int frame) {
   }

   /**
    * Moves the camera origin by the specified vector.
    * @param delta
    */
   public void moveOriginAlongAxis(final Vector3 delta) {
      Transform t = Transform.translate(delta);
      cameraToWorld = t.apply(cameraToWorld);
   }

   public void moveOriginAlongOrientation(final Vector3 delta) {
      final Transform t = Transform.translate(delta);
      cameraToWorld = cameraToWorld.apply(t);
   }

   public void moveDirectionAlongOrientation(final Vector3 delta) {
      final Transform[] inputTransforms = new Transform[] {
            Transform.rotateX(delta.x),
            Transform.rotateY(delta.y),
            Transform.rotateZ(delta.z)
      };
      final Transform[] compositeTransforms = Transform.composite(inputTransforms);
      cameraToWorld = cameraToWorld.apply(compositeTransforms[0]);
   }

   public void moveDirectionAlongAxis(final Vector3 delta) {

      final Transform[] inputTransforms = new Transform[] {
            Transform.rotateX(delta.x),
            Transform.rotateY(delta.y),
            Transform.rotateZ(delta.z)
      };
      final Transform[] compositeTransforms = Transform.composite(inputTransforms);
      cameraToWorld = compositeTransforms[0].apply(cameraToWorld);
   }

   public Ray[] getRays(final Point2 pixel, final int samples)
   {
      final Ray[] rays = new Ray[samples];

      for (int i = 0; i < samples; i++) {
         rays[i] = getRay(pixel);
      }

      return rays;
   }

   public Ray[] getRays(final Point2[] pixels, final int samples) {

      final Ray[] rays = new Ray[samples];

      for (int i = 0; i < samples; i++) {
         rays[i] = getRay(pixels[i]);
      }

      return rays;
   }

   public abstract Ray getRay(Point2 location);
}
