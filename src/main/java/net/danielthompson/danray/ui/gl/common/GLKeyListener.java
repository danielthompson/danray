package net.danielthompson.danray.ui.gl.common;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.structures.Vector;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by daniel on 3/8/16.
 */
public class GLKeyListener implements KeyListener {

   private final GLCameraState _cameraState;

   public GLKeyListener(GLCameraState cameraState) {

      _cameraState = cameraState;
   }


   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {

      int keyCode = e.getKeyCode();

      if (keyCode == KeyEvent.VK_ESCAPE) {
         _cameraState.hasFocus = false;
         // OpenGLCanvas.animator.stop();
         //_canvas.destroy();
      }

      float x = 0.0f;
      float y = 0.0f;
      float z = 0.0f;

      switch (keyCode) {
         case KeyEvent.VK_W: {
            z += -10f;
            _cameraState.DecayOriginZ = false;
            break;
         }
         case KeyEvent.VK_S: {
            z += 10f;
            _cameraState.DecayOriginZ = false;
            break;
         }
         case KeyEvent.VK_A: {
            x += -10f;
            _cameraState.DecayOriginX = false;
            break;
         }
         case KeyEvent.VK_D: {
            x += 10f;
            _cameraState.DecayOriginX = false;
            break;
         }
      }

      _cameraState.ActiveOriginMovement.X += x;
      _cameraState.ActiveOriginMovement.Y += y;
      _cameraState.ActiveOriginMovement.Z += z;


   }

   @Override
   public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();

      switch (keyCode) {
         case KeyEvent.VK_W: {
            _cameraState.DecayOriginZ = true;
            break;
         }
         case KeyEvent.VK_S: {
            _cameraState.DecayOriginZ = true;
            break;
         }
         case KeyEvent.VK_A: {
            _cameraState.DecayOriginX = true;
            break;
         }
         case KeyEvent.VK_D: {
            _cameraState.DecayOriginX = true;
            break;
         }
      }
   }
}