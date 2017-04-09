package net.danielthompson.danray.ui.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLMouseListener implements MouseListener, MouseMotionListener {

   private final OpenGLCameraState _cameraState;
   private boolean _first = true;

   public OpenGLMouseListener(OpenGLCameraState cameraState) {

      _cameraState = cameraState;
   }

   @Override
   public void mouseClicked(MouseEvent mouseEvent) {

      resetMousePosition(mouseEvent);
      _cameraState.hasFocus = true;


   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseReleased(MouseEvent mouseEvent) {

   }



   @Override
   public void mouseDragged(MouseEvent e) {

   }

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {

//      if (_cameraState.hasFocus) {
//         if (_first) {
//            _first = false;
//
//         } else {
//            // mouse x - rotate about y axis
//
//            float xDiff = (mouseEvent.getX() - _cameraState.PrevMouseX) / 5.0f;
//            _cameraState._yRotation += xDiff;
//            if (_cameraState._yRotation >= 360)
//               _cameraState._yRotation %= 360;
//
//            while (_cameraState._yRotation < 0) {
//               _cameraState._yRotation += 360;
//            }
//
//            // mouse y - rotate about x axis
//
//            float yDiff = (mouseEvent.getY() - _cameraState.PrevMouseY) / 5.0f;
//            _cameraState._xRotation += yDiff;
//
//            if (_cameraState._xRotation >= 360)
//               _cameraState._xRotation %= 360;
//
//            while (_cameraState._xRotation < 0) {
//               _cameraState._xRotation += 360;
//            }
//         }
//         resetMousePosition(mouseEvent);
         /*_cameraState.PrevMouseX = mouseEvent.getX();
         _cameraState.PrevMouseY = mouseEvent.getY();*/
//      }

   }

   private void resetMousePosition(MouseEvent event) {
      _cameraState.PrevMouseX = event.getX();
      _cameraState.PrevMouseY = event.getY();
   }
//
//   @Override
//   public void mouseDragged(MouseEvent mouseEvent) {
//
//   }
//
//   @Override
//   public void mouseWheelMoved(MouseEvent mouseEvent) {
//
//   }

   @Override
   public void mouseEntered(MouseEvent mouseEvent) {
      resetMousePosition(mouseEvent);
   }

   @Override
   public void mouseExited(MouseEvent mouseEvent) {

   }

}
