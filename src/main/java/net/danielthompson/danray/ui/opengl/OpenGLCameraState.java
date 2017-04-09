package net.danielthompson.danray.ui.opengl;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.structures.Matrix4x4;
import net.danielthompson.danray.structures.Transform;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLCameraState {

   public boolean hasFocus = false;

   public float PrevMouseX;
   public float PrevMouseY;

   public Camera Camera;

}
