package net.danielthompson.danray.ui.opengl;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLCameraState {
   public float _xRotation = 0;
   public float _yRotation = 0;
   public float _zRotation = 0;

   public boolean hasFocus = false;

   public float PrevMouseX;
   public float PrevMouseY;

   public float[] _position = {0, 0, 20};
   public float[] _direction = {0, 0, -1};
}
