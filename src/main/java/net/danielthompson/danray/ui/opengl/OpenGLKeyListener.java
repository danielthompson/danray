package net.danielthompson.danray.ui.opengl;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.structures.Vector;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLKeyListener implements KeyListener {

   private final OpenGLCameraState _cameraState;


   public OpenGLKeyListener(OpenGLCameraState cameraState) {

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

      switch (keyCode) {
         case KeyEvent.VK_W: {
            _cameraState.Camera.moveOriginAlongOrientation(new Vector(0f, 0f, -10f));
            break;
         }
         case KeyEvent.VK_S: {
            _cameraState.Camera.moveOriginAlongOrientation(new Vector(0f, 0f, 10f));
            break;
         }
         case KeyEvent.VK_A: {
            _cameraState.Camera.moveOriginAlongOrientation(new Vector(-10f, 0f, 0f));
            break;
         }
         case KeyEvent.VK_D: {
            _cameraState.Camera.moveOriginAlongOrientation(new Vector(10f, 0f, 0f));
            break;
         }

      }

   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}