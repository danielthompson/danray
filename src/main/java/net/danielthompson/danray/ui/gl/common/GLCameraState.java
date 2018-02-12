package net.danielthompson.danray.ui.gl.common;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 3/8/16.
 */
public class GLCameraState {

   public boolean hasFocus = false;

   public Camera Camera;

   public Vector ActiveOriginMovement;
   public boolean DecayOriginX = true;
   public boolean DecayOriginZ = true;


   public Vector ActiveDirectionMovement;

   public GLCameraState() {
      ActiveOriginMovement = new Vector(0, 0, 0);
      ActiveDirectionMovement = new Vector(0, 0, 0);
   }
}