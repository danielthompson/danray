package net.danielthompson.danray.ui.opengl;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLCameraState {
   public double _xRotation = 0;
   public double _yRotation = 0;
   public double _zRotation = 0;

   public boolean hasFocus = false;

   public double PrevMouseX;
   public double PrevMouseY;

   public double[] _position = {0, 0, 20};
   public double[] _direction = {0, 0, -1};
}
