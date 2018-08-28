package net.danielthompson.danray.ui.gl.common;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by daniel on 3/8/16.
 */
public class GLKeyListener implements KeyListener {

   private final GLCameraState _cameraState;

   public GLKeyListener(GLCameraState cameraState) {

      _cameraState = cameraState;
      System.out.println("GLKeyListener()");
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {

      System.out.println("keyPressed()");

      int keyCode = e.getKeyCode();

      if (keyCode == KeyEvent.VK_ESCAPE) {
         _cameraState.hasFocus = false;
      }

      switch (keyCode) {
         case KeyEvent.VK_W: {
            _cameraState.MoveForward = true;
            System.out.println("W pressed");
            break;
         }
         case KeyEvent.VK_S: {
            _cameraState.MoveBackward = true;
            System.out.println("S pressed");
            break;
         }
         case KeyEvent.VK_A: {
            _cameraState.MoveLeft = true;
            System.out.println("A pressed");
            break;
         }
         case KeyEvent.VK_D: {
            _cameraState.MoveRight = true;
            System.out.println("D pressed");
            break;
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();

      switch (keyCode) {
         case KeyEvent.VK_W: {
            _cameraState.MoveForward = false;
            System.out.println("W released");
            break;
         }
         case KeyEvent.VK_S: {
            _cameraState.MoveBackward = false;
            System.out.println("S released");
            break;
         }
         case KeyEvent.VK_A: {
            _cameraState.MoveLeft = false;
            System.out.println("A released");
            break;
         }
         case KeyEvent.VK_D: {
            _cameraState.MoveRight = false;
            System.out.println("D released");
            break;
         }
      }
   }
}