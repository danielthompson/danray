package net.danielthompson.danray.ui.gl.common;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.structures.Vector3;

/**
 * Created by daniel on 3/8/16.
 */
public class GLCameraState {

   public boolean hasFocus = false;

   public Camera Camera;

   public Vector3 ActiveOriginMovement;
   public boolean MoveForward = false;
   public boolean MoveBackward = false;
   public boolean MoveLeft = false;
   public boolean MoveRight = false;


   public Vector3 ActiveDirectionMovement;

   public GLCameraState() {
      ActiveOriginMovement = new Vector3(0, 0, 0);
      ActiveDirectionMovement = new Vector3(0, 0, 0);
   }
}