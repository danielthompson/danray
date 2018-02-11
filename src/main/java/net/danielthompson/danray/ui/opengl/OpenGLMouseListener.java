package net.danielthompson.danray.ui.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLMouseListener implements MouseListener, MouseMotionListener {

   private final OpenGLCameraState _cameraState;

   private int _prevX;
   private int _prevY;
   private boolean _prevPressed;

   private boolean _pressed;

   private float _sensitivity = 0.05f;

   public OpenGLMouseListener(OpenGLCameraState cameraState) {
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

         _cameraState.Camera.moveDirectionAlongOrientation(0, xDiff, 0);
         _cameraState.Camera.moveDirectionAlongOrientation(yDiff, 0, 0);
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