package net.danielthompson.danray.cameras;

import net.danielthompson.danray.animation.CameraOrientationMovement;
import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.structures.Ray;

/**
 * Created by daniel on 5/3/15.
 */
public class CameraSettings {
   /**
    * Width of the image in pixels. Required.
    */
   public int X;

   /**
    * Height of the image in pixels. Required.
    */
   public int Y;

   /**
    * Focal length of the simulated lens. Must be >= 0. 0 is isometric; > 0 is perspective. Required.
    */
   public double FocalLength;

   /**
    * Rotation about the view vector in degrees (positive = clockwise). Optional; defaults to 0.
    */
   public double Rotation;

   /**
    * How much to shrink or enlarge the viewport. Optional; defaults to 1; less than 1 = zoomed in; greater than 1 = zoomed out.
    */
   public double ZoomFactor;

   /**
    * Camera's origin and direction in world space. Required.
    */
   public Ray Orientation;

   /**
    * Movement of the orientation in time. Optional.
    */
   public CameraOrientationMovement Movement;

   /**
    * Aperture of the lens. Results in an image with DOF. Optional.
    */
   public Aperture Aperture;

   /**
    * Distance in world coordinates to the focus point. >0 required for DOF, otherwise optional.
    */
   public double FocusDistance;

}