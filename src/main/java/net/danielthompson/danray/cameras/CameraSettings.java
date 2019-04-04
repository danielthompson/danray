package net.danielthompson.danray.cameras;

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
    * Focal length of the simulated lens. Must be 0 &lt;= fov &gt;= 360. Required.
    */
   public float FieldOfView;
}
