package net.danielthompson.danray.ui.gl.common;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by daniel on 3/8/16.
 */
public class GLMouseListener implements MouseListener, MouseMotionListener {

   private final GLCameraState _cameraState;

   private int _prevX;
   private int _prevY;
   private boolean _prevPressed;

   private boolean _pressed;

   private float _sensitivity = .01f;

   public GLMouseListener(GLCameraState cameraState) {
      _cameraState = cameraState;
   }

   @Override
   public void mouseClicked(MouseEvent mouseEvent) {
      _cameraState.hasFocus = true;
   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {

      _pressed = true;
   }

   @Override
   public void mouseReleased(MouseEvent mouseEvent) {
      _pressed = false;
      _prevPressed = false;
   }

   @Override
   public void mouseDragged(MouseEvent e) {

      int x = e.getX();
      int y = e.getY();

      if (_prevPressed) {
         float xDiff = _sensitivity * (float)(x - _prevX);
         float yDiff = _sensitivity * (float)(y - _prevY);

         _cameraState.ActiveDirectionMovement.X += yDiff;
         _cameraState.ActiveDirectionMovement.Y += xDiff;

      }

      _prevX = x;
      _prevY = y;
      _prevPressed = _pressed;
   }

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseEntered(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseExited(MouseEvent mouseEvent) {

   }
}