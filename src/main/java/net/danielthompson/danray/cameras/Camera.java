package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 1/12/14.
 */
public abstract class Camera {

   public CameraSettings Settings;

   private static final Vector DefaultDirection = new Vector(0, 0, -1);
   static final Point DefaultOrigin = new Point(0, 0, 0);

   public Transform cameraToWorld;

   public Camera(CameraSettings settings, Transform cameraToWorld) {
      Settings = settings;
      this.cameraToWorld = cameraToWorld;
   }

   public Vector getDirection() {
      return cameraToWorld.Apply(DefaultDirection);
   }

   public Point getOrigin() {
      return cameraToWorld.Apply(DefaultOrigin);
   }

   public void setFrame(int frame) {
   }

   /**
    * Moves the camera origin by the specified vector.
    * @param delta
    */
   public void moveOriginAlongAxis(Vector delta) {
      Transform t = Transform.Translate(delta);
      cameraToWorld = t.Apply(cameraToWorld);
   }

   public void moveOriginAlongOrientation(Vector delta) {
      Transform t = Transform.Translate(delta);
      cameraToWorld = cameraToWorld.Apply(t);
   }

   public void moveDirectionAlongOrientation(Vector delta) {
      moveDirectionAlongOrientation(delta.X, delta.Y, delta.Z);
   }

   public void moveDirectionAlongOrientation(float x, float y, float z) {

      Transform[] inputTransforms = new Transform[] {
            Transform.RotateX(x),
            Transform.RotateY(y),
            Transform.RotateZ(z)
      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);
      cameraToWorld = cameraToWorld.Apply(compositeTransforms[0]);
   }

   public void moveDirectionAlongAxis(float x, float y, float z) {

      Transform[] inputTransforms = new Transform[] {
            Transform.RotateX(x),
            Transform.RotateY(y),
            Transform.RotateZ(z)
      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);
      cameraToWorld = compositeTransforms[0].Apply(cameraToWorld);
   }

   public Ray[] getRays(float x, float y, int samples)
   {
      Ray[] rays = new Ray[samples];

      for (int i = 0; i < samples; i++) {
         rays[i] = getRay(x, y);
      }

      return rays;
   }

   public Ray[] getRays(float[][] sampleLocations, int samples) {

      Ray[] rays = new Ray[samples];

      for (int i = 0; i < samples; i++) {
         rays[i] = getRay(sampleLocations[i][0], sampleLocations[i][1]);
      }

      return rays;
   }

   public Ray getRay(float x, float y) {
      throw new java.lang.UnsupportedOperationException();
   }
}
